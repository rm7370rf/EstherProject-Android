package org.rm7370rf.estherproject.ui.presenter;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.expception.VerifierException;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.ui.view.TopicView;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Verifier;

import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import moxy.MvpPresenter;

import static org.rm7370rf.estherproject.other.Config.MAX_LIST_ITEM_TEXT_LENGTH;

public class TopicPresenter extends MvpPresenter<TopicView> {
    private Disposable disposable;
    private Realm realm = Realm.getDefaultInstance();
    private Contract contract = Contract.getInstance();
    private BigInteger topicId;
    private Topic topic;

    public TopicPresenter(BigInteger topicId) {
        this.topicId = topicId;
        try {
            setTopic();
            getViewState().setTitle(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));
            setFirstPost();
            updateDatabase((countPosts() == 0) ? RefreshAnimationUtil.RefreshType.FIRST : RefreshAnimationUtil.RefreshType.AFTER_START);
            setContent();
        }
        catch (Exception e) {
            getViewState().finish();
            e.printStackTrace();
        }
    }

    private void setTopic() throws VerifierException {
        Topic dbTopic = realm.where(Topic.class).equalTo(Keys.Db.ID, String.valueOf(topicId)).findFirst();
        Verifier.verifyRealmObject(dbTopic);
        this.topic = realm.copyFromRealm(dbTopic);
    }

    private void setContent() {
        RealmResults<Post> posts = getPostsQuery()
                .equalTo(Keys.Db.TOPIC_ID, String.valueOf(topic.getId()))
                .sort(Keys.Db.TIMESTAMP, Sort.ASCENDING)
                .findAll();
        getViewState().setRecyclerAdapter(posts);
    }

    private RealmQuery<Post> getPostsQuery() {
        return realm.where(Post.class);
    }

    private long countPosts() {
        return getPostsQuery().equalTo(Keys.Db.TOPIC_ID, String.valueOf(topic.getId())).count();
    }

    private void setFirstPost() {
        if(countPosts() == 0) {
            Post firstPost = new Post();
            firstPost.setTopicId(topic.getId());
            firstPost.setId(BigInteger.ZERO);
            firstPost.setMessage(topic.getMessage());
            firstPost.setUserAddress(topic.getUserAddress());
            firstPost.setUserName(topic.getUserName());
            firstPost.setTimestamp(topic.getTimestamp());
            firstPost.createPrimaryKey();
            realm.executeTransaction(r -> r.copyToRealm(firstPost));
        }
    }

    public void updateDatabase(int refreshType) {
        long amount = countPosts();

        disposable = Observable.create((ObservableEmitter<Post> emitter) -> {
            try {
                BigInteger numberOfPosts = contract.countPostsAtTopic(topic.getId());

                BigInteger localNumberOfPosts = BigInteger.valueOf(amount).subtract(BigInteger.ONE);

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
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            post -> realm.executeTransaction(r -> r.copyToRealm(post)),
            error -> {
                error.printStackTrace();
                getViewState().showToast(error.getLocalizedMessage());
            },
            () -> getViewState().disableLoading(refreshType),
            i -> getViewState().enableLoading(refreshType)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
