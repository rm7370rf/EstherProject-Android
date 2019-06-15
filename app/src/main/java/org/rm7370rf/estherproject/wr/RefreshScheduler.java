package org.rm7370rf.estherproject.wr;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.rm7370rf.estherproject.other.Config;

import java.util.concurrent.TimeUnit;

import static androidx.work.NetworkType.CONNECTED;

public class RefreshScheduler {
    public static Constraints buildConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build();
    }

    public static void prepareWorkManager() {
        PeriodicWorkRequest request = buildUpdateTopicWork();
        WorkManager.getInstance().enqueue(request);
    }

    public static void cancelAll() {
        WorkManager.getInstance().cancelAllWork();
    }

    private static PeriodicWorkRequest buildUpdateTopicWork() {
        return new PeriodicWorkRequest.Builder(UpdateTopicsWorker.class, 15, TimeUnit.MINUTES)
                .addTag(Config.UPD_TOPIC_TAG)
                .setConstraints(buildConstraints())
                .build();
    }
}
