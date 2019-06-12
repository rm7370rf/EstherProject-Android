package org.rm7370rf.estherproject.ui.presenter;

import android.view.View;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.ui.view.AccountDataView;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.Toast;

import java.math.BigDecimal;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

public class AccountDataPresenter extends MvpPresenter<AccountDataView> {
    private Disposable disposable;
    private Contract contract = Contract.getInstance();

    public void refreshUserData(boolean bySwipe) {
        Single<BigDecimal> single = Single.fromCallable(() -> contract.getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        this.disposable = single.subscribeWith(new DisposableSingleObserver<BigDecimal>() {
            @Override
            protected void onStart() {
                super.onStart();
                getViewState().enabledLoading(bySwipe);
            }

            @Override
            public void onSuccess(BigDecimal balance) {
                getViewState().setBalance(String.valueOf(balance));
                onComplete();
            }

            @Override
            public void onError(Throwable e) {
                getViewState().showToast(e.getLocalizedMessage());
                onComplete();
            }

            private void onComplete() {
                getViewState().disableLoading(bySwipe);
            }
        });
    }

    @Override
    public void onDestroy() {
        if(!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
