package org.rm7370rf.estherproject.di;

import androidx.work.WorkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WorkManagerModule {
    @Singleton
    @Provides
    public WorkManager provideWorkManager() {
        return WorkManager.getInstance();
    }
}
