package siarhei.luskanau.iot.lamp.domain.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;

public class SendLampStateUseCase extends UseCase<Void, SendLampStateUseCase.Params> {

    private final LampRepository lampRepository;

    SendLampStateUseCase(LampRepository lampRepository, ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Params params) {
        return this.lampRepository.sendLampState(params.lampState);
    }

    public static final class Params {
        private final Boolean lampState;

        private Params(Boolean lampState) {
            this.lampState = lampState;
        }

        public static Params forLampState(Boolean lampState) {
            return new Params(lampState);
        }
    }
}
