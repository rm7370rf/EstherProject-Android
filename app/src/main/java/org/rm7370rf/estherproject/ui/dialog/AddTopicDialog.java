package org.rm7370rf.estherproject.ui.dialog;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.AddTopicPresenter;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

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
