package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.SetUsernamePresenter;
import org.rm7370rf.estherproject.ui.view.SetUsernameView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.send;

public class SetUsernameDialog extends FieldDialog implements SetUsernameView, CreateAccountDialog.OnClickListener {
    @InjectPresenter
    SetUsernamePresenter presenter;

    private OnCompleteListener listener = null;

    public SetUsernameDialog() {
        setLayout(R.layout.dialog_set_username);
        setOnClickListener(send, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillEditText();
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
}
