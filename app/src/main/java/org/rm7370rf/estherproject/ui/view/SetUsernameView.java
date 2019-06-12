package org.rm7370rf.estherproject.ui.view;

import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface SetUsernameView extends DialogView {
    void onComplete();
}
