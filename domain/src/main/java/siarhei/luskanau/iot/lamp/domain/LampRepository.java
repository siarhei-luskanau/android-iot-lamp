package siarhei.luskanau.iot.lamp.domain;

import io.reactivex.Observable;

public interface LampRepository {

    Observable<Boolean> listenLampState();

    Observable<Void> sendLampState(final boolean lampState);
}
