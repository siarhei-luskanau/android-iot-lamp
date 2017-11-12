package siarhei.luskanau.iot.lamp.iot.dagger.component;

import dagger.Subcomponent;
import siarhei.luskanau.iot.lamp.iot.BlinkActivity;
import siarhei.luskanau.iot.lamp.iot.dagger.modules.LampModule;

@Subcomponent(modules = LampModule.class)
public interface LampComponent {

    void inject(BlinkActivity blinkActivity);
}
