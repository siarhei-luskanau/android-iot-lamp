package siarhei.luskanau.iot.lamp.domain.interactor.message;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class SendLampMessageUseCase extends UseCase<Void, SendLampMessageUseCase.Params> {

    private final LampRepository lampRepository;

    public SendLampMessageUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.lampRepository.sendLampMessage(params.lampMessage);
    }

    public static final class Params {

        private final String lampMessage;

        private Params(final String lampMessage) {
            this.lampMessage = lampMessage;
        }

        public static Params forLampMessage(final String lampMessage) {
            return new Params(lampMessage);
        }
    }
}
