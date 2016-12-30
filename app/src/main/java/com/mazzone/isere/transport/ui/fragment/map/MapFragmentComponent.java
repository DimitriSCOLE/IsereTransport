package com.mazzone.isere.transport.ui.fragment.map;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {MapFragmentModule.class}
)
public interface MapFragmentComponent {
    void inject(MapFragment fragment);
}
