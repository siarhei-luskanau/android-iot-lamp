package siarhei.luskanau.iot.lamp.domain.interactor.state;

import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;
import siarhei.luskanau.iot.lamp.domain.executor.ISchedulerSet;
import siarhei.luskanau.iot.lamp.domain.interactor.UseCase;

public class ListenLampStateUseCase extends UseCase<Boolean, Void> {

    private final LampRepository lampRepository;

    public ListenLampStateUseCase(
            final LampRepository lampRepository,
            final ISchedulerSet schedulerSet
    ) {
        super(schedulerSet);
        this.lampRepository = lampRepository;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(final Void unused) {
        return this.lampRepository.listenLampState();
    }
}
