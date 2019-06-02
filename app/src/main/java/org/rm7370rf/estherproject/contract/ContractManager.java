package org.rm7370rf.estherproject.contract;

import org.rm7370rf.estherproject.model.Account;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static org.rm7370rf.estherproject.utils.Config.CONTRACT_ADDRESS;
import static org.rm7370rf.estherproject.utils.Config.NODE;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class ContractManager {
    private final Web3j web3j;
    private final Account account;

    public ContractManager(Account account) {
        this.web3j = Web3j.build(new HttpService(NODE));
        this.account = account;
    }

    public String callFunction(Function function) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(account.getWalletAddress(), CONTRACT_ADDRESS, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();

        return response.getValue();
    }

    public String executeFunction(String password, Function function) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(password, account.getWalletFolder() + account.getWalletName());
        BigInteger nonce = getNonce(credentials.getAddress());

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                CONTRACT_ADDRESS,
                encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue)
                .sendAsync().get();

        return transactionResponse.getTransactionHash();
    }

    private BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();
    }
}
