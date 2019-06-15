package org.rm7370rf.estherproject.di;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.util.ReceiverUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class ReceiverUtilModule {
    @Provides
    @NonNull
    public ReceiverUtil provideReceiverUtils() {
        return new ReceiverUtil();
    }
}
