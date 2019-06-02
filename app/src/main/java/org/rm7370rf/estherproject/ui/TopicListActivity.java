package org.rm7370rf.estherproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import static org.rm7370rf.estherproject.R.string.topics;

public class TopicListActivity extends AppCompatActivity {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Disposable disposable;
    private Contract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        ButterKnife.bind(this);
        setTitle(topics);

        Realm realm = Realm.getDefaultInstance();

        Account account = realm.where(Account.class).findFirst();
        this.contract = new Contract(realm.copyFromRealm(account));

        Log.d("ADDRESS", account.getWalletAddress());
        Log.d("FOLDER", account.getWalletFolder());
        Log.d("FILENAME", account.getWalletName());
        Log.d("BALANCE", "" + account.getBalance());

        refreshContent();
    }

    private void refreshContent() {
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
                            progressBar.setVisibility(View.GONE);

                        },
                        i -> {
                            Log.d("RF", "VISIBLE");
                            progressBar.setVisibility(View.VISIBLE);
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
