package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.CreateAccountPresenter;
import org.rm7370rf.estherproject.ui.view.CreateAccountView;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.backup;
import static org.rm7370rf.estherproject.R.string.save;

public class CreateAccountDialog extends FieldDialog implements CreateAccountView, FieldDialog.OnClickListener {
    @InjectPresenter
    CreateAccountPresenter presenter;
    private OnCompleteListener listener;

    public CreateAccountDialog() {
        setOnClickListener(save, this);
    }

    public void setOnCompleteListener(Dialog.OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillEditText();
    }

    @Override
    protected void fillEditText() {
        addToEditTextList(R.id.passwordEdit, R.id.repeatPasswordEdit);

        if(layoutId == R.layout.dialog_import_account) {
            addToEditTextList(R.id.privateKeyEdit);
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
    public void onComplete() {
        listener.onComplete();
    }

    @Override
    public void onClick(List<String> valueList) {
       presenter.onClick(
                buttonId,
                valueList,
                getContext().getApplicationInfo().dataDir
        );
    }
}
