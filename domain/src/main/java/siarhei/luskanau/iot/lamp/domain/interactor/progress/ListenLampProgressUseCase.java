package siarhei.luskanau.iot.lamp.domain.interactor.progress;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class ListenLampProgressUseCase extends UseCase<Double, Void> {

    private final LampRepository lampRepository;

    public ListenLampProgressUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Double> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampProgress();
    }
}
