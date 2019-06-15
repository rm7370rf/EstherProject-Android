package org.rm7370rf.estherproject.ui.presenter;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.view.AccountDataView;
import org.rm7370rf.estherproject.util.DBHelper;
import org.rm7370rf.estherproject.util.ReceiverUtil;

import java.math.BigDecimal;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class AccountDataPresenter extends MvpPresenter<AccountDataView> {
    private Disposable disposable;
    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    @Inject
    ReceiverUtil receiverUtil;

    private Account account = Account.get();

    public AccountDataPresenter() {
        EstherProject.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().prepareView(account);
        setBalanceListener();
    }

    private void setBalanceListener() {
        account.addChangeListener(realmModel -> {
            String balance = String.valueOf(account.getBalance());
            getViewState().setBalance(balance);
        });
    }

    public void refreshUserData(boolean bySwipe) {
        disposable = Completable.fromAction(() -> receiverUtil.loadNewBalanceToDatabase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        getViewState().enabledLoading(bySwipe);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showToast(e);
                        onComplete();
                    }

                    @Override
                    public void onComplete() {
                        getViewState().disableLoading(bySwipe);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
