package org.rm7370rf.estherproject.utils;

import android.content.Context;

import org.rm7370rf.estherproject.expceptions.VerifierException;
import org.web3j.crypto.WalletUtils;

import static org.rm7370rf.estherproject.R.string.invalid_private_key;
import static org.rm7370rf.estherproject.R.string.password_required;
import static org.rm7370rf.estherproject.R.string.passwords_do_not_match;
import static org.rm7370rf.estherproject.R.string.private_key_required;
import static org.rm7370rf.estherproject.R.string.repeat_password_required;

public class Verifier {
    public static void verifyPrivateKey(Context context, String privateKey) throws VerifierException {
        if(privateKey.isEmpty()) {
            throw new VerifierException(context, private_key_required);
        }
        if(!WalletUtils.isValidPrivateKey(privateKey)) {
            throw new VerifierException(context, invalid_private_key);
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
}
