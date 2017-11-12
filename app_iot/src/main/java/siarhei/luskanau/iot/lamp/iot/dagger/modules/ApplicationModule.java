package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.data.firebase.FirebaseLampRepository;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.exception.SimpleErrorMessageFactory;

@Module
public class ApplicationModule {

    @NonNull
    private final Application application;

    public ApplicationModule(@NonNull final Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @NonNull
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    @NonNull
    LampRepository provideLampRepository() {
        return new FirebaseLampRepository();
    }

    @Provides
    @Singleton
    @NonNull
    ErrorMessageFactory provide() {
        return new SimpleErrorMessageFactory();
    }
}
