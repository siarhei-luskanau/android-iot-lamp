package siarhei.luskanau.iot.lamp.remote.control;

import android.os.Bundle;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampPresenter;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.LampComponent;
import siarhei.luskanau.iot.lamp.view.SimpleLampView;

public class MainActivity extends BaseComponentActivity {

    @Inject
    protected ListenLampPresenter listenLampPresenter;
    @Inject
    protected SendLampPresenter sendLampPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeInjector();

        final SimpleLampView simpleLampView = findViewById(R.id.simpleLampView);

        listenLampPresenter.setView(simpleLampView);
        sendLampPresenter.setView(simpleLampView);

        simpleLampView.setOnLampStateChangeListener(lampState -> sendLampPresenter.sendLampState(lampState));
        simpleLampView.setOnLampProgressChangeListener(lampProgress -> sendLampPresenter.sendLampProgress(lampProgress));
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

        listenLampPresenter.listenLampState();
        listenLampPresenter.listenLampProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();

        listenLampPresenter.destroy();
        sendLampPresenter.destroy();
    }
}
