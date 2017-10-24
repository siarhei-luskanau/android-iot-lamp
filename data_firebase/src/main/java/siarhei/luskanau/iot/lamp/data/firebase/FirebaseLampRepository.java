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

}
