package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.data.firebase.FirebaseLampRepository;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.JobExecutor;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.executor.UIThread;
import siarhei.luskanau.iot.lamp.iot.dagger.scope.ApplicationScope;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(final Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @ApplicationScope
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @ApplicationScope
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    @ApplicationScope
    LampRepository provideLampRepository() {
        return new FirebaseLampRepository();
    }

    @Provides
    @ApplicationScope
    ErrorMessageFactory provide() {
        return new SimpleErrorMessageFactory();
    }
}
