package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
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
    ListenLampStateUseCase provideListenLampStateUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new ListenLampStateUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    ListenLampProgressUseCase provideListenLampProgressUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new ListenLampProgressUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    ListenLampMessageUseCase provideListenLampMessageUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new ListenLampMessageUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    SendLampStateUseCase provideSendLampStateUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new SendLampStateUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    SendLampProgressUseCase provideSendLampProgressUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new SendLampProgressUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    SendLampMessageUseCase provideSendLampMessageUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        return new SendLampMessageUseCase(
                lampRepository,
                threadExecutor,
                postExecutionThread
        );
    }

    @Provides
    ListenLampPresenter provideListenLampStatePresenter(
            final ErrorMessageFactory errorMessageFactory,
            final ListenLampStateUseCase listenLampStateUseCase,
            final ListenLampProgressUseCase listenLampProgressUseCase,
            final ListenLampMessageUseCase listenLampMessageUseCase
    ) {
        return new ListenLampPresenter(
                errorMessageFactory,
                listenLampStateUseCase,
                listenLampProgressUseCase,
                listenLampMessageUseCase
        );
    }

    @Provides
    SendLampPresenter provideSendLampStatePresenter(
            final ErrorMessageFactory errorMessageFactory,
            final SendLampStateUseCase sendLampStateUseCase,
            final SendLampProgressUseCase sendLampProgressUseCase,
            final SendLampMessageUseCase sendLampMessageUseCase
    ) {
        return new SendLampPresenter(
                errorMessageFactory,
                sendLampStateUseCase,
                sendLampProgressUseCase,
                sendLampMessageUseCase
        );
    }
}
