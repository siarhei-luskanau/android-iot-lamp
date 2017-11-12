package siarhei.luskanau.iot.lamp.domain.interactor.message;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.ISchedulerSet;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class ListenLampMessageUseCase extends UseCase<String, Void> {

    private final LampRepository lampRepository;

    public ListenLampMessageUseCase(
            final LampRepository lampRepository,
            final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<String> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampMessage();
    }
}
