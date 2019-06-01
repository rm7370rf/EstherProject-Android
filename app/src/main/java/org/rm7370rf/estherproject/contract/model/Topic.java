package org.rm7370rf.estherproject.contract.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Topic {
    private BigInteger id;
    private String subject;
    private String message;
    private String userAddress;
    private String userName;
    private BigInteger timestamp;
    private BigInteger numberOfPosts;
    private List<Post> posts = new ArrayList<>();

    public Topic() { }

    public void setId(BigInteger id) {
        this.id = id;
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
        this.timestamp = timestamp;
    }

    public void setNumberOfPosts(BigInteger numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public BigInteger getId() {
        return id;
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
        return timestamp;
    }

    public BigInteger getNumberOfPosts() {
        return numberOfPosts;
    }

    public boolean hasPosts() {
        return !posts.isEmpty();
    }

    public List<Post> getPosts() {
        return posts;
    }
}
