package org.rm7370rf.estherproject.ui.dialog;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.CreateAccountPresenter;
import org.rm7370rf.estherproject.ui.view.CreateAccountView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

public class CreateAccountDialog extends FieldDialog implements CreateAccountView, FieldDialog.OnClickListener {
    @InjectPresenter
    CreateAccountPresenter presenter;

    public CreateAccountDialog() {
        setOnClickListener(this);
        fillEditText();
    }

    @Override
    protected void fillEditText() {
        addToEditTextList(R.id.passwordEdit, R.id.repeatPasswordEdit);

        if(layoutId == R.id.importAccountBtn) {
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
    public void onClick(int buttonId, List<String> valueList) {
        presenter.onClick(
                buttonId,
                valueList,
                getContext().getApplicationInfo().dataDir
        );
    }
}
