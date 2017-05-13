package com.apidez.immutablediff.ui;

import android.support.v7.util.DiffUtil;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

public class TodoDiffCalculator {

    @Inject
    TodoDiffCalculator() {
    }

    public Pair<DiffUtil.DiffResult, List<TodoViewModel>> calculate(
            Pair<List<TodoViewModel>, List<TodoViewModel>> data) {
        return new Pair<>(DiffUtil.calculateDiff(new TodoDiffCallback(data)), data.second);
    }

    private class TodoDiffCallback extends DiffUtil.Callback {
        private final List<TodoViewModel> oldList;
        private final List<TodoViewModel> newList;

        public TodoDiffCallback(Pair<List<TodoViewModel>, List<TodoViewModel>> data) {
            checkNotNull(data.first);
            checkNotNull(data.second);
            this.oldList = data.first;
            this.newList = data.second;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
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
}
