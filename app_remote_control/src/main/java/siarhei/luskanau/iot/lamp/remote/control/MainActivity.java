package siarhei.luskanau.iot.lamp.remote.control;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.Checkable;
import android.widget.Toast;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampStatePresenter;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampStateView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampStatePresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampStateView;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.LampComponent;

public class MainActivity extends BaseComponentActivity implements ListenLampStateView, SendLampStateView {

    @Inject
    protected ListenLampStatePresenter listenLampStatePresenter;
    @Inject
    protected SendLampStatePresenter sendLampStatePresenter;

    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeInjector();

        listenLampStatePresenter.setView(this);
        sendLampStatePresenter.setView(this);

        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setOnClickListener(v -> {
            final boolean isChecked = ((Checkable) v).isChecked();
            sendLampStatePresenter.sendLampState(isChecked);
        });
    }

    private void initializeInjector() {
        final LampComponent lampComponent = DaggerLampComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        lampComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listenLampStatePresenter.listenLampState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listenLampStatePresenter.destroy();
        sendLampStatePresenter.destroy();
    }

    @Override
    public void showLampState(final Boolean lampState) {
        switchCompat.setChecked(lampState);
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
