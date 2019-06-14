package org.rm7370rf.estherproject.ui.presenter;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.ui.view.TopicView;
import org.rm7370rf.estherproject.util.DBHelper;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import moxy.InjectViewState;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.other.Config.MAX_LIST_ITEM_TEXT_LENGTH;

@InjectViewState
public class TopicPresenter extends MvpPresenter<TopicView> {
    private Disposable disposable;
    private Realm realm = Realm.getDefaultInstance();

    @Inject
    Contract contract;

    @Inject
    DBHelper dbHelper;

    private BigInteger topicId;
    private Topic topic;

    public TopicPresenter(BigInteger topicId) {
        EstherProject.getComponent().inject(this);
        this.topicId = topicId;
        try {
            setTopic();
            getViewState().setTitle(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));
            setFirstPost();
            setContent();
        }
        catch (Exception e) {
            getViewState().finish();
            e.printStackTrace();
        }
    }

    private void setTopic() throws VerifierException {
        Topic dbTopic = dbHelper.getTopic(topicId);
        Verifier.verifyRealmObject(dbTopic);
        this.topic = realm.copyFromRealm(dbTopic);
    }

    private void setContent() {
        getViewState().setRecyclerAdapter(dbHelper.getPosts(topicId));
    }

    private void onStartUpdate() {
        updateDatabase((dbHelper.countPosts(topicId) == 0) ? RefreshAnimationUtil.RefreshType.FIRST : RefreshAnimationUtil.RefreshType.AFTER_START);
    }

    private void setFirstPost() {
        if(dbHelper.countPosts(topicId) == 0) {
            Post firstPost = new Post();
            firstPost.setTopicId(topic.getId());
            firstPost.setId(BigInteger.ZERO);
            firstPost.setMessage(topic.getMessage());
            firstPost.setUserAddress(topic.getUserAddress());
            firstPost.setUserName(topic.getUserName());
            firstPost.setTimestamp(topic.getTimestamp());
            firstPost.createPrimaryKey();
            try(Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransactionAsync(
                        r -> r.copyToRealm(firstPost),
                        this::onStartUpdate
                );
            }
        }
    }

    public void updateDatabase(RefreshAnimationUtil.RefreshType refreshType) {
        long amount = dbHelper.countPosts(topicId);

        disposable = Completable.fromAction(() -> {

                BigInteger numberOfPosts = contract.countPostsAtTopic(topic.getId());

                BigInteger localNumberOfPosts = BigInteger.valueOf(amount).subtract(BigInteger.ONE);

                if (numberOfPosts.compareTo(localNumberOfPosts) > 0) {
                    for (BigInteger i = localNumberOfPosts; i.compareTo(numberOfPosts) < 0; i = i.add(BigInteger.ONE)) {
                        Post post = contract.getPostAtTopic(topic.getId(), i);
                        try(Realm realm = Realm.getDefaultInstance()) {
                            realm.executeTransaction(r -> r.copyToRealm(post));
                        }
                    }
                }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(
                new DisposableCompletableObserver() {
                    @Override
                    protected void onStart() {
                        getViewState().enableLoading(refreshType);
                    }

                    @Override
                    public void onComplete() {
                        getViewState().disableLoading(refreshType);

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showToast(e);
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
