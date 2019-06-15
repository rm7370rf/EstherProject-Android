package org.rm7370rf.estherproject.wr;

import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.rm7370rf.estherproject.other.Config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static androidx.work.NetworkType.CONNECTED;

public class RefreshScheduler {
    public static Constraints buildConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build();
    }

    public static void prepareWorkManager() {
        PeriodicWorkRequest updateTopicRequest = buildPeriodicWorkRequest(Config.UPD_TOPIC_TAG, UpdateTopicsWorker.class);
        PeriodicWorkRequest balanceTopicRequest = buildPeriodicWorkRequest(Config.UPD_BALANCE_TAG, UpdateBalanceWorker.class);
        WorkManager.getInstance().enqueue(Arrays.asList(
                updateTopicRequest,
                balanceTopicRequest
        ));
    }

    public static void cancelAll() {
        WorkManager.getInstance().cancelAllWork();
    }

    private static PeriodicWorkRequest buildPeriodicWorkRequest(String tag, Class<? extends ListenableWorker> worker) {
        return new PeriodicWorkRequest.Builder(worker, 15, TimeUnit.MINUTES)
                .addTag(tag)
                .setConstraints(buildConstraints())
                .build();
    }
}
