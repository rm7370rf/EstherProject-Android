package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.contract.model.Topic;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.utils.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

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

    private CompositeDisposable disposables = new CompositeDisposable();
    private Contract contract;
    private TRVAdapter adapter;
    private Realm realm = Realm.getDefaultInstance();
    private Account account;

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
        this.account = realm.where(Account.class).findFirst();
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
        disposables.add(
                Observable.create((ObservableEmitter<Topic> emitter) -> {
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
        inflater.inflate(R.menu.menu_topic_list, menu);

        boolean hasUsername = account.hasUsername();

        MenuItem setUsernameItem = menu.findItem(R.id.setUsername);
        setUsernameItem.setVisible(!hasUsername);
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
                showAccountDataDialog();
                return true;
            case R.id.setUsername:
                Log.d("MENU", "SET_USERNAME");
                showSetUsernameDialog();
                return true;
            case R.id.logout:
                Log.d("MENU", "LOGOUT");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAccountDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_account_data, null);

        TextView userBalanceText = view.findViewById(R.id.userBalance);
        TextView userNameLabel = view.findViewById(R.id.userNameLabel);
        TextView userNameText = view.findViewById(R.id.userName);
        TextView userAddressText = view.findViewById(R.id.userAddress);

        userBalanceText.setText(String.valueOf(account.getBalance()));
        int userNameVisibility = account.hasUsername() ? View.VISIBLE : View.GONE;
        userNameLabel.setVisibility(userNameVisibility);
        userNameText.setVisibility(userNameVisibility);

        if(!account.hasUsername()) {
            userNameText.setText(account.getUserName());
        }

        userAddressText.setText(account.getWalletAddress());

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshUserData(view, userBalanceText, swipeRefreshLayout, true));

        refreshUserData(view, userBalanceText, swipeRefreshLayout, false);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void refreshUserData(View view, TextView userBalanceText, SwipeRefreshLayout swipeRefreshLayout, boolean bySwipe) {
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        Single<BigDecimal> single = Single.fromCallable(() -> contract.getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        disposables.add(
                single.subscribeWith(new DisposableSingleObserver<BigDecimal>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if(!bySwipe) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onSuccess(BigDecimal balance) {
                        userBalanceText.setText(String.valueOf(balance));
                        onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.show(view.getContext(), e.getLocalizedMessage());
                        onComplete();
                    }

                    private void onComplete() {
                        if(!bySwipe) {
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                })
        );
    }

    private void showSetUsernameDialog() {

    }
}
