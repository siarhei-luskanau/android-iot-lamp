package siarhei.luskanau.iot.lamp.domain.interactor.progress;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class SendLampProgressUseCase extends UseCase<Void, SendLampProgressUseCase.Params> {

    private final LampRepository lampRepository;

    public SendLampProgressUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.lampRepository.sendLampProgress(params.lampMessage);
    }

    public static final class Params {

        private final double lampMessage;

        private Params(final double lampMessage) {
            this.lampMessage = lampMessage;
        }

        public static Params forLampProgress(final double lampProgress) {
            return new Params(lampProgress);
        }
    }
}
