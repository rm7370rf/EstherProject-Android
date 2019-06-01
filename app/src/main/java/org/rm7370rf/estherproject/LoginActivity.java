package org.rm7370rf.estherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ekalips.fancybuttonproj.FancyButton;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.rm7370rf.estherproject.utils.Toast;
import org.rm7370rf.estherproject.utils.Verifier;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static org.rm7370rf.estherproject.R.string.account_saved;
import static org.rm7370rf.estherproject.utils.Config.PREFS;
import static org.rm7370rf.estherproject.utils.Config.WALLET;
import static org.rm7370rf.estherproject.utils.Verifier.isAccountExists;
import static org.rm7370rf.estherproject.utils.Verifier.verifyAccountExistence;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupBouncyCastle();

        if(isAccountExists(this)) {
            startTopicListActivity();
        }
    }

    private void startTopicListActivity() {
        startActivity(new Intent(this, TopicListActivity.class));
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

    public void onClick(View view) {
        System.out.println("Click");
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

        dialog.setOnClickListener(button -> {
            this.disposable = Completable.fromAction(() -> {
                verifyAccountExistence(this);
                String password = editTextList.get(0).getText().toString(),
                        repeatPassword = editTextList.get(1).getText().toString(),
                        privateKey = "";

                Verifier.verifyPassword(this, password);
                Verifier.verifyRepeatPassword(this, repeatPassword);
                Verifier.verifyPasswords(this, password, repeatPassword);

                if (view.getId() == R.id.importAccountBtn) {
                    privateKey = editTextList.get(2).getText().toString();
                } else {
                    privateKey = Numeric.toHexStringWithPrefix(Keys.createEcKeyPair().getPrivateKey());
                }

                Verifier.verifyPrivateKey(this, privateKey);

                Thread.sleep(2000); //TODO: Remove

                Credentials credentials = Credentials.create(privateKey);

                File file = new File(getApplicationInfo().dataDir + "/keystore");

                if (!file.exists()) {
                    file.mkdir();
                }

                WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), file, false);

                SharedPreferences.Editor ed = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                ed.putString(WALLET, file.getPath() + "/" + file.getName());
                ed.commit();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableCompletableObserver() {
                        @Override
                        public void onStart() {
                            button.collapse();
                        }

                        @Override
                        public void onComplete() {
                            Toast.show(button.getContext(), account_saved);
                            button.expand();
                            startTopicListActivity();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            System.out.println("ON_ERROR");
                            Toast.show(button.getContext(), e.getLocalizedMessage());
                            button.expand();
                        }
                    });
        });
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        this.disposable.dispose();
        if(disposable.isDisposed()) {
            Log.d("DISPOSABLE", "YES");
        }
        else {
            Log.d("DISPOSABLE", "NO");
        }
        super.onDestroy();
    }
}