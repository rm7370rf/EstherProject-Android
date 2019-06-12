package org.rm7370rf.estherproject.contract;

import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.model.Topic;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Contract extends ContractManager {
    public Contract() { }

    @Override
    public Contract setAccount(Account account) {
        super.setAccount(account);
        return this;
    }

    public String setUsername(String password, String userName) throws Exception {
        Function function = new Function(
                "setUserName",
                Collections.singletonList(
                        new Utf8String(userName)
                ),
                Collections.emptyList()
        );
        return executeFunction(password, function);
    }

    public String addTopic(String password, String subject, String message) throws Exception {
        Function function = new Function(
                "addTopic",
                Arrays.asList(
                        new Utf8String(subject),
                        new Utf8String(message)
                ),
                Collections.emptyList()
        );
        return executeFunction(password, function);
    }

    public String addPostToTopic(String password, BigInteger topicId, String message) throws Exception {
        Function function = new Function(
                "addPostToTopic",
                            Arrays.asList(
                                new Uint256(topicId),
                                new Utf8String(message)
                            ),
                Collections.emptyList()
        );
        return executeFunction(password, function);
    }

    public String getUsername(String userAddress) throws Exception {
        Function function = new Function(
                "getUserName",
                Collections.singletonList(
                        new Address(userAddress)
                ),
                Collections.singletonList(
                        new TypeReference<Utf8String>() {}
                )
        );

        String responseValue = callFunction(function);
        List<Type> response = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

        return (String) response.get(0).getValue();
    }

    public Topic getTopic(BigInteger topicId) throws Exception {
        Function function = new Function(
                "getTopic",
                Collections.singletonList(new Uint256(topicId)),
                Arrays.asList(
                        new TypeReference<Uint256>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Uint256>() {}
                )
        );

        String responseValue = callFunction(function);
        List<Type> response = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

        Topic topic = new Topic();
        topic.setId((BigInteger) response.get(0).getValue());
        topic.setSubject((String) response.get(1).getValue());
        topic.setMessage((String) response.get(2).getValue());
        topic.setUserAddress((String) response.get(3).getValue());
        topic.setUserName((String) response.get(4).getValue());
        topic.setTimestamp((BigInteger) response.get(5).getValue());
        topic.setNumberOfPosts((BigInteger) response.get(6).getValue());

        return topic;
    }

    public Post getPostAtTopic(BigInteger topicId, BigInteger postId) throws Exception {
        Function function = new Function(
                "getPostAtTopic",
                Arrays.asList(
                        new Uint256(topicId),
                        new Uint256(postId)
                ),
                Arrays.asList(
                        new TypeReference<Uint256>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Uint256>() {}
                )
        );

        String responseValue = callFunction(function);
        List<Type> response = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

        Post post = new Post();
        post.setId(((BigInteger) response.get(0).getValue()).add(BigInteger.ONE));
        post.setTopicId(topicId);
        post.setMessage((String) response.get(1).getValue());
        post.setUserAddress((String) response.get(2).getValue());
        post.setUserName((String) response.get(3).getValue());
        post.setTimestamp((BigInteger) response.get(4).getValue());
        post.createPrimaryKey();
        return post;
    }

    public BigInteger countPostsAtTopic(BigInteger topicId) throws Exception {
        Function function = new Function(
                "countPostsAtTopic",
                Collections.singletonList(
                        new Uint256(topicId)
                ),
                Collections.singletonList(
                        new TypeReference<Uint256>() {}
                )
        );

        String responseValue = callFunction(function);
        List<Type> response = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

        return (BigInteger) response.get(0).getValue();
    }

    public BigInteger countTopics() throws Exception {
        Function function = new Function(
                "countTopics",
                Collections.emptyList(),
                Collections.singletonList(
                        new TypeReference<Uint256>() {}
                )
        );

        String responseValue = callFunction(function);
        List<Type> response = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());

        return (BigInteger) response.get(0).getValue();
    }
}
