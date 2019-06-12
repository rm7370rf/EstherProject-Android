package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.AddPostPresenter;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.math.BigInteger;
import java.util.List;

import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.send;

public class AddPostDialog extends FieldDialog implements DialogView, CreateAccountDialog.OnClickListener {
    @InjectPresenter
    AddPostPresenter presenter;
    private BigInteger topicId;

    public AddPostDialog() {
        setLayout(R.layout.dialog_add_post);
        setOnClickListener(send, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillEditText();
    }

    public void setTopicId(BigInteger topicId) {
        this.topicId = topicId;
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
        addToEditTextList(R.id.messageEdit, R.id.passwordEdit);
    }

    @Override
    public void onClick(int buttonId, List<String> valueList) {
        presenter.onClick(topicId, valueList);
    }
}
