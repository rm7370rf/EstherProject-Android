package org.rm7370rf.estherproject.contract.model;

import java.math.BigInteger;

public class Post {
    private BigInteger id;
    private String message;
    private String userAddress;
    private BigInteger timestamp;

    public Post() { }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
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
