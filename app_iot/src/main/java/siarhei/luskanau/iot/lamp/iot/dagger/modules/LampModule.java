package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.SendLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampPresenter;

@Module
public class LampModule {

    @Provides
    ListenLampPresenter provideListenLampStatePresenter(final LampRepository lampRepository,
                                                        final ThreadExecutor threadExecutor,
                                                        final PostExecutionThread postExecutionThread,
                                                        final ErrorMessageFactory errorMessageFactory) {
        final ListenLampStateUseCase listenLampStateUseCase = new ListenLampStateUseCase(lampRepository,
                threadExecutor, postExecutionThread);
        return new ListenLampPresenter(listenLampStateUseCase, errorMessageFactory);
    }

    @Provides
    SendLampPresenter provideSendLampStatePresenter(final LampRepository lampRepository,
                                                    final ThreadExecutor threadExecutor,
                                                    final PostExecutionThread postExecutionThread,
                                                    final ErrorMessageFactory errorMessageFactory) {
        final SendLampStateUseCase sendLampStateUseCase = new SendLampStateUseCase(lampRepository,
                threadExecutor, postExecutionThread);
        return new SendLampPresenter(sendLampStateUseCase, errorMessageFactory);
    }
}
