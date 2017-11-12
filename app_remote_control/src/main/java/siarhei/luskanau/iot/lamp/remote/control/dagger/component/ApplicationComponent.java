package siarhei.luskanau.iot.lamp.remote.control.dagger.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    LampRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();

    /**
     * SubComponents
     */

    LampComponent subLampComponent();
}