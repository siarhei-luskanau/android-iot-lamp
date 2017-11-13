package siarhei.luskanau.iot.lamp.android_things.dagger;

import android.app.Application;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import siarhei.luskanau.iot.lamp.android_things.PeripheralService;
import siarhei.luskanau.iot.lamp.android_things.view.ButtonGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.LampGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.PwmGpioView;
import siarhei.luskanau.iot.lamp.android_things.view.display.IDisplayView;
import siarhei.luskanau.iot.lamp.android_things.view.display.Ssd1306DisplayView;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.ISchedulerSet;
import siarhei.luskanau.iot.lamp.domain.interactor.message.ListenLampMessageUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.ListenLampProgressUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.SendLampStateUseCase;

@Module
public class PeripheralModule {

    @Provides
    @NonNull
    ISchedulerSet provideSchedulerSet() {
        return new ISchedulerSet() {

            private final Scheduler subscribeOn = Schedulers.io();
            private final Scheduler observeOn = Schedulers.computation();

            @Override
            @NonNull
            public Scheduler subscribeOn() {
                return subscribeOn;
            }

            @Override
            @NonNull
            public Scheduler observeOn() {
                return observeOn;
            }
        };
    }

    @Provides
    @NonNull
    LampGpioView provideLampGpioView(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new LampGpioView(new ListenLampStateUseCase(lampRepository, schedulerSet));
    }

    @Provides
    @NonNull
    ButtonGpioView provideButtonGpioView(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new ButtonGpioView(
                new ListenLampStateUseCase(lampRepository, schedulerSet),
                new SendLampStateUseCase(lampRepository, schedulerSet)
        );
    }

    @Provides
    @NonNull
    PwmGpioView providePwmGpioView(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new PwmGpioView(new ListenLampProgressUseCase(lampRepository, schedulerSet));
    }

    @Provides
    @NonNull
    IDisplayView provideSsd1306DisplayView(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet,
            @NonNull final Application application
    ) {
        return new Ssd1306DisplayView(
                new ListenLampMessageUseCase(lampRepository, schedulerSet),
                application
        );
    }

    @Provides
    @NonNull
    PeripheralService providePeripheralService(
            @NonNull final LampGpioView lampGpioView,
            @NonNull final ButtonGpioView buttonGpioView,
            @NonNull final PwmGpioView pwmGpioView,
            @NonNull final IDisplayView displayView

    ) {
        return new PeripheralService(
                lampGpioView,
                buttonGpioView,
                pwmGpioView,
                displayView
        );
    }
}
