package org.rm7370rf.estherproject.ui.presenter;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.view.AccountDataView;
import org.rm7370rf.estherproject.util.DBHelper;

import java.math.BigDecimal;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    private Account account = Account.get();

    @Inject
    DBHelper dbHelper;

    public AccountDataPresenter() {
        EstherProject.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().prepareView(Account.get());
    }

    public void refreshUserData(boolean bySwipe) {
        disposable = Single.fromCallable(() -> contract.getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BigDecimal>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        getViewState().enabledLoading(bySwipe);
                    }

                    @Override
                    public void onSuccess(BigDecimal balance) {
                        getViewState().setBalance(String.valueOf(balance));
                        dbHelper.executeTransaction(r -> account.setBalance(balance));

                        onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showToast(e);
                        onComplete();
                    }

                    private void onComplete() {
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
