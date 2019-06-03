package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.contract.model.Topic;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.utils.FieldDialog;
import org.rm7370rf.estherproject.utils.Utils;
import org.rm7370rf.estherproject.utils.Toast;
import org.rm7370rf.estherproject.utils.Verifier;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.Sort;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekalips.fancybuttonproj.FancyButton;

import static org.rm7370rf.estherproject.R.string.load;
import static org.rm7370rf.estherproject.R.string.please_backup_private_key;
import static org.rm7370rf.estherproject.R.string.request_successfully_sent;
import static org.rm7370rf.estherproject.R.string.send;
import static org.rm7370rf.estherproject.R.string.topics;
import static org.rm7370rf.estherproject.R.string.username_already_exists;

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
        this.adapter = new TRVAdapter(realm.where(Topic.class).findAll().sort("id", Sort.DESCENDING));
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
            case R.id.addTopic:
                showAddTopicDialog();
                return true;
            case R.id.accountData:
                Log.d("MENU", "ACCOUNT_DATA");
                try {
                    showAccountDataDialog();
                }
                catch (Exception e) {
                    Toast.show(this, e.getLocalizedMessage());
                }
                return true;
            case R.id.setUsername:
                Log.d("MENU", "SET_USERNAME");
                showSetUsernameDialog();
                return true;
            case R.id.backup:
                showBackupDialog();
                return true;
            case R.id.logout:
                realm.delete(Account.class);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddTopicDialog() {
        FieldDialog dialog = new FieldDialog(this);
        dialog.setLayout(R.layout.dialog_add_topic);

        EditText subjectEdit = dialog.getEditText(R.id.subjectEdit);
        EditText messageEdit = dialog.getEditText(R.id.messageEdit);
        EditText passwordEdit = dialog.getEditText(R.id.passwordEdit);

        dialog.setOnClickListener(
                send,
                button -> disposables.add(
                        Completable.fromAction(() -> {
                            String subject = subjectEdit.getText().toString();
                            String message = messageEdit.getText().toString();
                            String password = passwordEdit.getText().toString();
                            Verifier.verifySubject(this, subject);
                            Verifier.verifyMessage(this, message);
                            Verifier.verifyPassword(this, password);
                            contract.addTopic(password, subject, message);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            protected void onStart() {
                                button.collapse();
                            }

                            @Override
                            public void onComplete() {
                                Toast.show(dialog.getContext(), request_successfully_sent);
                                button.expand();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Toast.show(dialog.getContext(), e.getLocalizedMessage());
                                button.expand();
                            }
                        })
                )
        );
        dialog.show();
    }

    private void showBackupDialog() {
        FieldDialog dialog = new FieldDialog(this);
        dialog.setLayout(R.layout.dialog_backup);

        TextView privateKeyText = dialog.getTextView(R.id.privateKeyText);
        EditText passwordEdit = dialog.getEditText(R.id.passwordEdit);

        dialog.setOnClickListener(
                load,
                button -> disposables.add(
                        Single.fromCallable(() -> {
                            String password = passwordEdit.getText().toString();
                            Verifier.verifyPassword(this, password);
                            Credentials credentials = contract.getCredentials(password);
                            return credentials.getEcKeyPair().getPrivateKey().toString(16);
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    protected void onStart() {
                        button.collapse();
                    }

                    @Override
                    public void onSuccess(String privateKey) {
                        privateKeyText.setText(privateKey);
                        privateKeyText.setVisibility(View.VISIBLE);
                        privateKeyText.setOnClickListener(v -> Utils.copyToClipboard(dialog.getContext(), privateKey));
                        Toast.show(dialog.getContext(), please_backup_private_key);
                        button.expand();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.show(dialog.getContext(), e.getLocalizedMessage());
                        button.expand();
                    }
                }))
        );
        dialog.show();
    }

    private void showAccountDataDialog() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_account_data, null);

        TextView userBalanceText = view.findViewById(R.id.userBalance);
        TextView userNameLabel = view.findViewById(R.id.userNameLabel);
        TextView userNameText = view.findViewById(R.id.userName);
        TextView userAddressText = view.findViewById(R.id.userAddress);
        ImageButton qrCode = view.findViewById(R.id.qrCodeImage);

        userAddressText.setOnClickListener(v -> Utils.copyToClipboard(this, account.getWalletAddress()));
        qrCode.setOnClickListener(v -> Utils.copyToClipboard(this, account.getWalletAddress()));

        String address = account.getWalletAddress();

        int size = getResources().getDisplayMetrics().widthPixels/2;
        qrCode.setImageBitmap(Utils.createQrCode(address, size, size));

        userBalanceText.setText(String.valueOf(account.getBalance()));

        int userNameVisibility = account.hasUsername() ? View.VISIBLE : View.GONE;
        userNameLabel.setVisibility(userNameVisibility);
        userNameText.setVisibility(userNameVisibility);

        if (!account.hasUsername()) {
            userNameText.setText(address);
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
        if(!account.hasUsername()) {
            FieldDialog dialog = new FieldDialog(this);
            dialog.setLayout(R.layout.dialog_set_username);

            EditText userNameEdit = dialog.getEditText(R.id.userNameEdit);
            EditText passwordEdit = dialog.getEditText(R.id.passwordEdit);

            dialog.setOnClickListener(
                    send,
                    button -> {
                try {
                    String userName = userNameEdit.getText().toString();
                    String password = passwordEdit.getText().toString();

                    Verifier.verifyUserName(this, userName);
                    Verifier.verifyPassword(this, password);

                    disposables.add(
                            Completable.fromAction(() -> contract.setUsername(password, userName))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableCompletableObserver() {
                                @Override
                                protected void onStart() {
                                    super.onStart();
                                    button.collapse();
                                }

                                @Override
                                public void onComplete() {
                                    button.expand();
                                    dialog.hide();
                                    realm.executeTransaction(realm -> account.setUserName(userName));

                                    Toast.show(dialog.getContext(), request_successfully_sent);
                                    invalidateOptionsMenu();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    button.expand();
                                    e.printStackTrace();
                                    Toast.show(dialog.getContext(), e.getLocalizedMessage());
                                }
                            })
                    );
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.show(this, e.getLocalizedMessage());
                }
            });
            dialog.show();
        }
        else {
            Toast.show(this, username_already_exists);
        }
    }
}
