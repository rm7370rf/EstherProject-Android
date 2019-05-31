package org.rm7370rf.estherproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.rm7370rf.estherproject.utils.Toast;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.rm7370rf.estherproject.R.string.invalid_private_key;
import static org.rm7370rf.estherproject.R.string.password_required;
import static org.rm7370rf.estherproject.R.string.passwords_do_not_match;
import static org.rm7370rf.estherproject.R.string.private_key_required;
import static org.rm7370rf.estherproject.R.string.repeat_password_required;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.createAccountBtn, R.id.importAccountBtn})
    public void onClick(View view) {
        int dialogId;
        List<EditText> editTextList;

        AMDialog dialog = new AMDialog(this);
        switch (view.getId()) {
            case R.id.createAccountBtn:
                dialogId = R.layout.dialog_create_account;
                editTextList = dialog.getEditTextList(R.id.passwordEdit, R.id.repeatPasswordEdit, R.id.privateKeyEdit);
                break;
            case R.id.importAccountBtn:
                dialogId = R.layout.dialog_import_account;
                editTextList = dialog.getEditTextList(R.id.passwordEdit, R.id.repeatPasswordEdit);
                break;
            default:
                return;
        }

        dialog.setLayout(dialogId);

        dialog.setOnClickListener(v -> {
            String password = editTextList.get(0).getText().toString(),
                   repeatPassword = editTextList.get(1).getText().toString(),
                   privateKey;

            checkPassword(password);
            checkRepeatPassword(repeatPassword);
            checkPasswords(password, repeatPassword);

            switch (view.getId()) {
                case R.id.createAccountBtn:
                    try {
                        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
                        privateKey = ecKeyPair.getPrivateKey().toString(16);
                    } catch (Exception e) {
                        Toast.show(this, e.getLocalizedMessage());
                        return;
                    }

                    Log.d("POSITIVE", "CREATE");
                    break;
                case R.id.importAccountBtn:
                    privateKey = editTextList.get(2).getText().toString();
                    Log.d("POSITIVE", "IMPORT");
                    break;
                default:
                    return;
            }

            checkPrivateKey(privateKey);
        });

        dialog.show();
    }

    //TODO: Move to separate class
    private void checkPrivateKey(String privateKey) {
        if(privateKey.isEmpty()) {
            Toast.show(this, private_key_required);
        }

        if(WalletUtils.isValidPrivateKey(privateKey)) {
            Toast.show(this, invalid_private_key);
        }
    }

    private void checkPassword(String password) {
        if(password.isEmpty()) {
            Toast.show(this, password_required);
        }
    }

    private void checkRepeatPassword(String repeatPassword) {
        if(repeatPassword.isEmpty()) {
            Toast.show(this, repeat_password_required);
        }
    }

    private void checkPasswords(String password, String repeatPassword) {
        if(!password.equals(repeatPassword)) {
            Toast.show(this, passwords_do_not_match);
        }
    }
}
