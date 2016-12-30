package com.mazzone.isere.transport.ui.activity;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    public final MainActivityView view;

    public MainActivityModule(MainActivityView view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainActivityView provideMainActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MainPresenter provideMainPresenter(MainPresenterImpl presenter) {
        return presenter;
    }

}
