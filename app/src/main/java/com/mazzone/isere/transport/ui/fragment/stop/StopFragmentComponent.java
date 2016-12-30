package com.mazzone.isere.transport.ui.fragment.stop;

import com.mazzone.isere.transport.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {StopFragmentModule.class}
)
public interface StopFragmentComponent {
    void inject(StopFragment fragment);
}
