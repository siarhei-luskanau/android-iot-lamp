package siarhei.luskanau.iot.lamp.domain.interactor.progress;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.ISchedulerSet;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class ListenLampProgressUseCase extends UseCase<Double, Void> {

    private final LampRepository lampRepository;

    public ListenLampProgressUseCase(
            final LampRepository lampRepository,
            final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Double> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampProgress();
    }
}
