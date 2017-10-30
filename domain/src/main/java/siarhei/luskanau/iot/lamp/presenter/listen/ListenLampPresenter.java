package siarhei.luskanau.iot.lamp.presenter.listen;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.message.ListenLampMessageUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.progress.ListenLampProgressUseCase;
import siarhei.luskanau.iot.lamp.domain.interactor.state.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.Presenter;

public class ListenLampPresenter implements Presenter {

    private final ErrorMessageFactory errorMessageFactory;
    private final ListenLampStateUseCase listenLampStateUseCase;
    private final ListenLampProgressUseCase listenLampProgressUseCase;
    private final ListenLampMessageUseCase listenLampMessageUseCase;
    private ListenLampView listenLampStateView;

    public ListenLampPresenter(
            final ErrorMessageFactory errorMessageFactory,
            final ListenLampStateUseCase listenLampStateUseCase,
            final ListenLampProgressUseCase listenLampProgressUseCase,
            final ListenLampMessageUseCase listenLampMessageUseCase
    ) {
        this.errorMessageFactory = errorMessageFactory;
        this.listenLampStateUseCase = listenLampStateUseCase;
        this.listenLampProgressUseCase = listenLampProgressUseCase;
        this.listenLampMessageUseCase = listenLampMessageUseCase;
    }

    public void setView(@NonNull final ListenLampView view) {
        this.listenLampStateView = view;
    }

    public void listenLampState() {
        this.listenLampStateUseCase.execute(new ListenLampStateObserver(), null);
    }

    public void listenLampProgress() {
        this.listenLampProgressUseCase.execute(new ListenLampProgressObserver(), null);
    }

    public void listenLampMessage() {
        this.listenLampMessageUseCase.execute(new DefaultObserver<>(), null);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        this.listenLampStateUseCase.dispose();
        this.listenLampStateView = null;
    }

    private final class ListenLampStateObserver extends DefaultObserver<Boolean> {

        @Override
        public void onError(final Throwable e) {
            final CharSequence errorMessage = errorMessageFactory.create(e);
            ListenLampPresenter.this.listenLampStateView.showErrorMessage(errorMessage);
        }

        @Override
        public void onNext(final Boolean lampState) {
            ListenLampPresenter.this.listenLampStateView.showLampState(lampState);
        }
    }

    private final class ListenLampProgressObserver extends DefaultObserver<Double> {

        @Override
        public void onError(final Throwable e) {
            final CharSequence errorMessage = errorMessageFactory.create(e);
            ListenLampPresenter.this.listenLampStateView.showErrorMessage(errorMessage);
        }

        @Override
        public void onNext(final Double lampProgress) {
            ListenLampPresenter.this.listenLampStateView.showLampProgress(lampProgress);
        }
    }
}
