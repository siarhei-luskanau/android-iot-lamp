package siarhei.luskanau.iot.lamp.domain.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;

public class SendLampStateUseCase extends UseCase<Void, Boolean> {

    private final LampRepository lampRepository;

    SendLampStateUseCase(LampRepository lampRepository, ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Boolean lampState) {
        return this.lampRepository.sendLampState(lampState);
    }
}
