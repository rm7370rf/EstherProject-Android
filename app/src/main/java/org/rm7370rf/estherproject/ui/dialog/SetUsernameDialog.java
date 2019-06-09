package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.R.string.username_already_exists;

public class SetUsernameDialog extends FieldDialog {
    private EditText userNameEdit;
    private EditText passwordEdit;

    private Realm realm = Realm.getDefaultInstance();
    private Disposable disposable;
    private Contract contract = Contract.getInstance();
    private OnCompleteListener listener = null;


    public SetUsernameDialog(Activity activity) {
        super(activity);
        setLayout(R.layout.dialog_set_username);
        initUI();
        setOnClickListener(send, this::setUserName);
    }

    private void initUI() {
        userNameEdit = getView().findViewById(R.id.userNameEdit);
        passwordEdit = getView().findViewById(R.id.passwordEdit);
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }

    private void setUserName(FancyButton button) {
        try {
            Account account = Account.get(getContext());
            if (!account.hasUsername()) {
                String userName = userNameEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                disposable = Completable.fromAction(() -> {
                    Verifier.verifyUserName(getContext(), userName);
                    Verifier.verifyPassword(getContext(), password);
                    contract.setUsername(password, userName);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        button.collapse();
                    }

                    @Override
                    public void onComplete() {
                        button.expand();
                        hide();
                        realm.executeTransaction(realm -> account.setUserName(userName));

                        Toast.show(getContext(), request_successfully_sent);
                        if (listener != null) {
                            listener.onComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        button.expand();
                        e.printStackTrace();
                        Toast.show(getContext(), e.getLocalizedMessage());
                    }
                });
            }
            else {
                Toast.show(getContext(), username_already_exists);
            }
        }
        catch (Exception e) {
            Toast.show(getContext(), e.getLocalizedMessage());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        disposable.dispose();
    }

    public interface OnCompleteListener {
        void onComplete();
    }
}
