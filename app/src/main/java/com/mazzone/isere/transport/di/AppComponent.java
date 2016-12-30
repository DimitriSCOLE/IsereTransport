package com.mazzone.isere.transport.di;

import com.mazzone.isere.transport.di.module.AppModule;
import com.mazzone.isere.transport.di.module.DatabaseModule;
import com.mazzone.isere.transport.di.module.InteractorModule;
import com.mazzone.isere.transport.di.module.NetModule;
import com.mazzone.isere.transport.ui.activity.MainActivityComponent;
import com.mazzone.isere.transport.ui.activity.MainActivityModule;
import com.mazzone.isere.transport.ui.fragment.map.MapFragmentComponent;
import com.mazzone.isere.transport.ui.fragment.map.MapFragmentModule;
import com.mazzone.isere.transport.ui.fragment.stop.StopFragmentComponent;
import com.mazzone.isere.transport.ui.fragment.stop.StopFragmentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetModule.class,
        DatabaseModule.class,
        InteractorModule.class})
public interface AppComponent {

    MainActivityComponent plus(MainActivityModule module);

    MapFragmentComponent plus(MapFragmentModule module);

    StopFragmentComponent plus(StopFragmentModule module);
}