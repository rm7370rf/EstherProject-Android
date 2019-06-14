package org.rm7370rf.estherproject.wr;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.util.ReceiverUtil;

import javax.inject.Inject;

public class UpdateTopicsWorker extends Worker {
    @Inject
    ReceiverUtil receiverUtil;

    public UpdateTopicsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        EstherProject.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            receiverUtil.loadNewTopicsToDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success();
    }
}
