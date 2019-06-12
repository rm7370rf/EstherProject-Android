package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.presenter.SetUsernamePresenter;
import org.rm7370rf.estherproject.ui.view.CreateAccountView;
import org.rm7370rf.estherproject.ui.view.SetUsernameView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.R.string.username_already_exists;

public class SetUsernameDialog extends FieldDialog implements SetUsernameView, CreateAccountDialog.OnClickListener {
    @InjectPresenter
    SetUsernamePresenter presenter;

    private OnCompleteListener listener = null;

    public SetUsernameDialog() {
        setLayout(R.layout.dialog_set_username);
        setOnClickListener(send, this);
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void onComplete() {
        if(listener != null) {
            listener.onComplete();
        }
    }

    @Override
    public void showToast(int resource) {
        showToast(getString(resource));
    }

    @Override
    public void showToast(String message) {
        Toast.show(getContext(), message);
    }

    @Override
    public void collapsePositiveButton() {
        super.collapsePositiveButton();
    }

    @Override
    public void expandPositiveButton() {
        super.expandPositiveButton();
    }

    @Override
    protected void fillEditText() {
        addToEditTextList(R.id.userNameEdit, R.id.passwordEdit);
    }

    @Override
    public void onClick(int buttonId, List<String> valueList) {
        presenter.onClick(valueList);
    }

    public interface OnCompleteListener {
        void onComplete();
    }
}
