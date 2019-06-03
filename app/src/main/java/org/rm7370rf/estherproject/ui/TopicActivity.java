package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.utils.Toast;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static org.rm7370rf.estherproject.R.string.invalid_topic_id;
import static org.rm7370rf.estherproject.utils.Config.MAX_LIST_ITEM_TEXT_LENGTH;
import static org.rm7370rf.estherproject.utils.Config.TOPIC_ID_KEY;

public class TopicActivity extends AppCompatActivity {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CompositeDisposable disposables = new CompositeDisposable();
    private Contract contract;
    private Realm realm = Realm.getDefaultInstance();
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        setTopicId();

        setTitle(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));

        setContract();
        setSwipeRefreshLayout();
        setRecyclerAdapter();
        setFirstPost();
        updateDB(false);
    }

    private long countTopicsAtDb() {
        return Realm.getDefaultInstance().where(Post.class).equalTo("topicId", String.valueOf(topic.getId())).count();
    }

    private void setFirstPost() {
        if(countTopicsAtDb() == 0) {
            Post firstPost = new Post();
            firstPost.setTopicId(topic.getId());
            firstPost.setId(BigInteger.ZERO);
            firstPost.setMessage(topic.getMessage());
            firstPost.setUserAddress(topic.getUserAddress());
            firstPost.setUserName(topic.getUserName());
            firstPost.setTimestamp(topic.getTimestamp());
            realm.executeTransaction(r -> r.copyToRealm(firstPost));
        }
    }

    private void setTopicId() {
        String topicId = getIntent().getStringExtra(TOPIC_ID_KEY);
        if(topicId == null) {
            Toast.show(this, invalid_topic_id);
            finish();
        }
        else {
            Topic dbTopic = realm.where(Topic.class).equalTo("id", topicId).findFirst();
            this.topic = realm.copyFromRealm(dbTopic);
        }
    }

    private void setContract() {
        Account account = realm.where(Account.class).findFirst();
        this.contract = new Contract(realm.copyFromRealm(account));
    }

    private void setRecyclerAdapter() {
        TopicAdapter adapter = new TopicAdapter(realm.where(Post.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void setSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> updateDB(true));
    }

    private void updateDB(boolean bySwipe) {
        disposables.add(
                Observable.create((ObservableEmitter<Post> emitter) -> {
                    try {
                        BigInteger numberOfPosts = contract.countPostsAtTopic(topic.getId());
                        long count = countTopicsAtDb();

                        BigInteger localNumberOfPosts = BigInteger.valueOf(count);

                        if (numberOfPosts.compareTo(localNumberOfPosts) > 0) {
                            for (BigInteger i = localNumberOfPosts; i.compareTo(numberOfPosts) < 0; i = i.add(BigInteger.ONE)) {
                                Post post = contract.getPostAtTopic(topic.getId(), i);
                                emitter.onNext(post);
                            }
                        }
                        emitter.onComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                post -> {
                                    Log.d("NEW POST", String.valueOf(post.getId()));
                                    realm.executeTransaction(r -> r.copyToRealm(post));
                                },
                                error -> {
                                    error.printStackTrace();
                                    Toast.show(this, error.getLocalizedMessage());
                                },
                                () -> {
                                    if(!bySwipe) {
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }

                                },
                                i -> {
                                    if(!bySwipe) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }
                        )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPost:
//                showAddTopicDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
