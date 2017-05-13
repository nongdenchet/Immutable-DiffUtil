package com.apidez.immutablediff.ui;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.apidez.immutablediff.R;
import com.apidez.immutablediff.di.scope.ActivityScope;
import com.apidez.immutablediff.di.scope.ForActivity;

import java.util.List;

import javax.inject.Inject;

@ActivityScope
class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private final TodoListPresenter todoListPresenter;
    private final LayoutInflater layoutInflater;

    @Inject
    TodoListAdapter(@ForActivity Context context, TodoListPresenter todoListPresenter) {
        this.todoListPresenter = todoListPresenter;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_todo, parent, false));
    }

    private List<TodoViewModel> getTodos() {
        return todoListPresenter.getTodos();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getTodos().get(position));
    }

    @Override
    public int getItemCount() {
        return getTodos().size();
    }

    void applyDiff(DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(this);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbCompleted;
        final TextView tvRemove;
        final TextView tvDueDate;
        final TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            cbCompleted = (CheckBox) itemView.findViewById(R.id.cbCompleted);
            tvRemove = (TextView) itemView.findViewById(R.id.tvRemove);
            tvDueDate = (TextView) itemView.findViewById(R.id.tvDueDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            setUpListeners();
        }

        private void setUpListeners() {
            cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) ->
                    todoListPresenter.setCompleted(getAdapterPosition(), isChecked));
            tvRemove.setOnClickListener(v ->
                    todoListPresenter.deleteTodo(getAdapterPosition()));
        }

        void bind(TodoViewModel todo) {
            tvTitle.setText(todo.title);
            tvDueDate.setText(todo.dueDate);
            tvRemove.setVisibility(todo.removeVisibility());
            cbCompleted.setChecked(todo.completed);
        }
    }
}
