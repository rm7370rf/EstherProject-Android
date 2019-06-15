package org.rm7370rf.estherproject;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import org.rm7370rf.estherproject.di.AppComponent;
import org.rm7370rf.estherproject.di.ContractModule;
import org.rm7370rf.estherproject.di.DBHelperModule;
import org.rm7370rf.estherproject.di.DaggerAppComponent;
import org.rm7370rf.estherproject.di.ReceiverUtilModule;
import org.rm7370rf.estherproject.di.RefreshAnimationUtilModule;
import org.rm7370rf.estherproject.di.WorkManagerModule;
import org.rm7370rf.estherproject.wr.RefreshScheduler;

import io.realm.Realm;

public class EstherProject extends Application implements LifecycleObserver {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = buildComponent();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onEnterForeground() {
        RefreshScheduler.cancelAll();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onEnterBackground() {
        RefreshScheduler.prepareWorkManager();
    }

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .dBHelperModule(new DBHelperModule())
                .contractModule(new ContractModule())
                .receiverUtilModule(new ReceiverUtilModule())
                .workManagerModule(new WorkManagerModule())
                .refreshAnimationUtilModule(new RefreshAnimationUtilModule())
                .build();
    }
}
