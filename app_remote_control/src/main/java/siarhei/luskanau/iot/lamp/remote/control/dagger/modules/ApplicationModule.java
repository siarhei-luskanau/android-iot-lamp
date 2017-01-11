package siarhei.luskanau.iot.lamp.remote.control.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.data.firebase.FirebaseLampRepository;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.remote.control.AppApplication;
import siarhei.luskanau.iot.lamp.remote.control.executor.JobExecutor;
import siarhei.luskanau.iot.lamp.remote.control.executor.UIThread;

@Module
public class ApplicationModule {

    private final AppApplication application;

    public ApplicationModule(AppApplication application) {
        this.application = application;
    }

    @Provides
    AppApplication provideApplicationContext() {
        return this.application;
    }

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    LampRepository provideLampRepository() {
        return new FirebaseLampRepository();
    }

    @Provides
    ErrorMessageFactory provide() {
        return new SimpleErrorMessageFactory();
    }
}
