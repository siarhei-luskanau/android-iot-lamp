package siarhei.luskanau.iot.lamp.remote.control.dagger.modules;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.ISchedulerSet;
import siarhei.luskanau.iot.lamp.domain.interactor.message.ListenLampMessageUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.message.SendLampMessageUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.ListenLampProgressUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.SendLampProgressUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.SendLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampPresenter;

@Module
public class LampModule {

    @Provides
    @NonNull
    ISchedulerSet provideSchedulerSet() {
        return new ISchedulerSet() {

            private final Scheduler subscribeOn = Schedulers.io();
            private final Scheduler observeOn = AndroidSchedulers.mainThread();

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
    ListenLampStateUseCase provideListenLampStateUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new ListenLampStateUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    ListenLampProgressUseCase provideListenLampProgressUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new ListenLampProgressUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    ListenLampMessageUseCase provideListenLampMessageUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new ListenLampMessageUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    SendLampStateUseCase provideSendLampStateUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new SendLampStateUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    SendLampProgressUseCase provideSendLampProgressUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new SendLampProgressUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    SendLampMessageUseCase provideSendLampMessageUseCase(
            @NonNull final LampRepository lampRepository,
            @NonNull final ISchedulerSet schedulerSet
    ) {
        return new SendLampMessageUseCase(
                lampRepository,
                schedulerSet
        );
    }

    @Provides
    @NonNull
    ListenLampPresenter provideListenLampStatePresenter(
            @NonNull final ErrorMessageFactory errorMessageFactory,
            @NonNull final ListenLampStateUseCase listenLampStateUseCase,
            @NonNull final ListenLampProgressUseCase listenLampProgressUseCase,
            @NonNull final ListenLampMessageUseCase listenLampMessageUseCase
    ) {
        return new ListenLampPresenter(
                errorMessageFactory,
                listenLampStateUseCase,
                listenLampProgressUseCase,
                listenLampMessageUseCase
        );
    }

    @Provides
    @NonNull
    SendLampPresenter provideSendLampStatePresenter(
            @NonNull final ErrorMessageFactory errorMessageFactory,
            @NonNull final SendLampStateUseCase sendLampStateUseCase,
            @NonNull final SendLampProgressUseCase sendLampProgressUseCase,
            @NonNull final SendLampMessageUseCase sendLampMessageUseCase
    ) {
        return new SendLampPresenter(
                errorMessageFactory,
                sendLampStateUseCase,
                sendLampProgressUseCase,
                sendLampMessageUseCase
        );
    }
}
