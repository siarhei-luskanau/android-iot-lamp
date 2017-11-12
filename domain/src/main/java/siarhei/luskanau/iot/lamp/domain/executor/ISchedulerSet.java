package siarhei.luskanau.iot.lamp.domain.executor;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

public interface ISchedulerSet {

    @NonNull
    Scheduler subscribeOn();

    @NonNull
    Scheduler observeOn();
}
