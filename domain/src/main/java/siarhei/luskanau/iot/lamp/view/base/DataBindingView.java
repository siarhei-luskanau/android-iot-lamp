package siarhei.luskanau.iot.lamp.view.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public abstract class DataBindingView<T extends ViewDataBinding> extends FrameLayout {

    protected T binding;

    public DataBindingView(final Context context) {
        super(context);
        init(context, null);
    }

    public DataBindingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DataBindingView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DataBindingView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(final Context context, final AttributeSet attrs) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getViewLayout(), this, true);

        applyAttributes(context, attrs);

        onViewCreated(context);
    }

    protected void applyAttributes(@SuppressWarnings("unused") final Context context, @SuppressWarnings("unused") final AttributeSet attrs) {
    }

    protected void onViewCreated(@SuppressWarnings("unused") final Context context) {
    }

    @LayoutRes
    protected abstract int getViewLayout();
}
