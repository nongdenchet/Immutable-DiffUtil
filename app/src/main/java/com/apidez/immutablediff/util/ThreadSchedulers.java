package com.apidez.immutablediff.util;

import io.reactivex.Scheduler;

public interface ThreadSchedulers {
    Scheduler subscribeOn();

    Scheduler observeOn();
}
