package org.rm7370rf.estherproject.contract.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Topic {
    private final BigInteger id;
    private final String subject;
    private final String message;
    private final String userAddress;
    private final BigInteger timestamp;
    private final BigInteger numberOfPosts;
    private final List<Post> posts;

    private Topic(BigInteger id, String subject, String message, String userAddress, BigInteger timestamp, BigInteger numberOfPosts, List<Post> posts) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.userAddress = userAddress;
        this.timestamp = timestamp;
        this.numberOfPosts = numberOfPosts;
        this.posts = posts;
    }

    public static Topic create(BigInteger id, String subject, String message, String userAddress, BigInteger timestamp, BigInteger numberOfPosts) {
        return new Topic(id, subject, message, userAddress, timestamp, numberOfPosts, new ArrayList<>());
    }

    public static Topic create(BigInteger id, String subject, String message, String userAddress, BigInteger timestamp, BigInteger numberOfPosts, List<Post> posts) {
        return new Topic(id, subject, message, userAddress, timestamp, numberOfPosts, posts);
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
