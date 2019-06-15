package org.rm7370rf.estherproject.di;

import org.rm7370rf.estherproject.util.DBHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DBHelperModule {
    @Provides
    @Singleton
    public DBHelper provideDBHelper() {
        return new DBHelper();
    }
}
