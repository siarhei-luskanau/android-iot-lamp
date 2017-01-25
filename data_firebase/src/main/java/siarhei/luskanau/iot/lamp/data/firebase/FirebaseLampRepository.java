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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        lampDatabaseReference = firebaseDatabase.getReference(LAMP_KEY);
    }

    @Override
    public Observable<Boolean> listenLampState() {
        return Observable.create(emitter -> {
            lampDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!isDisposed(emitter, this)) {
                        Object valueObject = dataSnapshot.getValue();
                        boolean value = Boolean.valueOf(String.valueOf(valueObject));
                        emitter.onNext(value);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    if (!isDisposed(emitter, this)) {
                        emitter.onError(error.toException());
                    }
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        });
    }

    @Override
    public Observable<Void> sendLampState(boolean lampState) {
        return Observable.defer(() -> {
            lampDatabaseReference.setValue(lampState);
            return Observable.empty();
        });
    }

    private boolean isDisposed(ObservableEmitter emitter, ValueEventListener valueEventListener) {
        boolean isDisposed = emitter.isDisposed();
        if (isDisposed) {
            lampDatabaseReference.removeEventListener(valueEventListener);
        }
        return isDisposed;
    }
}
