package org.rm7370rf.estherproject;

import android.app.Application;
import android.content.Intent;

import org.rm7370rf.estherproject.di.AppComponent;
import org.rm7370rf.estherproject.di.ContractModule;
import org.rm7370rf.estherproject.di.DaggerAppComponent;
import org.rm7370rf.estherproject.di.RealmModule;
import org.rm7370rf.estherproject.di.RefreshAnimationUtilModule;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.service.BalanceService;

import io.realm.Realm;

public class EstherProject extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        if(Account.get() != null) {
            startService(new Intent(this, BalanceService.class));
        }
    }

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .realmModule(new RealmModule(this))
                .contractModule(new ContractModule())
                .refreshAnimationUtilModule(new RefreshAnimationUtilModule())
                .build();
    }
}
