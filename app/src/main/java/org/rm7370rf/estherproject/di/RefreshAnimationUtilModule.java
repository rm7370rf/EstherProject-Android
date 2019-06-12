package org.rm7370rf.estherproject.di;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RefreshAnimationUtilModule {
    @Provides
    @NonNull
    public RefreshAnimationUtil provideRefreshAnimationUtil() {
        return new RefreshAnimationUtil();
    }
}
