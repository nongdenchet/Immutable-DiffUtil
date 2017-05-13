package com.apidez.immutablediff.ui;

import com.apidez.immutablediff.data.model.Todo;
import com.apidez.immutablediff.data.repo.TodoRepo;
import com.apidez.immutablediff.di.scope.ActivityScope;
import com.apidez.immutablediff.util.EchoTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScope
class TodoListPresenter {
    private final TodoListScreen todoListScreen;
    private final TodoRepo todoRepo;
    private final TodoDiffCalculator todoDiffCalculator;

    private List<TodoViewModel> todos = new ArrayList<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TodoListPresenter(TodoListScreen todoListScreen,
                             TodoRepo todoRepo,
                             TodoDiffCalculator todoDiffCalculator) {
        this.todoListScreen = todoListScreen;
        this.todoRepo = todoRepo;
        this.todoDiffCalculator = todoDiffCalculator;
    }

    List<TodoViewModel> getTodos() {
        return todos;
    }

    void onCreate() {
        todoListScreen.initialize();
        disposables.add(todoRepo.getTodos()
                .map(this::toViewModels)
                .compose(new EchoTransformer<>(todos))
                .map(todoDiffCalculator::calculate)
                .subscribe(result -> {
                    todos = result.second;
                    todoListScreen.applyDiff(result.first);
                }));
    }

    void setCompleted(int position, boolean completed) {
        disposables.add(Observable.just(todos.get(position))
                .filter(viewModel -> viewModel.completed != completed)
                .map(viewModel -> viewModel.setCompleted(completed))
                .map(TodoViewModel::toModel)
                .flatMapCompletable(todoRepo::update)
                .subscribe());
    }

    void create(String title, String dueDate) {
        disposables.add(todoRepo.create(title, dueDate)
                .subscribe(todoListScreen::scrollToTop));
    }

    void deleteTodo(int position) {
        disposables.add(Observable.just(todos.get(position))
                .map(TodoViewModel::toModel)
                .flatMapCompletable(todoRepo::delete)
                .subscribe());
    }

    void onDestroy() {
        disposables.dispose();
    }

    @SuppressWarnings("Convert2streamapi")
    private List<TodoViewModel> toViewModels(List<Todo> todos) {
        List<TodoViewModel> viewModels = new ArrayList<>(todos.size());
        for (Todo todo : todos) {
            viewModels.add(new TodoViewModel(todo));
        }
        return viewModels;
    }
}
