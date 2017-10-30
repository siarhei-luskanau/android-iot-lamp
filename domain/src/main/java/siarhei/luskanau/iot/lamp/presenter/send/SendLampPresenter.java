package siarhei.luskanau.iot.lamp.presenter.send;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.message.SendLampMessageUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.SendLampProgressUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.SendLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.Presenter;

public class SendLampPresenter implements Presenter {

    private final ErrorMessageFactory errorMessageFactory;
    private final SendLampStateUseCase sendLampStateUseCase;
    private final SendLampProgressUseCase sendLampProgressUseCase;
    private final SendLampMessageUseCase sendLampMessageUseCase;
    private SendLampView sendLampStateView;

    public SendLampPresenter(
            final ErrorMessageFactory errorMessageFactory,
            final SendLampStateUseCase sendLampStateUseCase,
            final SendLampProgressUseCase sendLampProgressUseCase,
            final SendLampMessageUseCase sendLampMessageUseCase

    ) {
        this.errorMessageFactory = errorMessageFactory;
        this.sendLampStateUseCase = sendLampStateUseCase;
        this.sendLampProgressUseCase = sendLampProgressUseCase;
        this.sendLampMessageUseCase = sendLampMessageUseCase;
    }

    public void setView(@NonNull final SendLampView view) {
        this.sendLampStateView = view;
    }

    public void sendLampState(final Boolean lampState) {
        sendLampStateUseCase.execute(
                new SendLampObserver<>(),
                SendLampStateUseCase.Params.forLampState(lampState)
        );
    }

    public void sendLampProgress(final double lampProgress) {
        sendLampProgressUseCase.execute(
                new SendLampObserver<>(),
                SendLampProgressUseCase.Params.forLampProgress(lampProgress)
        );
    }

    public void sendLampMessage(final String lampMessage) {
        sendLampMessageUseCase.execute(
                new SendLampObserver<>(),
                SendLampMessageUseCase.Params.forLampMessage(lampMessage)
        );   }

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

    private final class SendLampObserver<T> extends DefaultObserver<T> {

        @Override
        public void onError(final Throwable e) {
            final CharSequence errorMessage = errorMessageFactory.create(e);
            sendLampStateView.showErrorMessage(errorMessage);
        }
    }
}
