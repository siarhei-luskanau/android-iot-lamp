package siarhei.luskanau.iot.lamp.remote.control;

import android.support.v7.app.AppCompatActivity;

import siarhei.luskanau.iot.lamp.remote.control.dagger.component.ApplicationComponent;

public abstract class BaseComponentActivity extends AppCompatActivity {

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }
}
