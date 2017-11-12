package siarhei.luskanau.iot.lamp.android_things;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.lamp.android_things.view.ButtonGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.LampGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.PwmGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.display.IDisplayView;

public class PeripheralService {

    @NonNull
    private final LampGpioView lampGpioView;
    @NonNull
    private final ButtonGpioView buttonGpioView;
    @NonNull
    private final PwmGpioView pwmGpioView;
    @NonNull
    private final IDisplayView displayView;

    public PeripheralService(
            @NonNull final LampGpioView lampGpioView,
            @NonNull final ButtonGpioView buttonGpioView,
            @NonNull final PwmGpioView pwmGpioView,
            @NonNull final IDisplayView displayView
    ) {
        this.lampGpioView = lampGpioView;
        this.buttonGpioView = buttonGpioView;
        this.pwmGpioView = pwmGpioView;
        this.displayView = displayView;
    }

    public void openPeripheral() {
        lampGpioView.open();
        buttonGpioView.open();
        pwmGpioView.open();
        displayView.open();
    }

    @SuppressWarnings("unused")
    public void closePeripheral() {
        displayView.close();
        pwmGpioView.close();
        buttonGpioView.close();
        lampGpioView.close();
    }
}
