package com.apidez.immutablediff.util.rx;

import io.reactivex.Scheduler;

public interface ThreadSchedulers {
    Scheduler subscribeOn();

    Scheduler observeOn();
}
