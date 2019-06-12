package org.rm7370rf.estherproject.ui.view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface DialogView extends MvpView {
    void showToast(int resource);
    void showToast(Throwable e);
    void collapsePositiveButton();
    void expandPositiveButton();
}
