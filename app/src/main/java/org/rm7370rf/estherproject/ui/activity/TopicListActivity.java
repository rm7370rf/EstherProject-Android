package org.rm7370rf.estherproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.ui.adapter.TopicsAdapter;
import org.rm7370rf.estherproject.ui.dialog.AccountDialog;
import org.rm7370rf.estherproject.ui.dialog.AddTopicDialog;
import org.rm7370rf.estherproject.ui.dialog.BackupDialog;
import org.rm7370rf.estherproject.ui.dialog.SetUsernameDialog;
import org.rm7370rf.estherproject.ui.presenter.TopicListPresenter;
import org.rm7370rf.estherproject.ui.view.TopicListView;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;
import org.rm7370rf.estherproject.util.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;


import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends MvpAppCompatActivity implements TopicListView {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.topProgressBar)
    ProgressBar topProgressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.noDataText)
    TextView noDataText;

    private boolean hasUsername;
    private TopicsAdapter adapter;
    private RefreshAnimationUtil refreshAnimationUtil = new RefreshAnimationUtil();

    @InjectPresenter
    TopicListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);

        setTitle(topics);
        setSwipeRefreshLayout();
        setRecyclerAdapter();
        setRefreshAnimationUtil();
        presenter.updateDatabase();
    }

    @Override
    public void setHasUsername(boolean hasUsername) {
        this.hasUsername = hasUsername;
    }

    private void setSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> presenter.updateDatabase(RefreshType.BY_REQUEST)); //HOW??????
    }

    private void setRecyclerAdapter() {
        this.adapter = new TopicsAdapter();
        this.adapter.setListener(topicId -> {
            Intent intent = new Intent(this, TopicActivity.class);
            intent.putExtra(Keys.Extra.TOPIC_ID, String.valueOf(topicId));
            startActivity(intent);
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setRefreshAnimationUtil() {
        refreshAnimationUtil.setTopProgressBar(topProgressBar);
        refreshAnimationUtil.setProgressBar(progressBar);
        refreshAnimationUtil.setSwipeRefreshLayout(swipeRefreshLayout);
        refreshAnimationUtil.setRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic_list, menu);
        MenuItem setUsernameItem = menu.findItem(R.id.setUsername);
        setUsernameItem.setVisible(!hasUsername);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog = null;

        switch (item.getItemId()) {
            case R.id.addTopic:
                dialog = new AddTopicDialog(this);
                break;
            case R.id.accountData:
                AccountDialog d = new AccountDialog(this);
                d.show();
                break;
            case R.id.setUsername:
                dialog = new SetUsernameDialog(this);
                ((SetUsernameDialog) dialog).setOnCompleteListener(this::invalidateOptionsMenu);
                break;
            case R.id.backup:
                dialog = new BackupDialog(this);
                break;
            case R.id.logout:
//                realm.executeTransaction(r -> r.deleteAll());
//                finish();
                Toast.show(this, "NOT WORK");
                break;
        }

        if(dialog != null) {
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(int resource) {
        showToast(getString(resource));
    }

    @Override
    public void showToast(String message) {
        Toast.show(this, message);
    }

    @Override
    public void enableLoading(int refreshType) {
        refreshAnimationUtil.start(refreshType);
    }

    @Override
    public void disableLoading(int refreshType) {
        refreshAnimationUtil.stop(refreshType);
    }

    @Override
    public void setNoDataVisibility(int visibility) {
        noDataText.setVisibility(visibility);
    }
}
