package org.rm7370rf.estherproject.util;

import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;

import io.realm.Realm;

public class ReceiverUtil {
    @Inject
    DBHelper dbHelper;

    @Inject
    Contract contract;

    public ReceiverUtil() {
        EstherProject.getComponent().inject(this);
    }

    public boolean loadNewBalanceToDatabase() throws Exception {
        Account account = Account.get();
        BigDecimal prevBalance = account.getBalance();
        BigDecimal balance = contract.getBalance();

        boolean balanceChanged = (prevBalance.compareTo(balance) != 0);
        if(balanceChanged) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(r -> account.setBalance(balance));
            }
        }
        return balanceChanged;
    }

    public void loadNewTopicsToDatabase() throws Exception {
        BigInteger numberOfTopics = contract.countTopics();
        BigInteger localNumberOfTopics = BigInteger.valueOf(dbHelper.countTopics());

        if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
            for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                Topic topic = contract.getTopic(i);
                try(Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(r -> r.copyToRealm(topic));
                }
            }
        }
    }
}
