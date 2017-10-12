package siarhei.luskanau.iot.lamp.remote.control.dagger.modules;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.SendLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampStatePresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampStatePresenter;

@Module
public class LampModule {

    @Provides
    ListenLampStatePresenter provideListenLampStatePresenter(final LampRepository lampRepository,
                                                             final ThreadExecutor threadExecutor,
                                                             final PostExecutionThread postExecutionThread,
                                                             final ErrorMessageFactory errorMessageFactory) {
        final ListenLampStateUseCase listenLampStateUseCase = new ListenLampStateUseCase(lampRepository,
                threadExecutor, postExecutionThread);
        return new ListenLampStatePresenter(listenLampStateUseCase, errorMessageFactory);
    }

    @Provides
    SendLampStatePresenter provideSendLampStatePresenter(final LampRepository lampRepository,
                                                         final ThreadExecutor threadExecutor,
                                                         final PostExecutionThread postExecutionThread,
                                                         final ErrorMessageFactory errorMessageFactory) {
        final SendLampStateUseCase sendLampStateUseCase = new SendLampStateUseCase(lampRepository,
                threadExecutor, postExecutionThread);
        return new SendLampStatePresenter(sendLampStateUseCase, errorMessageFactory);
    }
}
