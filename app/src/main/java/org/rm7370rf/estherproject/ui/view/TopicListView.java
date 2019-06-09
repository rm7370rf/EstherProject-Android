package org.rm7370rf.estherproject.ui.view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface TopicListView extends MvpView {
    void showToast(int resource);
    void showToast(String message);
    void enableLoading(int refreshType);
    void disableLoading(int refreshType);
    void setNoDataVisibility(int visibility);
    void setHasUsername(boolean hasUsername);
}
