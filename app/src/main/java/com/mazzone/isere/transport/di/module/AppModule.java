package com.mazzone.isere.transport.di.module;

import android.app.Application;
import android.content.Context;

import com.mazzone.isere.transport.TransportApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    TransportApplication mApplication;

    public AppModule(TransportApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

}
