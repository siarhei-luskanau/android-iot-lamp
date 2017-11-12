package siarhei.luskanau.iot.lamp.remote.control.dagger.component;

import dagger.Subcomponent;
import siarhei.luskanau.iot.lamp.remote.control.MainActivity;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.LampModule;

@Subcomponent(modules = LampModule.class)
public interface LampComponent {

    void inject(MainActivity mainActivity);
}
