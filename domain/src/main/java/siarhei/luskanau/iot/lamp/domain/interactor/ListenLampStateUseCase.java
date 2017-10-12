package siarhei.luskanau.iot.lamp.domain.interactor;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;

public class ListenLampStateUseCase extends UseCase<Boolean, Void> {

    private final LampRepository lampRepository;

    public ListenLampStateUseCase(final LampRepository lampRepository,
                                  final ThreadExecutor threadExecutor,
                                  final PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    Observable<Boolean> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampState();
    }
}
