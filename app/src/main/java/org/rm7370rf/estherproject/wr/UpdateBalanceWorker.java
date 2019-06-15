package org.rm7370rf.estherproject.wr;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.other.Config;
import org.rm7370rf.estherproject.util.Notification;
import org.rm7370rf.estherproject.util.ReceiverUtil;

import java.math.BigDecimal;

import javax.inject.Inject;

public class UpdateBalanceWorker extends Worker {
    @Inject
    ReceiverUtil receiverUtil;

    public UpdateBalanceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        EstherProject.getComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(Config.UPD_BALANCE_TAG, "@doWork");
            if(receiverUtil.loadNewBalanceToDatabase()) {
                Notification.show(getApplicationContext(), Config.BALANCE_NOTIFICATION_ID, R.string.app_name, R.string.changed_balance);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
        return Result.success();
    }
}
