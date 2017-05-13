package com.apidez.immutablediff.di.component;

import com.apidez.immutablediff.di.module.ActivityModule;
import com.apidez.immutablediff.di.module.AppModule;
import com.apidez.immutablediff.di.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {
    ActivityComponent plus(ActivityModule activityModule);
}
