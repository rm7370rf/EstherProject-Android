package org.rm7370rf.estherproject.di;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.util.ReceiverUtils;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class ReceiverUtilsModule {
    @Provides
    @NonNull
    public ReceiverUtils provideReceiverUtils() {
        return new ReceiverUtils();
    }

}
