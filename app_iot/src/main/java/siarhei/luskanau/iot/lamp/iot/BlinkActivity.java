package siarhei.luskanau.iot.lamp.iot;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.Checkable;
import android.widget.Toast;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.iot.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.iot.dagger.component.LampComponent;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampStatePresenter;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampStateView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampStatePresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampStateView;

public class BlinkActivity extends BaseComponentActivity implements ListenLampStateView, SendLampStateView {

    private static final String TAG = BlinkActivity.class.getSimpleName();

    @Inject
    protected ListenLampStatePresenter listenLampStatePresenter;
    @Inject
    protected SendLampStatePresenter sendLampStatePresenter;

    private Gpio mLedGpio;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Starting BlinkActivity");
        this.initializeInjector();

        listenLampStatePresenter.setView(this);
        sendLampStatePresenter.setView(this);

        try {
            setContentView(R.layout.activity_main);
            switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
            switchCompat.setOnClickListener(v -> {
                boolean isChecked = ((Checkable) v).isChecked();
                sendLampStatePresenter.sendLampState(isChecked);
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            String pinName = BoardDefaults.getGPIOForLED();
            mLedGpio = service.openGpio(pinName);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    private void initializeInjector() {
        LampComponent lampComponent = DaggerLampComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        lampComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listenLampStatePresenter.listenLampState();
    }

    @Override
    protected void onPause() {
        super.onPause();

        listenLampStatePresenter.destroy();
        sendLampStatePresenter.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }

    @Override
    public void showLampState(Boolean lampState) {
        try {
            if (mLedGpio != null) {
                mLedGpio.setValue(lampState);
                Log.d(TAG, "State set to " + lampState);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        if (switchCompat != null) {
            switchCompat.setChecked(lampState);
        }
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Log.e(TAG, "showErrorMessage: " + errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
