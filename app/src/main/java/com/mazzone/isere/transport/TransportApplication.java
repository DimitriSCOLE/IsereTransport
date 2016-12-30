package com.mazzone.isere.transport;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.mazzone.isere.transport.di.AppComponent;
import com.mazzone.isere.transport.di.DaggerAppComponent;
import com.mazzone.isere.transport.di.module.AppModule;
import com.mazzone.isere.transport.di.module.DatabaseModule;
import com.mazzone.isere.transport.di.module.InteractorModule;
import com.mazzone.isere.transport.di.module.NetModule;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;

public class TransportApplication extends Application {

    private static final String BASE_URL = "http://www.itinisere.fr:80/webServices/TransinfoService/api/";

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init libraries
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        LeakCanary.install(this);

        // Init Dagger
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BASE_URL))
                .interactorModule(new InteractorModule())
                .databaseModule(new DatabaseModule())
                .build();
    }

    public static TransportApplication get(Context context) {
        return (TransportApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return sAppComponent;
    }
}
