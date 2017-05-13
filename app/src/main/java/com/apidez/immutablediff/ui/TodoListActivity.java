package com.apidez.immutablediff.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.apidez.immutablediff.R;
import com.apidez.immutablediff.TodoApplication;
import com.apidez.immutablediff.di.module.ActivityModule;

import javax.inject.Inject;

@SuppressWarnings("ALL")
public class TodoListActivity extends AppCompatActivity implements TodoListScreen,
        NewTodoFragment.OnSubmitListener {
    private RecyclerView rvTodos;

    @Inject
    TodoListPresenter todoListPresenter;
    @Inject
    TodoListAdapter todoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        ((TodoApplication) getApplication()).component()
                .plus(new ActivityModule(this))
                .inject(this);
        todoListPresenter.onCreate();
    }


    @Override
    public void initialize() {
        rvTodos = (RecyclerView) findViewById(R.id.rvTodos);
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setAdapter(todoListAdapter);
    }

    @Override
    protected void onDestroy() {
        todoListPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            showNewDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNewDialog() {
        NewTodoFragment newTodoFragment = NewTodoFragment.newInstance();
        newTodoFragment.show(getSupportFragmentManager(), NewTodoFragment.class.getSimpleName());
    }

    @Override
    public void onSubmit(String title, String dueDate) {
        todoListPresenter.create(title, dueDate);
    }

    @Override
    public void applyDiff(DiffUtil.DiffResult diffResult) {
        todoListAdapter.applyDiff(diffResult);
    }

    @Override
    public void scrollToTop() {
        rvTodos.smoothScrollToPosition(0);
    }
}
