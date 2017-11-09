package siarhei.luskanau.iot.lamp.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.SeekBar;
import android.widget.Toast;

import butterknife.BindView;
import siarhei.luskanau.iot.lamp.domain.R;
import siarhei.luskanau.iot.lamp.domain.R2;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampView;
import siarhei.luskanau.iot.lamp.view.base.InflateFrameLayout;

public class SimpleLampView extends InflateFrameLayout implements ListenLampView, SendLampView {

    @BindView(R2.id.switchCompat)
    protected SwitchCompat switchCompat;
    @BindView(R2.id.seekBar)
    protected SeekBar seekBar;

    private OnLampStateChangeListener onLampStateChangeListener;
    private OnLampProgressChangeListener onLampProgressChangeListener;

    public SimpleLampView(final Context context) {
        super(context);
    }

    public SimpleLampView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleLampView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public SimpleLampView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onViewCreated(final Context context) {
        super.onViewCreated(context);

        switchCompat.setOnClickListener(v -> {
            final boolean isChecked = ((Checkable) v).isChecked();
            if (onLampStateChangeListener != null) {
                onLampStateChangeListener.onLampStateChanged(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                if (fromUser) {
                    if (onLampProgressChangeListener != null) {
                        onLampProgressChangeListener.onLampProgressChanged((double) progress / seekBar.getMax());
                    }
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

    public void setOnLampStateChangeListener(@Nullable final OnLampStateChangeListener onLampStateChangeListener) {
        this.onLampStateChangeListener = onLampStateChangeListener;
    }

    public void setOnLampProgressChangeListener(@Nullable final OnLampProgressChangeListener onLampProgressChangeListener) {
        this.onLampProgressChangeListener = onLampProgressChangeListener;
    }

    @Override
    @LayoutRes
    protected int getViewLayout() {
        return R.layout.view_simple_lamp_view;
    }

    @Override
    public void showLampState(final Boolean lampState) {
        if (switchCompat != null) {
            switchCompat.setChecked(lampState);
        }
    }

    @Override
    public void showLampProgress(final Double lampProgress) {
        if (seekBar != null && lampProgress != null) {
            seekBar.setProgress((int) (lampProgress * seekBar.getMax()));
        }
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public interface OnLampStateChangeListener {

        @SuppressWarnings("BooleanParameter")
        void onLampStateChanged(boolean lampState);
    }

    public interface OnLampProgressChangeListener {

        void onLampProgressChanged(Double lampProgress);
    }
}
