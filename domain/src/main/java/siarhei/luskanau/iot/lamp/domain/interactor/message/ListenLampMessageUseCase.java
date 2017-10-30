package siarhei.luskanau.iot.lamp.domain.interactor.message;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.lamp.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class ListenLampMessageUseCase extends UseCase<String, Void> {

    private final LampRepository lampRepository;

    public ListenLampMessageUseCase(
            final LampRepository lampRepository,
            final ThreadExecutor threadExecutor,
            final PostExecutionThread postExecutionThread
    ) {
        super(threadExecutor, postExecutionThread);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<String> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampMessage();
    }
}
