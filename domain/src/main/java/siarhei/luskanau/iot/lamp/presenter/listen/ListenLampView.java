package siarhei.luskanau.iot.lamp.presenter.listen;

public interface ListenLampView {

    void showLampState(Boolean lampState);

    void showErrorMessage(CharSequence errorMessage);
}
