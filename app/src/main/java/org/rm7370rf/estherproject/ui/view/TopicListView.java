package org.rm7370rf.estherproject.ui.view;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

import static org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface TopicListView extends MvpView {
    void showToast(int resource);
    void showToast(Throwable e);
    void enableLoading(RefreshType refreshType);
    void disableLoading(RefreshType refreshType);
    void setNoDataVisibility(int visibility);
    void setHasUsername(boolean hasUsername);
}
