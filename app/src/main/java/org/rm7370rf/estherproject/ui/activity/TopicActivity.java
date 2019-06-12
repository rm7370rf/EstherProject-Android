package org.rm7370rf.estherproject.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.ui.adapter.TopicAdapter;
import org.rm7370rf.estherproject.ui.dialog.AddPostDialog;
import org.rm7370rf.estherproject.ui.presenter.TopicPresenter;
import org.rm7370rf.estherproject.ui.view.TopicView;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.other.Config.MAX_LIST_ITEM_TEXT_LENGTH;

public class TopicActivity extends MvpAppCompatActivity implements TopicView {
    @InjectPresenter
    TopicPresenter presenter;

    @ProvidePresenter
    TopicPresenter provideDetailsPresenter() {
        try {
            String topicId = getIntent().getStringExtra(Keys.Extra.TOPIC_ID);
            Verifier.verifyIntentExtra(topicId);
            return new TopicPresenter(new BigInteger(topicId));
        }
        catch (Exception e) {
            finish();
        }
        return null;
    }

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.topProgressBar)
    ProgressBar topProgressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RefreshAnimationUtil refreshAnimationUtil = new RefreshAnimationUtil();
    private BigInteger topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        setSwipeRefreshLayout();
        setRefreshAnimationUtil();
        setBackButton();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setRefreshAnimationUtil() {
        refreshAnimationUtil.setTopProgressBar(topProgressBar);
        refreshAnimationUtil.setProgressBar(progressBar);
        refreshAnimationUtil.setSwipeRefreshLayout(swipeRefreshLayout);
        refreshAnimationUtil.setRecyclerView(recyclerView);
    }

    private void setBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setRecyclerAdapter(OrderedRealmCollection<Post> posts) {
        TopicAdapter adapter = new TopicAdapter(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void setSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> presenter.updateDatabase(RefreshType.BY_REQUEST));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.addPost:
                AddPostDialog dialog = new AddPostDialog();
                dialog.setTopicId(topicId);
                dialog.show(getSupportFragmentManager());
                return true;
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
    public void finish() {
        super.finish();
    }
}
