package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.ui.presenter.AddTopicPresenter;
import org.rm7370rf.estherproject.ui.view.CreateAccountView;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.R.string.subject;

public class AddTopicDialog extends FieldDialog implements DialogView, CreateAccountDialog.OnClickListener {
    @InjectPresenter
    AddTopicPresenter presenter;

    public AddTopicDialog() {
        setLayout(R.layout.dialog_add_topic);
    }


    @Override
    protected void fillEditText() {
        addToEditTextList(R.id.subjectEdit, R.id.messageEdit, R.id.passwordEdit);
    }

    @Override
    public void showToast(int resource) {
        Toast.show(getContext(), resource);
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
    public void onClick(int buttonId, List<String> valueList) {
        presenter.onClick(valueList);
    }
}
