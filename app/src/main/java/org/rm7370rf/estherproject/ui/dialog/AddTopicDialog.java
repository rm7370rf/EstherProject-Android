package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.AddTopicPresenter;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.send;

public class AddTopicDialog extends FieldDialog implements DialogView, CreateAccountDialog.OnClickListener {
    @InjectPresenter
    AddTopicPresenter presenter;

    public AddTopicDialog() {
        setLayout(R.layout.dialog_add_topic);
        setOnClickListener(send, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillEditText();
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
    public void showToast(Throwable e) {
        Toast.show(getContext(), e);
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
    public void onClick(List<String> valueList) {
        presenter.onClick(valueList);
    }
}
