package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.contract.model.Topic;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.utils.Toast;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends AppCompatActivity {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Disposable disposable;
    private Contract contract;
    private TRVAdapter adapter;
    private Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        setTitle(topics);
        setContract();
        setSwipeRefreshLayout();
        setRecyclerAdapter();
        updateDB(false);
    }

    private void setContract() {
        Account account = realm.where(Account.class).findFirst();
        this.contract = new Contract(realm.copyFromRealm(account));
        Log.d("ADDRESS", account.getWalletAddress());
        Log.d("FOLDER", account.getWalletFolder());
        Log.d("FILENAME", account.getWalletName());
        Log.d("BALANCE", "" + account.getBalance());
    }

    private void setRecyclerAdapter() {
        this.adapter = new TRVAdapter(realm.where(Topic.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void setSwipeRefreshLayout() {
        this.swipeRefreshLayout.setOnRefreshListener(() -> updateDB(true));
    }

    private void updateDB(boolean bySwipe) {
        this.disposable = Observable.create((ObservableEmitter<Topic> emitter) -> {
            try {
                BigInteger numberOfTopics = contract.countTopics();
                long count = Realm.getDefaultInstance().where(Topic.class).count();
                Log.d("RF", "|" + count + "|" + numberOfTopics + "|");
                BigInteger localNumberOfTopics = BigInteger.valueOf(count);

                if (numberOfTopics.compareTo(localNumberOfTopics) > 0) {
                    for (BigInteger i = localNumberOfTopics; i.compareTo(numberOfTopics) < 0; i = i.add(BigInteger.ONE)) {
                        Topic topic = contract.getTopic(i);
                        emitter.onNext(topic);
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
                        topic -> {
                            Log.d("RF", "TopicId: " + (topic.getId()));
                            Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealm(topic));
                        },
                        error -> {
                            Log.d("RF", "EXCEPTION");
                            error.printStackTrace();
                            Toast.show(this, error.getLocalizedMessage());
                        },
                        () -> {
                            Log.d("RF", "GONE");
                            if(!bySwipe) {
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        },
                        i -> {
                            Log.d("RF", "VISIBLE");
                            if(!bySwipe) {
                                progressBar.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
             menu.setGroupVisible(R.id.group_normal_mode, true);
menu.setGroupVisible(R.id.group_delete_mode, false);
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topic_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accountData:
                Log.d("MENU", "ACCOUNT_DATA");
                return true;
            case R.id.setUsername:
                Log.d("MENU", "SET_USERNAME");
                return true;
            case R.id.logout:
                Log.d("MENU", "LOGOUT");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
