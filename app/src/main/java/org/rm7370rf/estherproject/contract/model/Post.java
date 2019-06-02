package org.rm7370rf.estherproject.contract.model;

import java.math.BigInteger;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Post extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String topicsId;
    @Required
    private String message;
    @Required
    private String userAddress;
    @Required
    private String userName;
    @Required
    private String timestamp;

    public Post() { }

    public void setId(BigInteger id) {
        this.id = String.valueOf(id);
    }

    public void setTopicsId(BigInteger topicsId) {
        this.topicsId = String.valueOf(topicsId);
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

    public BigInteger getId() {
        return new BigInteger(id);
    }

    public BigInteger getTopicsId() {
        return new BigInteger(topicsId);
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
}
