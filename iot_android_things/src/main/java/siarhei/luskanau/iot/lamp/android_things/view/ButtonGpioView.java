package siarhei.luskanau.iot.lamp.android_things.view;

import android.support.annotation.NonNull;

import com.google.android.things.contrib.driver.button.Button;

import java.io.IOException;

import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.SendLampStateUseCase;
import timber.log.Timber;

public class ButtonGpioView {

    private static final String GPIO_BUTTON = "BCM22";

    @NonNull
    private final ListenLampStateUseCase listenLampStateUseCase;
    @NonNull
    private final SendLampStateUseCase sendLampStateUseCase;

    private Button button;
    private boolean lampState;

    public ButtonGpioView(
            @NonNull final ListenLampStateUseCase listenLampStateUseCase,
            @NonNull final SendLampStateUseCase sendLampStateUseCase
    ) {
        this.listenLampStateUseCase = listenLampStateUseCase;
        this.sendLampStateUseCase = sendLampStateUseCase;
    }

    public void open() {
        try {
            button = new Button(GPIO_BUTTON, Button.LogicState.PRESSED_WHEN_LOW);
            button.setOnButtonEventListener((button, pressed) -> {
                if (pressed) {
                    Timber.i("GPIO changed, button pressed");
                    final boolean newLampState = !lampState;
                    sendLampStateUseCase.execute(
                            new DefaultObserver<>(),
                            SendLampStateUseCase.Params.forLampState(newLampState)
                    );
                }
            });
        } catch (final Throwable e) {
            Timber.e(e, "Error on PeripheralIO API");
        }

        listenLampStateUseCase.execute(
                new DefaultObserver<Boolean>() {

                    @Override
                    public void onNext(final Boolean lampState) {
                        ButtonGpioView.this.lampState = lampState;
                    }
                },
                null
        );
    }

    public void close() {
        if (button != null) {
            try {
                button.close();
            } catch (final IOException e) {
                Timber.e(e, "Error closing Button driver");
            } finally {
                button = null;
            }
        }
    }
}
