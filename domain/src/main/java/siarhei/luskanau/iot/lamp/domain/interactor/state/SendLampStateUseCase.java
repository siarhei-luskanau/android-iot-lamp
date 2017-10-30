package siarhei.luskanau.iot.lamp.domain.interactor.state;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class SendLampStateUseCase extends UseCase<Void, SendLampStateUseCase.Params> {

    private final LampRepository lampRepository;

    public SendLampStateUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Void> buildUseCaseObservable(final Params params) {
        return this.lampRepository.sendLampState(params.lampState);
    }

    public static final class Params {

        private final Boolean lampState;

        private Params(final Boolean lampState) {
            this.lampState = lampState;
        }

        public static Params forLampState(final Boolean lampState) {
            return new Params(lampState);
        }
    }
}
