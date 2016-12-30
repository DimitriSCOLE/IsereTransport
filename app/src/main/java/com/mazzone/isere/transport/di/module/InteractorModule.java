package com.mazzone.isere.transport.di.module;

import com.mazzone.isere.transport.interactor.TransInfoInteractor;
import com.mazzone.isere.transport.interactor.TransInfoInteractorImpl;
import com.mazzone.isere.transport.ui.model.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class InteractorModule {

    public InteractorModule() {
    }

    @Provides
    @Singleton
    TransInfoInteractor provideTransInfoInteractor(Retrofit retrofit, DaoSession daoSession) {
        return new TransInfoInteractorImpl(retrofit, daoSession);
    }
}
