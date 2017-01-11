package siarhei.luskanau.iot.lamp.presenter.send;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.SendLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.Presenter;

public class SendLampStatePresenter implements Presenter {

    private final SendLampStateUseCase sendLampStateUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private SendLampStateView sendLampStateView;

    public SendLampStatePresenter(SendLampStateUseCase sendLampStateUseCase,
                                  ErrorMessageFactory errorMessageFactory) {
        this.sendLampStateUseCase = sendLampStateUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull SendLampStateView view) {
        this.sendLampStateView = view;
    }

    public void sendLampState(Boolean lampState) {
        this.sendLampStateUseCase.execute(new SendLampStateObserver(),
                SendLampStateUseCase.Params.forLampState(lampState));
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    public void destroy() {
        this.sendLampStateUseCase.dispose();
        this.sendLampStateView = null;
    }

    private final class SendLampStateObserver extends DefaultObserver<Void> {
        @Override
        public void onError(Throwable e) {
            CharSequence errorMessage = errorMessageFactory.create(e);
            SendLampStatePresenter.this.sendLampStateView.showErrorMessage(errorMessage);
        }
    }
}
