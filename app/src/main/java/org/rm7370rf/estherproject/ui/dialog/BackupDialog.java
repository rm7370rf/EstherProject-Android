package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;
import org.web3j.crypto.Credentials;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.rm7370rf.estherproject.R.string.load;
import static org.rm7370rf.estherproject.R.string.please_backup_private_key;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class BackupDialog extends FieldDialog {
    private TextView privateKeyText;
    private EditText passwordEdit;

    private Disposable disposable;
    private Contract contract = Contract.getInstance();

    public BackupDialog(Activity activity) {
        super(activity);
        setLayout(R.layout.dialog_backup);
        initUI();
        setOnClickListener(load, this::showBackup);
    }

    private void initUI() {
        privateKeyText = getView().findViewById(R.id.privateKeyText);
        passwordEdit = getView().findViewById(R.id.passwordEdit);
    }

    private void showBackup(FancyButton button) {
        String password = passwordEdit.getText().toString();
        disposable = Single.fromCallable(() -> {
            Verifier.verifyPassword(getContext(), password);
            Credentials credentials = contract.getCredentials(password);
            return credentials.getEcKeyPair().getPrivateKey().toString(16);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<String>() {
            @Override
            protected void onStart() {
                button.collapse();
            }

            @Override
            public void onSuccess(String privateKey) {
                privateKeyText.setText(privateKey);
                privateKeyText.setVisibility(View.VISIBLE);
                privateKeyText.setOnClickListener(v -> copyToClipboard(getContext(), privateKey));
                Toast.show(getContext(), please_backup_private_key);
                button.expand();
            }

            @Override
            public void onError(Throwable e) {
                Toast.show(getContext(), e.getLocalizedMessage());
                button.expand();
            }
        });
    }
}
