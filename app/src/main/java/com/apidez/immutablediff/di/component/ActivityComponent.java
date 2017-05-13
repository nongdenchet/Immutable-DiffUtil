package com.apidez.immutablediff.di.component;

import com.apidez.immutablediff.di.module.ActivityModule;
import com.apidez.immutablediff.di.scope.ActivityScope;
import com.apidez.immutablediff.ui.TodoListActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(TodoListActivity todoListActivity);
}
