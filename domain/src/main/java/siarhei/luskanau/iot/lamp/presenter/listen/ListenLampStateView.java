package siarhei.luskanau.iot.lamp.presenter.listen;

public interface ListenLampStateView {

    void showLampState(Boolean lampState);

    void showErrorMessage(CharSequence errorMessage);
}
