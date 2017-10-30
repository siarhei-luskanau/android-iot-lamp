package siarhei.luskanau.iot.lamp.data.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import siarhei.luskanau.iot.lamp.domain.LampRepository;

public class FirebaseLampRepository implements LampRepository {

    private static final String LAMP_APP_KEY = "lamp_app";
    private static final String LAMP_KEY = "lamp";
    private static final String MESSAGE_KEY = "message";
    private static final String PROGRESS_KEY = "progress";

    private static DatabaseReference getAppDatabase() {
        return FirebaseDatabase.getInstance()
                .getReference(LAMP_APP_KEY);
    }

    @Override
    public Observable<Boolean> listenLampState() {
        return RxFirebaseDatabase
                .observeValueEvent(
                        getAppDatabase().child(LAMP_KEY),
                        Boolean.class
                )
                .toObservable();
    }

    @SuppressWarnings("BooleanParameter")
    @Override
    public Observable<Void> sendLampState(final boolean lampState) {
        return Completable.create(emitter ->
                getAppDatabase()
                        .child(LAMP_KEY)
                        .setValue(lampState))
                .toObservable();
    }

    @Override
    public Observable<String> listenLampMessage() {
        return RxFirebaseDatabase
                .observeValueEvent(
                        getAppDatabase().child(MESSAGE_KEY),
                        String.class
                )
                .toObservable();
    }

    @Override
    public Observable<Void> sendLampMessage(final String lampMessage) {
        return Completable.create(emitter ->
                getAppDatabase()
                        .child(MESSAGE_KEY)
                        .setValue(lampMessage))
                .toObservable();
    }

    @Override
    public Observable<Double> listenLampProgress() {
        return RxFirebaseDatabase
                .observeValueEvent(
                        getAppDatabase().child(PROGRESS_KEY),
                        Double.class
                )
                .toObservable();
    }

    @Override
    public Observable<Void> sendLampProgress(final double lampProgress) {
        return Completable.create(emitter ->
                getAppDatabase()
                        .child(PROGRESS_KEY)
                        .setValue(lampProgress))
                .toObservable();
    }
}
