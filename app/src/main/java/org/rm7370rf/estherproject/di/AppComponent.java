package org.rm7370rf.estherproject.di;

import org.rm7370rf.estherproject.ui.activity.TopicActivity;
import org.rm7370rf.estherproject.ui.activity.TopicListActivity;
import org.rm7370rf.estherproject.ui.presenter.AccountDataPresenter;
import org.rm7370rf.estherproject.ui.presenter.AddPostPresenter;
import org.rm7370rf.estherproject.ui.presenter.AddTopicPresenter;
import org.rm7370rf.estherproject.ui.presenter.BackupPresenter;
import org.rm7370rf.estherproject.ui.presenter.SetUsernamePresenter;
import org.rm7370rf.estherproject.ui.presenter.TopicListPresenter;
import org.rm7370rf.estherproject.ui.presenter.TopicPresenter;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ContractModule.class, RefreshAnimationUtilModule.class})
@Singleton
public interface AppComponent {
    void inject(TopicActivity activity);
    void inject(TopicListActivity activity);
    void inject(AccountDataPresenter presenter);
    void inject(TopicListPresenter presenter);
    void inject(AddPostPresenter presenter);
    void inject(AddTopicPresenter presenter);
    void inject(BackupPresenter presenter);
    void inject(SetUsernamePresenter presenter);
    void inject(TopicPresenter presenter);
}
