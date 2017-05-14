package com.apidez.immutablediff.ui;

import com.apidez.immutablediff.data.model.Todo;
import com.apidez.immutablediff.data.repo.TodoRepo;
import com.apidez.immutablediff.di.scope.ActivityScope;
import com.apidez.immutablediff.util.diffutil.DiffCalculator;
import com.apidez.immutablediff.util.rx.EchoTransformer;
import com.apidez.immutablediff.util.rx.SchedulersTransformer;
import com.apidez.immutablediff.util.rx.ThreadSchedulers;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static com.apidez.immutablediff.di.module.AppModule.IO_THREAD;

@ActivityScope
class TodoListPresenter {
    private final TodoListScreen todoListScreen;
    private final TodoRepo todoRepo;
    private final DiffCalculator diffCalculator;
    private final ThreadSchedulers threadSchedulers;

    private List<TodoViewModel> todos = Collections.emptyList();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TodoListPresenter(TodoListScreen todoListScreen,
                             TodoRepo todoRepo,
                             DiffCalculator diffCalculator,
                             @Named(IO_THREAD) ThreadSchedulers threadSchedulers) {
        this.todoListScreen = todoListScreen;
        this.todoRepo = todoRepo;
        this.diffCalculator = diffCalculator;
        this.threadSchedulers = threadSchedulers;
    }

    List<TodoViewModel> getTodos() {
        return todos;
    }

    void onCreate() {
        todoListScreen.initialize();
        disposables.add(todoRepo.getTodos()
                .map(this::toViewModels)
                .compose(new EchoTransformer<>(todos))
                .map(TodoDiffCallback::new)
                .map(diffCalculator::calculate)
                .compose(new SchedulersTransformer<>(threadSchedulers))
                .subscribe(result -> {
                    todos = result.second;
                    todoListScreen.applyDiff(result.first);
                }));
    }

    void setCompleted(int position, boolean completed) {
        if (!isValidPosition(position)) {
            return;
        }
        disposables.add(Observable.just(todos.get(position))
                .filter(viewModel -> viewModel.completed != completed)
                .map(viewModel -> viewModel.setCompleted(completed))
                .map(TodoViewModel::toModel)
                .flatMapCompletable(todoRepo::update)
                .compose(new SchedulersTransformer<>(threadSchedulers))
                .subscribe());
    }

    void create(String title, String dueDate) {
        disposables.add(todoRepo.create(title, dueDate)
                .compose(new SchedulersTransformer<>(threadSchedulers))
                .subscribe(todoListScreen::scrollToTop));
    }

    void deleteTodo(int position) {
        if (!isValidPosition(position)) {
            return;
        }
        disposables.add(Observable.just(todos.get(position))
                .map(TodoViewModel::toModel)
                .flatMapCompletable(todoRepo::delete)
                .compose(new SchedulersTransformer<>(threadSchedulers))
                .subscribe());
    }

    void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private boolean isValidPosition(int position) {
        return position >= 0 && position < todos.size();
    }

    @SuppressWarnings("Convert2streamapi")
    private List<TodoViewModel> toViewModels(List<Todo> todos) {
        return Observable.fromIterable(todos)
                .map(TodoViewModel::new)
                .toList()
                .blockingGet();
    }
}
