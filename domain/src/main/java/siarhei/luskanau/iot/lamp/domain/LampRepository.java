package siarhei.luskanau.iot.lamp.domain;

import io.reactivex.Observable;

public interface LampRepository {

    Observable<Boolean> listenLampState();
    @SuppressWarnings("BooleanParameter")
    Observable<Void> sendLampState(final boolean lampState);

    Observable<String> listenLampMessage();
    Observable<Void> sendLampMessage(final String lampMessage);

    Observable<Double> listenLampProgress();
    Observable<Void> sendLampProgress(final double lampProgress);

}
