package siarhei.luskanau.iot.lamp.remote.control;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.Checkable;
import android.widget.SeekBar;
import android.widget.Toast;

import javax.inject.Inject;

import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampPresenter;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampView;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.DaggerLampComponent;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.LampComponent;

public class MainActivity extends BaseComponentActivity implements ListenLampView, SendLampView {

    @Inject
    protected ListenLampPresenter listenLampStatePresenter;
    @Inject
    protected SendLampPresenter sendLampPresenter;

    private SwitchCompat switchCompat;
    private SeekBar seekBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeInjector();

        listenLampStatePresenter.setView(this);
        sendLampPresenter.setView(this);

        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setOnClickListener(v -> {
            final boolean isChecked = ((Checkable) v).isChecked();
            sendLampPresenter.sendLampState(isChecked);
        });

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                if (fromUser) {
                    sendLampPresenter.sendLampProgress((double) progress / seekBar.getMax());
                }
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
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
        listenLampStatePresenter.listenLampProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listenLampStatePresenter.destroy();
        sendLampPresenter.destroy();
    }

    @Override
    public void showLampState(final Boolean lampState) {
        switchCompat.setChecked(lampState);
    }

    @Override
    public void showLampProgress(final Double lampProgress) {
        if (seekBar != null && lampProgress != null) {
            seekBar.setProgress((int) (lampProgress * seekBar.getMax()));
        }
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
