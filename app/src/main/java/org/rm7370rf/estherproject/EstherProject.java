package org.rm7370rf.estherproject;

import android.app.Application;

import org.rm7370rf.estherproject.di.AppComponent;
import org.rm7370rf.estherproject.di.ContractModule;
import org.rm7370rf.estherproject.di.DaggerAppComponent;
import org.rm7370rf.estherproject.di.RefreshAnimationUtilModule;

import io.realm.Realm;

public class EstherProject extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = buildComponent();
    }

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .contractModule(new ContractModule())
                .refreshAnimationUtilModule(new RefreshAnimationUtilModule())
                .build();
    }
}
