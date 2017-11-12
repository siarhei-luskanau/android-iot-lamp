package siarhei.luskanau.iot.lamp.android_things.dagger;

import dagger.Subcomponent;
import siarhei.luskanau.iot.lamp.android_things.PeripheralService;

@Subcomponent(modules = PeripheralModule.class)
public interface PeripheralComponent {

    PeripheralService peripheralUtils();
}
