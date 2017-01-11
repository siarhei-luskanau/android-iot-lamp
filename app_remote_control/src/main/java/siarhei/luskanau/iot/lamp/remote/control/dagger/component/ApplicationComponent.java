package siarhei.luskanau.iot.lamp.remote.control.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.lamp.remote.control.dagger.scope.ApplicationScope;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    LampRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();
}