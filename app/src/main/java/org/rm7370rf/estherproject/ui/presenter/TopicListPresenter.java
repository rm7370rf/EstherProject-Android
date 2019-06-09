package org.rm7370rf.estherproject.ui.presenter;

import android.view.View;


import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.ui.view.TopicListView;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;

import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class TopicListPresenter extends MvpPresenter<TopicListView> {
    private CompositeDisposable disposables = new CompositeDisposable();
    private Realm realm = Realm.getDefaultInstance();
    private Account account = Account.get();
    private Contract contract;

    public TopicListPresenter() {
        this.contract = Contract.getInstance().setAccount(realm.copyFromRealm(account));
        getViewState().setHasUsername(account.hasUsername());
    }

    private long countTopics() {
        return realm.where(Topic.class).count();
    }

    public void updateDatabase() {
        updateDatabase(countTopics() == 0 ? RefreshType.FIRST : RefreshType.AFTER_START);
    }

    public void updateDatabase(int refreshType) {
        long amount = countTopics();
        disposables.add(
                Observable.create((ObservableEmitter<Topic> emitter) -> {
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
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                topic -> realm.executeTransaction(r -> r.copyToRealm(topic)),
                                error -> getViewState().showToast(error.getLocalizedMessage()),
                                () -> {
                                    getViewState().disableLoading(refreshType);
                                    getViewState().setNoDataVisibility((countTopics() == 0) ? View.VISIBLE : View.GONE);
                                },
                                i -> getViewState().enableLoading(refreshType)
                        )
        );
    }

    @Override
    public void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}
