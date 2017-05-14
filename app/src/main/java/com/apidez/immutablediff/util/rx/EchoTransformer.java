package com.apidez.immutablediff.util.rx;

import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class EchoTransformer<T> implements ObservableTransformer<T, Pair<T, T>> {
    private T lastValue;

    public EchoTransformer(T initValue) {
        lastValue = initValue;
    }

    @Override
    public ObservableSource<Pair<T, T>> apply(Observable<T> upstream) {
        return upstream.map(newValue -> {
            Pair<T, T> result = new Pair<>(lastValue, newValue);
            lastValue = newValue;
            return result;
        });
    }
}
