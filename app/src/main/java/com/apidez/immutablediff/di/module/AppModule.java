package com.apidez.immutablediff.di.module;

import android.content.Context;

import com.apidez.immutablediff.di.scope.ApplicationScope;
import com.apidez.immutablediff.util.IOThreadSchedulers;
import com.apidez.immutablediff.util.ThreadSchedulers;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    public static final String IO_THREAD = "IO_THREAD";
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    Context provideContext() {
        return context;
    }

    @Provides
    @ApplicationScope
    @Named(value = IO_THREAD)
    ThreadSchedulers provideIOThreadSchedulers() {
        return new IOThreadSchedulers();
    }
}
