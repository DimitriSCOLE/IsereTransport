package com.mazzone.isere.transport.ui.fragment.map;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MapFragmentModule {

    public final MapFragmentView view;

    public MapFragmentModule(MapFragmentView view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MapFragmentView provideMapFragmentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MapPresenter provideMapPresenter(MapPresenterImpl presenter) {
        return presenter;
    }

}
