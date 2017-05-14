package com.apidez.immutablediff.util.diffutil;

import android.support.v7.util.DiffUtil;
import android.util.Pair;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;

public abstract class ListDiffCallback<T> extends DiffUtil.Callback {
    protected final List<T> oldList;
    protected final List<T> newList;

    public ListDiffCallback(Pair<List<T>, List<T>> data) {
        checkNotNull(data.first);
        checkNotNull(data.second);
        this.oldList = data.first;
        this.newList = data.second;
    }

    public List<T> getOldList() {
        return oldList;
    }

    public List<T> getNewList() {
        return newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }
}
