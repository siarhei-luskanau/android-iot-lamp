package siarhei.luskanau.iot.lamp.android_things.view;

import android.support.annotation.NonNull;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;

import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.ListenLampProgressUseCase;
import timber.log.Timber;

public class PwmGpioView {

    private static final String GPIO_PWM = "PWM0";
    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 3;
    private static final double PULSE_PERIOD_MS = 20;  // Frequency of 50Hz (1000/20)

    @NonNull
    private final ListenLampProgressUseCase listenLampProgressUseCase;

    private Pwm pwm;

    public PwmGpioView(@NonNull final ListenLampProgressUseCase listenLampProgressUseCase) {
        this.listenLampProgressUseCase = listenLampProgressUseCase;
    }

    public void open() {
        try {
            final PeripheralManagerService service = new PeripheralManagerService();
            pwm = service.openPwm(GPIO_PWM);
            // Always set frequency and initial duty cycle before enabling PWM
            pwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            //showLampProgress(0.5);
            pwm.setEnabled(true);
        } catch (final Throwable e) {
            Timber.e(e, "Error on PeripheralIO API");
        }

        listenLampProgressUseCase.execute(
                new DefaultObserver<Double>() {

                    @Override
                    public void onNext(final Double lampProgress) {
                        onLampProgressUpdated(lampProgress);
                    }
                },
                null
        );
    }

    public void close() {
        if (pwm != null) {
            Timber.i("Closing Pwm GPIO pin");
            try {
                pwm.close();
            } catch (final IOException e) {
                Timber.e(e, "Error on PeripheralIO API");
            } finally {
                pwm = null;
            }
        }
    }

    private void onLampProgressUpdated(final Double lampProgress) {
        if (pwm != null && lampProgress != null) {
            try {
                final double pwmDutyCycle = MIN_ACTIVE_PULSE_DURATION_MS +
                        lampProgress * (PULSE_PERIOD_MS / 2 - MIN_ACTIVE_PULSE_DURATION_MS);
                pwm.setPwmDutyCycle(pwmDutyCycle);
                Timber.d("Pwm:  Progress=%s  PwmDutyCycle=%s", lampProgress, pwmDutyCycle);
            } catch (final IOException e) {
                Timber.e(e, "Error on PeripheralIO API");
            }
        }
    }
}
