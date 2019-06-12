package org.rm7370rf.estherproject.ui.presenter;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.view.SetUsernameView;
import org.rm7370rf.estherproject.util.Verifier;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.username_already_exists;

@InjectViewState
public class SetUsernamePresenter extends MvpPresenter<SetUsernameView> {
    private Disposable disposable;
    @Inject
    Contract contract;
    private Realm realm = Realm.getDefaultInstance();

    public SetUsernamePresenter() {
        EstherProject.getComponent().inject(this);
    }

    public void onClick(List<String> valueList) {
        try {
            Account account = Account.get();
            if (!account.hasUsername()) {
                String userName = valueList.get(0);
                String password = valueList.get(1);

                disposable = Completable.fromAction(() -> {
                    Verifier.verifyUserName(userName);
                    Verifier.verifyPassword(password);
                    contract.setUsername(password, userName);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        getViewState().collapsePositiveButton();
                    }

                    @Override
                    public void onComplete() {
                        getViewState().expandPositiveButton();
                        realm.executeTransaction(r -> account.setUserName(userName));
                        getViewState().showToast(request_successfully_sent);
                        getViewState().onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().expandPositiveButton();
                        e.printStackTrace();
                        getViewState().showToast(e.getLocalizedMessage());
                    }
                });
            }
            else {
                getViewState().showToast(username_already_exists);
            }
        }
        catch (Exception e) {
            getViewState().showToast(e.getLocalizedMessage());
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
