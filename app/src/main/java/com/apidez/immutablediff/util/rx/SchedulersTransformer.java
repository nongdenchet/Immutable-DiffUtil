package com.apidez.immutablediff.util.rx;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class SchedulersTransformer<T> implements ObservableTransformer<T, T>, CompletableTransformer {
    private final ThreadSchedulers threadSchedulers;

    public SchedulersTransformer(ThreadSchedulers threadSchedulers) {
        this.threadSchedulers = threadSchedulers;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(threadSchedulers.subscribeOn())
                .observeOn(threadSchedulers.observeOn());
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.subscribeOn(threadSchedulers.subscribeOn())
                .observeOn(threadSchedulers.observeOn());
    }
}
