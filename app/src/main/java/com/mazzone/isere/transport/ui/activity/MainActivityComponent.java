package com.mazzone.isere.transport.ui.activity;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {MainActivityModule.class}
)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
