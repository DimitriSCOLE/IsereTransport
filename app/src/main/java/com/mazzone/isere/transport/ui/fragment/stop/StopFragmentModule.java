package com.mazzone.isere.transport.ui.fragment.stop;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class StopFragmentModule {

    public final StopFragmentView view;

    public StopFragmentModule(StopFragmentView view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    StopFragmentView provideStopFragmentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    StopPresenter provideStopPresenter(StopPresenterImpl presenter) {
        return presenter;
    }

}
