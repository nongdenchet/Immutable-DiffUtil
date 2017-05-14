package com.apidez.immutablediff.util.diffutil;

import android.support.v7.util.DiffUtil;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;

public class DiffCalculator {

    @Inject
    DiffCalculator() {
    }

    public <T> Pair<DiffUtil.DiffResult, List<T>> calculate(ListDiffCallback<T> callback) {
        return new Pair<>(DiffUtil.calculateDiff(callback), callback.getNewList());
    }
}
