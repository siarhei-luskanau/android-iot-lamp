package siarhei.luskanau.iot.lamp.iot;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.iot.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.iot.dagger.component.LampComponent;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampView;

public class BlinkActivity extends BaseComponentActivity implements ListenLampView, SendLampView {

    private static final String TAG = BlinkActivity.class.getSimpleName();
    private static final String GPIO_LAMP = "BCM6";
    private static final String GPIO_BUTTON = "BCM22";
    private static final String GPIO_PWM = "PWM0";
    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 3;
    private static final double PULSE_PERIOD_MS = 20;  // Frequency of 50Hz (1000/20)

    @Inject
    protected ListenLampPresenter listenLampStatePresenter;
    @Inject
    protected SendLampPresenter sendLampStatePresenter;

    private Gpio lampGpio;
    private ButtonInputDriver buttonInputDriver;
    private Pwm pwm;
    private SwitchCompat switchCompat;
    private boolean lampState;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Starting BlinkActivity");
        this.initializeInjector();

        listenLampStatePresenter.setView(this);
        sendLampStatePresenter.setView(this);

        try {
            setContentView(R.layout.activity_main);
            switchCompat = findViewById(R.id.switchCompat);
            switchCompat.setOnClickListener(v -> {
                final boolean isChecked = switchCompat.isChecked();
                sendLampStatePresenter.sendLampState(isChecked);
            });
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        try {
            buttonInputDriver = new ButtonInputDriver(
                    GPIO_BUTTON,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_SPACE);
            buttonInputDriver.register();
        } catch (final Throwable e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        try {
            final PeripheralManagerService service = new PeripheralManagerService();
            lampGpio = service.openGpio(GPIO_LAMP);
            lampGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
        } catch (final Throwable e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        try {
            final PeripheralManagerService service = new PeripheralManagerService();
            pwm = service.openPwm(GPIO_PWM);
            // Always set frequency and initial duty cycle before enabling PWM
            pwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            showLampProgress(0.5);
            pwm.setEnabled(true);
        } catch (final Throwable e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private void initializeInjector() {
        final LampComponent lampComponent = DaggerLampComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        lampComponent.inject(this);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {

            Log.i(TAG, "GPIO changed, button pressed");
            final boolean newLampState = !lampState;
            showLampState(newLampState);
            sendLampStatePresenter.sendLampState(newLampState);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listenLampStatePresenter.listenLampState();
        listenLampStatePresenter.listenLampProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listenLampStatePresenter.destroy();
        sendLampStatePresenter.destroy();

        if (pwm != null) {
            Log.i(TAG, "Closing Pwm GPIO pin");
            try {
                pwm.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                pwm = null;
            }
        }

        if (lampGpio != null) {
            Log.i(TAG, "Closing LED GPIO pin");
            try {
                lampGpio.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            } finally {
                lampGpio = null;
            }
        }

        if (buttonInputDriver != null) {
            buttonInputDriver.unregister();
            try {
                buttonInputDriver.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                buttonInputDriver = null;
            }
        }
    }

    @Override
    public void showLampState(final Boolean lampState) {
        this.lampState = lampState;
        if (lampGpio != null) {
            try {
                lampGpio.setValue(lampState);
                Log.d(TAG, "State set to " + lampState);
            } catch (final IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }

        if (switchCompat != null) {
            switchCompat.setChecked(lampState);
        }
    }

    @Override
    public void showLampProgress(final Double lampProgress) {
        if (pwm != null && lampProgress != null) {
            try {
                final double pwmDutyCycle = MIN_ACTIVE_PULSE_DURATION_MS +
                        lampProgress * (PULSE_PERIOD_MS / 2 - MIN_ACTIVE_PULSE_DURATION_MS);
                pwm.setPwmDutyCycle(pwmDutyCycle);
                Log.d(TAG, String.format(Locale.ENGLISH, "Pwm:  Progress=%s  PwmDutyCycle=%s", lampProgress, pwmDutyCycle));
            } catch (final IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Log.e(TAG, "showErrorMessage: " + errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
