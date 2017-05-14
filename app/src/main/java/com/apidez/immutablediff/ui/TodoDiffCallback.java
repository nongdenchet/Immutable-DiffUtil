package com.apidez.immutablediff.ui;

import android.util.Pair;

import com.apidez.immutablediff.util.diffutil.ListDiffCallback;

import java.util.List;

public class TodoDiffCallback extends ListDiffCallback<TodoViewModel> {

    public TodoDiffCallback(Pair<List<TodoViewModel>, List<TodoViewModel>> data) {
        super(data);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).title.equals(newList.get(newItemPosition).title)
                && oldList.get(oldItemPosition).dueDate.equals(newList.get(newItemPosition).dueDate)
                && oldList.get(oldItemPosition).completed == newList.get(newItemPosition).completed;
    }
}
