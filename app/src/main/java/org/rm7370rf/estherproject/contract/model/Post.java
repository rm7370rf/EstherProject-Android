package org.rm7370rf.estherproject.contract.model;

import java.math.BigInteger;

public class Post {
    private final BigInteger id;
    private final String message;
    private final String userAddress;
    private final BigInteger timestamp;

    private Post(BigInteger id, String message, String userAddress, BigInteger timestamp) {
        this.id = id;
        this.message = message;
        this.userAddress = userAddress;
        this.timestamp = timestamp;
    }

    public Post create(BigInteger id, String message, String userAddress, BigInteger timestamp) {
        return new Post(id, message, userAddress, timestamp);
    }

    public BigInteger getId() {
        return id;
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
}
