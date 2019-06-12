package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.ui.presenter.BackupPresenter;
import org.rm7370rf.estherproject.ui.view.BackupView;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;
import org.web3j.crypto.Credentials;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.R.string.load;
import static org.rm7370rf.estherproject.R.string.please_backup_private_key;
import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class BackupDialog extends FieldDialog implements BackupView, FieldDialog.OnClickListener {
    @InjectPresenter
    BackupPresenter presenter;

    public BackupDialog() {
        setLayout(R.layout.dialog_backup);
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
