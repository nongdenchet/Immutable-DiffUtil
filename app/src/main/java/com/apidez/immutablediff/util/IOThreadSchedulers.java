package com.apidez.immutablediff.util;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IOThreadSchedulers implements ThreadSchedulers {

    @Override
    public Scheduler subscribeOn() {
        return Schedulers.io();
    }

    @Override
    public Scheduler observeOn() {
        return AndroidSchedulers.mainThread();
    }
}
