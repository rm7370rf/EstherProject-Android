package org.rm7370rf.estherproject.model;


import android.content.Context;

import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Account extends RealmObject {
    @PrimaryKey
    private String walletAddress;
    @Required
    private String walletName;
    @Required
    private String walletFolder;
    @Required
    private String userName = "";
    @Required
    private String balance = String.valueOf(BigInteger.ZERO);

    public Account() { }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public void setWalletFolder(String walletFolder) {
        this.walletFolder = walletFolder;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBalance(BigInteger balance) {
        this.balance = String.valueOf(balance);
    }

    public String getWalletName() {
        return walletName;
    }

    public String getWalletFolder() {
        return (walletFolder + "/");
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getUserName() {
        return userName;
    }

    public boolean hasUsername() {
        return !userName.isEmpty();
    }

    public BigInteger getBalance() {
        return new BigInteger(balance);
    }

    public static Account get(Context context) throws VerifierException {
        Account account = Realm.getDefaultInstance().where(Account.class).findFirst();
        Verifier.verifyRealmObject(context, account);
        return account;
    }
}
