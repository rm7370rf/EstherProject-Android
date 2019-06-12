package org.rm7370rf.estherproject.ui.presenter;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;

@InjectViewState
public class AddPostPresenter extends MvpPresenter<DialogView> {
    private Disposable disposable;
    private Contract contract = Contract.getInstance();

    public void onClick(BigInteger topicId, List<String> valueList) {
        String message = valueList.get(0);
        String password = valueList.get(1);

        disposable = Completable.fromAction(() -> {
            Verifier.verifyMessage(message);
            Verifier.verifyPassword(password);
            contract.addPostToTopic(password, topicId, message);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableCompletableObserver() {
            @Override
            protected void onStart() {
                getViewState().collapsePositiveButton();
            }

            @Override
            public void onComplete() {
                getViewState().showToast(request_successfully_sent);
                getViewState().expandPositiveButton();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getViewState().showToast(e.getLocalizedMessage());
                getViewState().expandPositiveButton();
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
