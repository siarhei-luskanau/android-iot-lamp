package siarhei.luskanau.iot.lamp.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.SeekBar;
import android.widget.Toast;

import siarhei.luskanau.iot.lamp.domain.R;
import siarhei.luskanau.iot.lamp.domain.databinding.ViewSimpleLampViewBinding;
import siarhei.luskanau.iot.lamp.presenter.listen.ListenLampView;
import siarhei.luskanau.iot.lamp.presenter.send.SendLampView;
import siarhei.luskanau.iot.lamp.view.base.DataBindingView;

public class SimpleLampView extends DataBindingView<ViewSimpleLampViewBinding> implements ListenLampView, SendLampView {

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

        binding.switchCompat.setOnClickListener(v -> {
            final boolean isChecked = ((Checkable) v).isChecked();
            if (onLampStateChangeListener != null) {
                onLampStateChangeListener.onLampStateChanged(isChecked);
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
        if (binding.switchCompat != null) {
            binding.switchCompat.setChecked(lampState);
        }
    }

    @Override
    public void showLampProgress(final Double lampProgress) {
        if (binding.seekBar != null && lampProgress != null) {
            binding.seekBar.setProgress((int) (lampProgress * binding.seekBar.getMax()));
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
