package com.mazzone.isere.transport.di.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mazzone.isere.transport.ui.model.DaoMaster;
import com.mazzone.isere.transport.ui.model.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    public DatabaseModule() {
    }

    @Provides
    @Singleton
    SQLiteDatabase provideWritableDatabase(Context context) {
        return new DaoMaster.DevOpenHelper(context, "map-item-db", null).getWritableDatabase();
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(SQLiteDatabase sqLiteDatabase) {
        return new DaoMaster(sqLiteDatabase);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

}
