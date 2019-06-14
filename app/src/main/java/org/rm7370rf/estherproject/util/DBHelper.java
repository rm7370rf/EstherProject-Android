package org.rm7370rf.estherproject.util;

import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.other.Keys;

import java.math.BigInteger;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DBHelper {
    @Inject
    Realm realm;

    public void executeTransaction(Realm.Transaction transaction) {
        realm.executeTransaction(transaction);
    }

    public long countTopics() {
        return realm.where(Topic.class).count();
    }

    public long countPosts(BigInteger topicId) {
        return realm.where(Post.class)
                .equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId))
                .count();
    }

    public Topic getTopic(BigInteger topicId) {
        return realm.where(Topic.class).equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId)).findFirst();
    }

    public RealmResults<Post> getPosts(BigInteger topicId) {
        return realm.where(Post.class)
                .equalTo(Keys.Db.TOPIC_ID, String.valueOf(topicId))
                .sort(Keys.Db.TIMESTAMP, Sort.ASCENDING)
                .findAll();
    }
}
