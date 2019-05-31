package org.rm7370rf.estherproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.rm7370rf.estherproject.utils.Toast;
import org.rm7370rf.estherproject.utils.Verifier;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
        List<Integer> resourceList = new ArrayList<>();
        resourceList.add(R.id.passwordEdit);
        resourceList.add(R.id.repeatPasswordEdit);

        AMDialog dialog = new AMDialog(this);
        switch (view.getId()) {
            case R.id.createAccountBtn:
                dialogId = R.layout.dialog_create_account;
                break;
            case R.id.importAccountBtn:
                dialogId = R.layout.dialog_import_account;
                resourceList.add(R.id.privateKeyEdit);
                break;
            default:
                return;
        }

        dialog.setLayout(dialogId);


        List<EditText> editTextList = dialog.getEditTextList(resourceList);
        dialog.setOnClickListener(v -> {
            try {
                String password = editTextList.get(0).getText().toString(),
                       repeatPassword = editTextList.get(1).getText().toString(),
                       privateKey;

                Verifier.verifyPassword(this, password);
                Verifier.verifyRepeatPassword(this, repeatPassword);
                Verifier.verifyPasswords(this, password, repeatPassword);

                switch (view.getId()) {
                    case R.id.createAccountBtn:
                        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
                        privateKey = ecKeyPair.getPrivateKey().toString(16);
                        Log.d("PRIVATE_KEY", "|" + privateKey + "|");
                        Log.d("POSITIVE", "CREATE");
                        break;
                    case R.id.importAccountBtn:
                        privateKey = editTextList.get(2).getText().toString();
                        Log.d("POSITIVE", "IMPORT");
                        break;
                    default:
                        return;
                }
                Verifier.verifyPrivateKey(this, privateKey);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.show(this, e.getLocalizedMessage());
            }
        });

        dialog.show();
    }
}