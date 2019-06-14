package org.rm7370rf.estherproject.wr;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.util.DBHelper;
import org.rm7370rf.estherproject.util.ReceiverUtils;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.Realm;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UpdateTopicsWorker extends Worker {
    @Inject
    Contract contract;

    @Inject
    ReceiverUtils receiverUtils;

    public UpdateTopicsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        EstherProject.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            receiverUtils.loadNewTopicsToDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success();
    }
}
