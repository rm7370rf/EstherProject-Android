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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static androidx.work.NetworkType.CONNECTED;

@InjectViewState
public class TopicListPresenter extends MvpPresenter<TopicListView> {
    private Disposable disposable;
    private Realm realm = Realm.getDefaultInstance();
    private Account account = Account.get();

    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    public TopicListPresenter() {
        EstherProject.getComponent().inject(this);
        prepareWorkManager();
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

        Observable<Topic> topicObservable = Observable.create((ObservableEmitter<Topic> emitter) -> {
            try {
                BigInteger numberOfTopics = contract.countTopics();
                BigInteger localNumberOfTopics = BigInteger.valueOf(amount);

                if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
                    for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                        Topic topic = contract.getTopic(i);
                        emitter.onNext(topic);
                    }
                }
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });

        Observable<String> userNameObservable = Observable.fromCallable(() -> contract.getUsername(address));

        disposable = Observable.concat(topicObservable, userNameObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (result instanceof Topic) {
                                dbHelper.executeTransaction(r -> r.copyToRealm((Topic) result));
                            }
                            else if(result instanceof String) {
                                String username = result.toString();
                                if(!username.isEmpty()) {
                                    dbHelper.executeTransaction(r -> account.setUserName((String) result));
                                }
                                getViewState().setHasUsername(account.hasUsername());
                            }
                        },
                        error -> getViewState().showToast(error),
                        () -> {
                            getViewState().disableLoading(refreshType);
                            getViewState().setNoDataVisibility((dbHelper.countTopics() == 0) ? View.VISIBLE : View.GONE);
                        },
                        i -> getViewState().enableLoading(refreshType)
                );
    }

    public void logout(Realm.Transaction.OnSuccess listener) {
        dbHelper.executeTransaction(r -> r.deleteAll(), listener);
    }

    @Override
    public void onDestroy() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
