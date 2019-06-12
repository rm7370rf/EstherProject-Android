package org.rm7370rf.estherproject.ui.view;

import org.rm7370rf.estherproject.model.Post;

import io.realm.OrderedRealmCollection;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface TopicView extends MvpView {
    void setRecyclerAdapter(OrderedRealmCollection<Post> posts);
    void setTitle(String title);
    void showToast(int resource);
    void showToast(String message);
    void enableLoading(int refreshType);
    void disableLoading(int refreshType);
    void finish();
}
