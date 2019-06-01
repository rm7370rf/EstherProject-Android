package org.rm7370rf.estherproject.contract;

import org.rm7370rf.estherproject.contract.model.Post;
import org.rm7370rf.estherproject.contract.model.Topic;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class Contract {
    private final String node;

    public Contract(String node) {
        super();
        this.node = node;
    }

    public void addTopic(String subject, String message) {

    }

    public void addPostToTopic(BigInteger topicId, String message) {

    }

    public Topic getTopic(BigInteger topicId) {
        return null;
    }

    public Post getPostAtTopic(BigInteger topicId, BigInteger postId) {
        return null;
    }

    public BigInteger countPostsAtTopic(BigInteger topicId) {
        return null;
    }

    public BigInteger countTopics() {
        return null;
    }
}
