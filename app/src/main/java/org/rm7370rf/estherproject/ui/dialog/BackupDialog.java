package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.presenter.BackupPresenter;
import org.rm7370rf.estherproject.ui.view.BackupView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;

import java.util.List;

import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class BackupDialog extends FieldDialog implements BackupView, FieldDialog.OnClickListener {
    @InjectPresenter
    BackupPresenter presenter;

    public BackupDialog() {
        setLayout(R.layout.dialog_backup);
        setOnClickListener(send, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fillEditText();
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
    protected void fillEditText() {
        addToEditTextList(R.id.passwordEdit);
    }

    @Override
    public void onClick(int buttonId, List<String> valueList) {
        presenter.onClick(valueList.get(0));
    }

    @Override
    public void setPrivateKey(String privateKey) {
        TextView privateKeyText = getView().findViewById(R.id.privateKeyText);
        privateKeyText.setText(privateKey);
        privateKeyText.setVisibility(View.VISIBLE);
        privateKeyText.setOnClickListener(v -> copyToClipboard(getContext(), privateKey));
    }
}
