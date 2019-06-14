package org.rm7370rf.estherproject.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

@Deprecated
public class BalanceService extends IntentService {
    @Inject
    Contract contract;
    private Disposable disposable;
    private int notificationId = 1;

    public BalanceService() {
        super("BALANCE");
        EstherProject.getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
         disposable = Single.fromCallable(() -> contract.getBalance())
        .repeatWhen(observable -> observable.delay(10, TimeUnit.SECONDS))
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(
            balance -> {
                Account account = Account.get();
                BigDecimal prevBalance = account.getBalance();
                if(prevBalance.compareTo(balance) != 0) {
                    Realm.getDefaultInstance().executeTransaction(r -> account.setBalance(balance));
                    sendNotification(balance);
                }
            },
            Throwable::printStackTrace
        );
    }

    private void sendNotification(BigDecimal balance) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("CHANNEL_ID", "name", importance);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Balance changed to " + balance)
                .setContentText(getString(R.string.app_name));

        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }
}
