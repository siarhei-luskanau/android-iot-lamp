package siarhei.luskanau.iot.lamp.android_things.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import timber.log.Timber;

public class LampGpioView {

    private static final String GPIO_LAMP = "BCM6";

    @NonNull
    private final ListenLampStateUseCase listenLampStateUseCase;
    @Nullable
    private Gpio lampGpio;

    public LampGpioView(@NonNull final ListenLampStateUseCase listenLampStateUseCase) {
        this.listenLampStateUseCase = listenLampStateUseCase;
    }

    public void open() {
        try {
            final PeripheralManager peripheralManager = PeripheralManager.getInstance();
            lampGpio = peripheralManager.openGpio(GPIO_LAMP);
            lampGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Timber.i("Start blinking LED GPIO pin");
        } catch (final Throwable e) {
            Timber.e(e, "Error on PeripheralIO API");
        }

        listenLampStateUseCase.execute(
                new DefaultObserver<Boolean>() {

                    @Override
                    public void onNext(final Boolean lampState) {
                        onLampStateUpdated(lampState);
                    }
                },
                null
        );
    }

    public void close() {
        if (lampGpio != null) {
            Timber.i("Closing LED GPIO pin");
            try {
                lampGpio.close();
            } catch (final IOException e) {
                Timber.e(e, "Error on PeripheralIO API");
            } finally {
                lampGpio = null;
            }
        }
    }

    private void onLampStateUpdated(final Boolean lampState) {
        if (lampGpio != null) {
            try {
                lampGpio.setValue(lampState);
                Timber.d("State set to %s", lampState);
            } catch (final IOException e) {
                Timber.e(e, "Error on PeripheralIO API");
            }
        }
    }
}
