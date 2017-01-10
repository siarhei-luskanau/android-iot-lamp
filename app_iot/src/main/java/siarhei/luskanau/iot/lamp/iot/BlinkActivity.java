package siarhei.luskanau.iot.lamp.iot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class BlinkActivity extends AppCompatActivity {

    private static final String TAG = BlinkActivity.class.getSimpleName();
    private Gpio mLedGpio;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting BlinkActivity");

        try {
            setContentView(R.layout.activity_main);
            switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.d(TAG, "SwitchCompat setOnCheckedChangeListener: " + isChecked);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue(isChecked);
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            String pinName = BoardDefaults.getGPIOForLED();
            mLedGpio = service.openGpio(pinName);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        readFromDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }

    private void readFromDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object valueObject = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + valueObject);
                boolean value = Boolean.valueOf(String.valueOf(valueObject));

                try {
                    if (mLedGpio != null) {
                        mLedGpio.setValue(value);
                        Log.d(TAG, "State set to " + value);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }

                if (switchCompat != null) {
                    switchCompat.setChecked(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
