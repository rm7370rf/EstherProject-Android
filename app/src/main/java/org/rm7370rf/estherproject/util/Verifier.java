package org.rm7370rf.estherproject.util;

import android.content.Context;

import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.web3j.crypto.WalletUtils;

import io.realm.Realm;
import io.realm.RealmObject;

import static org.rm7370rf.estherproject.R.string.account_already_exists;
import static org.rm7370rf.estherproject.R.string.invalid_private_key;
import static org.rm7370rf.estherproject.R.string.message_required;
import static org.rm7370rf.estherproject.R.string.object_is_null;
import static org.rm7370rf.estherproject.R.string.password_required;
import static org.rm7370rf.estherproject.R.string.passwords_do_not_match;
import static org.rm7370rf.estherproject.R.string.private_key_required;
import static org.rm7370rf.estherproject.R.string.repeat_password_required;
import static org.rm7370rf.estherproject.R.string.subject_required;
import static org.rm7370rf.estherproject.R.string.username_required;
import static org.rm7370rf.estherproject.R.string.value_is_null;

public class Verifier {
    public static void verifyIntentExtra(Object value) throws VerifierException {
        if(value == null) {
            throw new VerifierException(value_is_null);
        }
    }

    public static void verifyRealmObject(RealmObject object) throws VerifierException {
        if(object == null) {
            throw new VerifierException(object_is_null);
        }
    }

    public static void verifySubject(String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(subject_required);
        }
    }

    public static void verifyMessage(String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(message_required);
        }
    }

    public static void verifyPrivateKey(String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(private_key_required);
        }
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new VerifierException(invalid_private_key);
        }
    }

    public static void verifyUserName(String userName) throws VerifierException {
        if(userName.isEmpty()) {
            throw new VerifierException(username_required);
        }
    }

    public static void verifyPassword(String password) throws VerifierException {
        if(password.isEmpty()) {
            throw new VerifierException(password_required);
        }
    }

    public static void verifyRepeatPassword(String repeatPassword) throws VerifierException {
        if(repeatPassword.isEmpty()) {
            throw new VerifierException(repeat_password_required);
        }
    }

    public static void verifyPasswords(String password, String repeatPassword) throws VerifierException {
        if(!password.equals(repeatPassword)) {
            throw new VerifierException(passwords_do_not_match);
        }
    }

    public static void verifyAccountExistence() throws VerifierException {
        if(isAccountExists()) {
            throw new VerifierException(account_already_exists);
        }
    }

    public static boolean isAccountExists() {
        return (Realm.getDefaultInstance().where(Account.class).count() > 0);
    }
}
