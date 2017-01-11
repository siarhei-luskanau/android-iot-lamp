package siarhei.luskanau.iot.lamp.domain.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;

public class ListenLampStateUseCase extends UseCase<Boolean, Void> {

    private final LampRepository lampRepository;

    ListenLampStateUseCase(LampRepository lampRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    Observable<Boolean> buildUseCaseObservable(Void unused) {
        return this.lampRepository.listenLampState();
    }
}
