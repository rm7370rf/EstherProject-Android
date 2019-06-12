package org.rm7370rf.estherproject.di;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.util.RefreshAnimationUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class RefreshAnimationUtilModule {
    @Provides
    @NonNull
    public RefreshAnimationUtil provideRefreshAnimationUtil() {
        return new RefreshAnimationUtil();
    }
}
