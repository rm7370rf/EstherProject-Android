package org.rm7370rf.estherproject.ui.presenter;

import android.view.View;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.view.TopicListView;
import org.rm7370rf.estherproject.util.DBHelper;
import org.rm7370rf.estherproject.util.ReceiverUtil;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;


@InjectViewState
public class TopicListPresenter extends MvpPresenter<TopicListView> {
    private Disposable disposable;
    private Account account = Account.get();

    @Inject
    ReceiverUtil receiverUtil;

    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    public TopicListPresenter() {
        EstherProject.getComponent().inject(this);
        setUsername();
    }

    private void setUsername() {
        Account account = Account.get();
        getViewState().setHasUsername(account.hasUsername());
        account.addChangeListener(realmModel -> getViewState().setHasUsername(Account.get().hasUsername()));
    }

    public void updateDatabase() {
        updateDatabase(dbHelper.countTopics() == 0 ? RefreshType.FIRST : RefreshType.AFTER_START);
    }

    public void updateDatabase(RefreshType refreshType) {
        String address = account.getWalletAddress();

        Completable topicCompletable = Completable.fromAction(() -> receiverUtil.loadNewTopicsToDatabase());

        Completable userNameCompletable = Completable.fromAction(() -> receiverUtil.loadNewUsernameToDatabase(address));

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

        if(Account.get() != null) {
            Account.get().removeAllChangeListeners();
        }

        super.onDestroy();
    }
}
