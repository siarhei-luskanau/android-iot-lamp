package siarhei.luskanau.iot.lamp.presenter.listen;

public interface ListenLampView {

    void showLampState(Boolean lampState);

    void showLampProgress(Double lampProgress);

    void showErrorMessage(CharSequence errorMessage);
}
