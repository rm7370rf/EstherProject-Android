package org.rm7370rf.estherproject.wr;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.rm7370rf.estherproject.other.Config;

import java.util.concurrent.TimeUnit;

import static androidx.work.NetworkType.CONNECTED;

public class RefreshScheduler {
    private Constraints constraints;

    public RefreshScheduler() {
        this.constraints = new Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build();
    }

    public void prepareWorkManager() {
        PeriodicWorkRequest request = buildUpdateTopicWork();
        WorkManager.getInstance().enqueue(request);
    }

    public void cancelAll() {
        WorkManager.getInstance().cancelAllWork();
    }

    private PeriodicWorkRequest buildUpdateTopicWork() {
        return new PeriodicWorkRequest.Builder(UpdateTopicsWorker.class, 15, TimeUnit.MINUTES)
                .addTag(Config.UPD_TOPIC_TAG)
                .setConstraints(constraints)
                .build();
    }
}
