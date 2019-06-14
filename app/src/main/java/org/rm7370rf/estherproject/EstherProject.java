package org.rm7370rf.estherproject;

import android.app.Application;

import org.rm7370rf.estherproject.di.AppComponent;
import org.rm7370rf.estherproject.di.ContractModule;
import org.rm7370rf.estherproject.di.DBHelperModule;
import org.rm7370rf.estherproject.di.DaggerAppComponent;
import org.rm7370rf.estherproject.di.ReceiverUtilsModule;
import org.rm7370rf.estherproject.di.RefreshAnimationUtilModule;
import org.rm7370rf.estherproject.di.WorkManagerModule;

public class EstherProject extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        /*if(Account.get() != null) {
            startService(new Intent(this, BalanceService.class));
        }*/
    }

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .dBHelperModule(new DBHelperModule())
                .workManagerModule(new WorkManagerModule())
                .contractModule(new ContractModule())
                .refreshAnimationUtilModule(new RefreshAnimationUtilModule())
                .receiverUtilsModule(new ReceiverUtilsModule())
                .build();
    }
}
