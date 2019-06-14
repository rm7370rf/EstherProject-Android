package org.rm7370rf.estherproject.di;

import android.content.Context;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RealmModule {
    public RealmModule(Context context) {
        Realm.init(context);
    }
    @Provides
    @Singleton
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
