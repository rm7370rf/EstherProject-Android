package org.rm7370rf.estherproject.ui.presenter;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.ui.view.BackupView;
import org.rm7370rf.estherproject.util.Verifier;
import org.web3j.crypto.Credentials;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.R.string.please_backup_private_key;

@InjectViewState
public class BackupPresenter extends MvpPresenter<BackupView> {
    private Disposable disposable;
    @Inject
    Contract contract;

    public BackupPresenter() {
        EstherProject.getComponent().inject(this);
    }

    public void onClick(String password) {
        disposable = Single.fromCallable(() -> {
            Verifier.verifyPassword(password);
            Credentials credentials = contract.getCredentials(password);
            return credentials.getEcKeyPair().getPrivateKey().toString(16);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<String>() {
            @Override
            protected void onStart() {
                getViewState().collapsePositiveButton();
            }

            @Override
            public void onSuccess(String privateKey) {
                getViewState().setPrivateKey(privateKey);
                getViewState().showToast(please_backup_private_key);
                getViewState().expandPositiveButton();
            }

            @Override
            public void onError(Throwable e) {
                getViewState().showToast(e.getLocalizedMessage());
                getViewState().expandPositiveButton();
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
