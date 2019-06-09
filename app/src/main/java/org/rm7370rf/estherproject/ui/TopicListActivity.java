package org.rm7370rf.estherproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.ui.adapter.TopicsAdapter;
import org.rm7370rf.estherproject.ui.dialog.AccountDialog;
import org.rm7370rf.estherproject.ui.dialog.AddTopicDialog;
import org.rm7370rf.estherproject.ui.dialog.BackupDialog;
import org.rm7370rf.estherproject.ui.dialog.SetUsernameDialog;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.FieldDialog;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil.RefreshType;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.Sort;

import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends AppCompatActivity {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.topProgressBar)
    ProgressBar topProgressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CompositeDisposable disposables = new CompositeDisposable();
    private Contract contract;
    private TopicsAdapter adapter;
    private Realm realm = Realm.getDefaultInstance();
    private Account account;
    private RefreshAnimationUtil refreshAnimationUtil = new RefreshAnimationUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        try {
            setTitle(topics);
            setContract();
            setSwipeRefreshLayout();
            setRecyclerAdapter();
            setRefreshAnimationUtil();
            updateDB((countTopics() == 0) ? RefreshType.FIRST : RefreshType.AFTER_START);
        }
        catch (Exception e) {
            Toast.show(this, e.getLocalizedMessage());
            finish();
        }
    }

    private void setContract() throws VerifierException {
        this.account = Account.get(this);
        this.contract = Contract.getInstance()
                                .setAccount(realm.copyFromRealm(account));
    }

    private void setSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> updateDB(RefreshType.BY_REQUEST));
    }

    private void setRecyclerAdapter() {
        this.adapter = new TopicsAdapter(
                realm.where(Topic.class).findAll().sort(Keys.Db.ID, Sort.DESCENDING)
        );
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

    private long countTopics() {
        return realm.where(Topic.class).count();
    }

    private void updateDB(int refreshType) {
        long amount = countTopics();
        disposables.add(
                Observable.create((ObservableEmitter<Topic> emitter) -> {
                    try {
                        BigInteger numberOfTopics = contract.countTopics();
                        BigInteger localNumberOfTopics = BigInteger.valueOf(amount);

                        if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
                            for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                                Topic topic = contract.getTopic(i);
                                emitter.onNext(topic);
                            }
                        }
                        emitter.onComplete();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                topic -> realm.executeTransaction(r -> r.copyToRealm(topic)),
                                error -> Toast.show(this, error.getLocalizedMessage()),
                                () -> {
                                    refreshAnimationUtil.stop(refreshType);
                                    findViewById(R.id.noDataText).setVisibility((countTopics() == 0) ? View.VISIBLE : View.GONE);
                                },
                                i -> refreshAnimationUtil.start(refreshType)
                        )
        );
    }

    @Override
    protected void onDestroy() {
        if(!disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic_list, menu);

        boolean hasUsername = account.hasUsername();

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
                realm.executeTransaction(r -> r.deleteAll());
                finish();
                break;
        }

        if(dialog != null) {
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
