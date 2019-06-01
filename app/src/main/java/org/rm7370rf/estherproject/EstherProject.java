package org.rm7370rf.estherproject;

import android.app.Application;

import io.realm.Realm;

public class EstherProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
