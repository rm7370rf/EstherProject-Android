package org.rm7370rf.estherproject.wr;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.util.DBHelper;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.Realm;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UpdateTopicsWorker extends Worker {
    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    public UpdateTopicsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            BigInteger numberOfTopics = contract.countTopics();
            BigInteger localNumberOfTopics = BigInteger.valueOf(dbHelper.countTopics());

            if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
                for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                    Topic topic = contract.getTopic(i);
                    dbHelper.executeTransaction(r -> r.copyToRealm(topic));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }
}
