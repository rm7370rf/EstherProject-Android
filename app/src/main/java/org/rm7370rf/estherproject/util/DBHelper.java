package org.rm7370rf.estherproject.util;

import org.apache.commons.lang3.concurrent.Computable;
import org.rm7370rf.estherproject.EstherProject;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;

import java.math.BigInteger;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DBHelper {
    public DBHelper() {
        EstherProject.getComponent().inject(this);
    }

    public long countTopics() {
        try (Realm realm = Realm.getDefaultInstance()) {
            return realm.where(Topic.class).count();
        }
    }

    public long countPosts(BigInteger topicId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            return realm.where(Post.class)
                    .equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId))
                    .count();
        }
    }

    public Topic getTopic(BigInteger topicId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            return realm.where(Topic.class).equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId)).findFirst();
        }
    }

    public RealmResults<Post> getPosts(BigInteger topicId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            return realm.where(Post.class)
                    .equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId))
                    .sort(Keys.Db.TIMESTAMP, Sort.ASCENDING)
                    .findAll();
        }
    }
}
