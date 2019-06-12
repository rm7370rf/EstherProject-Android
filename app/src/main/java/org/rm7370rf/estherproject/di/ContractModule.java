package org.rm7370rf.estherproject.di;

import androidx.annotation.NonNull;

import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ContractModule {
    @Provides
    @NonNull
    @Singleton
    public Contract provideContract() {
        return new Contract().setAccount(Realm.getDefaultInstance().copyFromRealm(Account.get()));
    }
}
