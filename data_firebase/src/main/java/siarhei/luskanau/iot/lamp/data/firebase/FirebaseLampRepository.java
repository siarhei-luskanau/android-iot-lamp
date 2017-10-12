package siarhei.luskanau.iot.lamp.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import siarhei.luskanau.iot.lamp.domain.LampRepository;

public class FirebaseLampRepository implements LampRepository {

    private static final String TAG = FirebaseLampRepository.class.getSimpleName();
    private static final String LAMP_KEY = "lamp";

    private final DatabaseReference lampDatabaseReference;

    public FirebaseLampRepository() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        lampDatabaseReference = firebaseDatabase.getReference(LAMP_KEY);
    }

    @Override
    public Observable<Boolean> listenLampState() {
        return Observable.create(emitter -> lampDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!isDisposed(emitter, this)) {
                    final Object valueObject = dataSnapshot.getValue();
                    final boolean value = Boolean.valueOf(String.valueOf(valueObject));
                    emitter.onNext(value);
                }
            }

            @Override
            public void onCancelled(final DatabaseError error) {
                if (!isDisposed(emitter, this)) {
                    emitter.onError(error.toException());
                }
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        }));
    }

    @SuppressWarnings("BooleanParameter")
    @Override
    public Observable<Void> sendLampState(final boolean lampState) {
        return Observable.defer(() -> {
            lampDatabaseReference.setValue(lampState);
            return Observable.empty();
        });
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private boolean isDisposed(final ObservableEmitter emitter, final ValueEventListener valueEventListener) {
        final boolean isDisposed = emitter.isDisposed();
        if (isDisposed) {
            lampDatabaseReference.removeEventListener(valueEventListener);
        }
        return isDisposed;
    }
}
