package com.apidez.immutablediff.di.module;

import android.app.Activity;
import android.content.Context;

import com.apidez.immutablediff.di.scope.ActivityScope;
import com.apidez.immutablediff.di.scope.ForActivity;
import com.apidez.immutablediff.ui.TodoListScreen;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivity
    @ActivityScope
    public Context provideContext() {
        return activity;
    }

    @Provides
    @ActivityScope
    public TodoListScreen provideTodoListScreen() {
        return (TodoListScreen) activity;
    }
}
