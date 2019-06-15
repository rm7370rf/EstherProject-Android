package org.rm7370rf.estherproject.contract;

import org.rm7370rf.estherproject.model.Account;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.rm7370rf.estherproject.other.Config.CONTRACT_ADDRESS;
import static org.rm7370rf.estherproject.other.Config.NODE;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class ContractManager {
    private final Web3j web3j;
    private Account account;

    protected ContractManager() {
        this.web3j = Web3j.build(new HttpService(NODE));
    }

    public boolean isNullAccount() {
        return (account == null);
    }

    public ContractManager setAccount(Account account) {
        if(this.account == null) {
            this.account = account;
        }
        return this;
    }

    public BigDecimal getBalance() throws Exception {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(account.getWalletAddress(), DefaultBlockParameterName.LATEST).send();
        return Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
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
        Credentials credentials = getCredentials(password);

        BigInteger nonce = getNonce(credentials.getAddress());

        String encodedFunction = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createEthCallTransaction(credentials.getAddress(), CONTRACT_ADDRESS.toLowerCase(), encodedFunction);
        EthEstimateGas estimateGas = web3j.ethEstimateGas(transaction).send();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                GAS_PRICE,
                estimateGas.getAmountUsed(),
                CONTRACT_ADDRESS,
                encodedFunction
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return transactionResponse.getTransactionHash();
    }

    private BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        return ethGetTransactionCount.getTransactionCount();
    }

    public Credentials getCredentials(String password) throws CipherException, IOException {
        return WalletUtils.loadCredentials(password, account.getWalletFolder() + account.getWalletName());
    }
}
