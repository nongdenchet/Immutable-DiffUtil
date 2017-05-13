package com.apidez.immutablediff.data.repo;

import com.apidez.immutablediff.data.model.Todo;
import com.apidez.immutablediff.di.scope.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@ApplicationScope
public class TodoRepo {
    private BehaviorSubject<List<Todo>> todos;

    @Inject
    public TodoRepo() {
        List<Todo> newTodos = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            newTodos.add(new Todo(i, "Task " + (i + 1), "0" + (i + 1) + "/12/2010", i % 4 == 0));
        }
        todos = BehaviorSubject.createDefault(newTodos);
    }

    public Observable<List<Todo>> getTodos() {
        return todos.hide();
    }

    private List<Todo> currentTodos() {
        return todos.getValue();
    }

    public Completable update(Todo todo) {
        return Completable.fromAction(() -> {
            int index = indexOf(todo);
            List<Todo> newTodos = new ArrayList<>(currentTodos());
            if (index != -1) {
                newTodos.set(index, todo);
            }
            todos.onNext(newTodos);
        });
    }

    public Completable delete(Todo todo) {
        return Completable.fromAction(() -> {
            List<Todo> newTodos = new ArrayList<>(currentTodos());
            newTodos.remove(todo);
            todos.onNext(newTodos);
        });
    }

    public Completable create(String title, String dueDate) {
        return Completable.fromAction(() -> {
            List<Todo> newTodos = new ArrayList<>(currentTodos());
            newTodos.add(0, new Todo(nextId(), title, dueDate, false));
            todos.onNext(newTodos);
        });
    }

    private long nextId() {
        List<Todo> todos = currentTodos();
        return todos.get(todos.size() - 1).id + 1;
    }

    private int indexOf(Todo todo) {
        return currentTodos().indexOf(todo);
    }
}
