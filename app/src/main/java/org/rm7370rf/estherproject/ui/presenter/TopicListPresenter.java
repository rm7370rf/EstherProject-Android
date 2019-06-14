package org.rm7370rf.estherproject.ui.presenter;

import android.view.View;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Config;
import org.rm7370rf.estherproject.ui.view.TopicListView;
import org.rm7370rf.estherproject.util.DBHelper;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;
import org.rm7370rf.estherproject.wr.UpdateTopicsWorker;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static androidx.work.NetworkType.CONNECTED;

@InjectViewState
public class TopicListPresenter extends MvpPresenter<TopicListView> {
    private Disposable disposable;
    private Account account = Account.get();

    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    public TopicListPresenter() {
        EstherProject.getComponent().inject(this);
        prepareWorkManager();
        setAccountUsernameListener();
    }

    private void setAccountUsernameListener() {
        Account.get().addChangeListener(realmModel -> getViewState().setHasUsername(Account.get().hasUsername()));
    }

    private void prepareWorkManager() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build();

        PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(UpdateTopicsWorker.class, 15, TimeUnit.MINUTES)
                .addTag(Config.UPD_TOPIC_TAG)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(myWorkRequest);
    }

    public void updateDatabase() {
        updateDatabase(dbHelper.countTopics() == 0 ? RefreshType.FIRST : RefreshType.AFTER_START);
    }

    public void updateDatabase(RefreshType refreshType) {
        long amount = dbHelper.countTopics();
        String address = account.getWalletAddress();

        Completable topicCompletable = Completable.fromAction(() -> {
            BigInteger numberOfTopics = contract.countTopics();
            BigInteger localNumberOfTopics = BigInteger.valueOf(amount);

            if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
                for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                    Topic topic = contract.getTopic(i);
                    try(Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(r -> r.copyToRealm(topic));
                    }
                }
            }
        });

        Completable userNameCompletable = Completable.fromAction(() -> {
            String username = contract.getUsername(address);
            try(Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(r -> Account.get().setUserName(username));
            }
        });


        disposable = topicCompletable.andThen(userNameCompletable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    protected void onStart() {
                        getViewState().enableLoading(refreshType);
                    }

                    @Override
                    public void onComplete() {
                        getViewState().disableLoading(refreshType);
                        getViewState().setNoDataVisibility((dbHelper.countTopics() == 0) ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showToast(e);
                    }
                });
    }

    public void logout(Realm.Transaction.OnSuccess listener) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(r -> r.deleteAll(), listener);
        }
    }

    @Override
    public void onDestroy() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
