package org.rm7370rf.estherproject.utils;

import android.content.Context;

import org.rm7370rf.estherproject.expceptions.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.web3j.crypto.WalletUtils;

import io.realm.Realm;
import io.realm.RealmObject;

import static org.rm7370rf.estherproject.R.string.invalid_private_key;
import static org.rm7370rf.estherproject.R.string.message_required;
import static org.rm7370rf.estherproject.R.string.password_required;
import static org.rm7370rf.estherproject.R.string.account_already_exists;
import static org.rm7370rf.estherproject.R.string.passwords_do_not_match;
import static org.rm7370rf.estherproject.R.string.private_key_required;
import static org.rm7370rf.estherproject.R.string.repeat_password_required;
import static org.rm7370rf.estherproject.R.string.subject_required;
import static org.rm7370rf.estherproject.R.string.username_required;
import static org.rm7370rf.estherproject.R.string.object_is_null;
import static org.rm7370rf.estherproject.R.string.value_is_null;

public class Verifier {
    public static void verifyIntentExtra(Context context, Object value) throws VerifierException {
        if(value == null) {
            throw new VerifierException(context, value_is_null);
        }
    }
    public static void verifyRealmObject(Context context, RealmObject object) throws VerifierException {
        if(object == null) {
            throw new VerifierException(context, object_is_null);
        }
    }

    public static void verifySubject(Context context, String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(context, subject_required);
        }
    }

    public static void verifyMessage(Context context, String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(context, message_required);
        }
    }

    public static void verifyPrivateKey(Context context, String privateKey) throws VerifierException {
        if(privateKey == null || privateKey.isEmpty()) {
            throw new VerifierException(context, private_key_required);
        }
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new VerifierException(context, invalid_private_key);
        }
    }

    public static void verifyUserName(Context context, String userName) throws VerifierException {
        if(userName.isEmpty()) {
            throw new VerifierException(context, username_required);
        }
    }

    public static void verifyPassword(Context context, String password) throws VerifierException {
        if(password.isEmpty()) {
            throw new VerifierException(context, password_required);
        }
    }

    public static void verifyRepeatPassword(Context context, String repeatPassword) throws VerifierException {
        if(repeatPassword.isEmpty()) {
            throw new VerifierException(context, repeat_password_required);
        }
    }

    public static void verifyPasswords(Context context, String password, String repeatPassword) throws VerifierException {
        if(!password.equals(repeatPassword)) {
            throw new VerifierException(context, passwords_do_not_match);
        }
    }

    public static void verifyAccountExistence(Context context) throws VerifierException {
        if(isAccountExists()) {
            throw new VerifierException(context, account_already_exists);
        }
    }

    public static boolean isAccountExists() {
        return (Realm.getDefaultInstance().where(Account.class).count() > 0);
    }
}
