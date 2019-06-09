package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.widget.EditText;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
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

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;

public class AddTopicDialog extends FieldDialog {
    private EditText subjectEdit;
    private EditText messageEdit;
    private EditText passwordEdit;
    private Contract contract = Contract.getInstance();
    private Disposable disposable;

    public AddTopicDialog(Activity activity) {
        super(activity);
        setLayout(R.layout.dialog_add_topic);
        setUI();
        setOnClickListener(send, this::addTopic);
    }

    private void setUI() {
        subjectEdit = getView().findViewById(R.id.subjectEdit);
        messageEdit = getView().findViewById(R.id.messageEdit);
        passwordEdit = getView().findViewById(R.id.passwordEdit);
    }

    private void addTopic(FancyButton button) {
        String subject = subjectEdit.getText().toString();
        String message = messageEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        disposable = Completable.fromAction(() -> {
            Verifier.verifySubject(getContext(), subject);
            Verifier.verifyMessage(getContext(), message);
            Verifier.verifyPassword(getContext(), password);
            contract.addTopic(password, subject, message);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableCompletableObserver() {
            @Override
            protected void onStart() {
                button.collapse();
            }

            @Override
            public void onComplete() {
                Toast.show(getContext(), request_successfully_sent);
                button.expand();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.show(getContext(), e.getLocalizedMessage());
                button.expand();
            }
        });
    }
}
