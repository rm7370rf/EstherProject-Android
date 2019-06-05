package org.rm7370rf.estherproject.model;

import java.math.BigInteger;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Topic extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String subject;
    @Required
    private String message;
    @Required
    private String userAddress;
    @Required
    private String userName;
    @Required
    private String timestamp;
    @Required
    private String numberOfPosts;

    public Topic() { }

    public void setId(BigInteger id) {
        this.id = String.valueOf(id);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = String.valueOf(timestamp);
    }

    public void setNumberOfPosts(BigInteger numberOfPosts) {
        this.numberOfPosts = String.valueOf(numberOfPosts);
    }

    public BigInteger getId() {
        return new BigInteger(id);
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public BigInteger getTimestamp() {
        return new BigInteger(timestamp);
    }

    public BigInteger getNumberOfPosts() {
        return new BigInteger(numberOfPosts);
    }
}
