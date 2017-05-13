package com.apidez.immutablediff.ui;

import android.support.v7.util.DiffUtil;

public interface TodoListScreen {
    void applyDiff(DiffUtil.DiffResult diffResult);

    void scrollToTop();

    void initialize();
}
