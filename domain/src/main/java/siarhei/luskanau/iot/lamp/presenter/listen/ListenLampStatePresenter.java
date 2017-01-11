package siarhei.luskanau.iot.lamp.presenter.listen;

import android.support.annotation.NonNull;

import siarhei.luskanau.iot.lamp.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.ListenLampStateUseCase;
import siarhei.luskanau.iot.lamp.presenter.Presenter;

public class ListenLampStatePresenter implements Presenter {

    private final ListenLampStateUseCase listenLampStateUseCase;
    private final ErrorMessageFactory errorMessageFactory;
    private ListenLampStateView listenLampStateView;

    public ListenLampStatePresenter(ListenLampStateUseCase listenLampStateUseCase,
                                    ErrorMessageFactory errorMessageFactory) {
        this.listenLampStateUseCase = listenLampStateUseCase;
        this.errorMessageFactory = errorMessageFactory;
    }

    public void setView(@NonNull ListenLampStateView view) {
        this.listenLampStateView = view;
    }

    private void listenLampState() {
        this.listenLampStateUseCase.execute(new ListenLampStateObserver(), null);
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
        public void onError(Throwable e) {
            CharSequence errorMessage = errorMessageFactory.create(e);
            ListenLampStatePresenter.this.listenLampStateView.showErrorMessage(errorMessage);
        }

        @Override
        public void onNext(Boolean lampState) {
            ListenLampStatePresenter.this.listenLampStateView.showLampState(lampState);
        }
    }
}
