package org.rm7370rf.estherproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.rm7370rf.estherproject.utils.Toast;
import org.rm7370rf.estherproject.utils.Verifier;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.Provider;
import java.security.Security;
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
        setupBouncyCastle();

    }

    private void setupBouncyCastle() {
        //Based on https://github.com/web3j/web3j/issues/915
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
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
                        privateKey = "0x" + ecKeyPair.getPrivateKey().toString(16);
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