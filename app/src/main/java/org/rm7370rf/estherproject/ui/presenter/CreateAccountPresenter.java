package org.rm7370rf.estherproject.ui.presenter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.view.CreateAccountView;
import org.rm7370rf.estherproject.ui.view.DialogView;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.Verifier;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.R.string.account_saved;
import static org.rm7370rf.estherproject.util.Verifier.verifyAccountExistence;

@InjectViewState
public class CreateAccountPresenter extends MvpPresenter<CreateAccountView> {
    private Disposable disposable;

    public CreateAccountPresenter() {
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

    public void onClick(int btnId, List<String> valueList, String path) {
        Single<Account> single = Single.fromCallable(() -> createAccount(btnId, valueList, path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        this.disposable = single.subscribeWith(new DisposableSingleObserver<Account>() {
            @Override
            protected void onStart() {
                getViewState().collapsePositiveButton();
            }

            @Override
            public void onSuccess(Account account) {
                if(account != null) {
                    Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealm(account));
                    getViewState().showToast(account_saved);
                    getViewState().expandPositiveButton();
                    getViewState().onComplete();
                }

            }

            @Override
            public void onError(Throwable e) {
                getViewState().showToast(e.getLocalizedMessage());
                e.printStackTrace();
                getViewState().expandPositiveButton();
            }
        });
    }

    private Account createAccount(int btnId, List<String> valueList, String path) {
        try {
            verifyAccountExistence();
            String password = valueList.get(0),
                    repeatPassword = valueList.get(1),
                    privateKey;

            Verifier.verifyPassword(password);
            Verifier.verifyRepeatPassword(repeatPassword);
            Verifier.verifyPasswords(password, repeatPassword);

            if (btnId == R.id.importAccountBtn) {
                privateKey = valueList.get(2);
            } else {
                privateKey = Numeric.toHexStringWithPrefix(Keys.createEcKeyPair().getPrivateKey());
            }

            Verifier.verifyPrivateKey(privateKey);

            Credentials credentials = Credentials.create(privateKey);

            File file = new File(path + "/keystore");

            if (!file.exists()) {
                file.mkdir();
            }

            String walletName = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), file, false);

            String address = credentials.getAddress();

            Account account = new Account();
            account.setWalletName(walletName);
            account.setWalletFolder(file.getPath());
            account.setWalletAddress(address);
            Contract contract = Contract.getInstance()
                    .setAccount(account);
            String userName = contract.getUsername(address);
            if (!userName.isEmpty()) {
                account.setUserName(userName);
            }
            return account;
        }
        catch (VerifierException e) {
            getViewState().showToast(e.getResource());
        }
        catch (Exception e) {
            getViewState().showToast(e.getMessage());
        }
        return null;
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }

}
