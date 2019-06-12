package org.rm7370rf.estherproject.ui.view;

import org.rm7370rf.estherproject.model.Account;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface AccountDataView extends MvpView {
    void setBalance(String balance);
    void showToast(Throwable e);
    void enabledLoading(boolean bySwipe);
    void disableLoading(boolean bySwipe);
    void prepareView(Account account);
}
