package org.rm7370rf.estherproject.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.contract.Contract;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Utils;

import java.math.BigDecimal;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class AccountDialog extends Dialog {
    private TextView userBalanceText,
                     userNameLabel,
                     userNameText,
                     userAddressText;

    private ImageButton qrCode;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private Disposable disposable;
    private Contract contract = Contract.getInstance();

    public AccountDialog(Activity activity) {
        super(activity);
        setLayout(R.layout.dialog_account_data);
        setUI();
    }

    private void setUI() {
        userBalanceText = getView().findViewById(R.id.userBalance);
        userNameLabel = getView().findViewById(R.id.userNameLabel);
        userNameText = getView().findViewById(R.id.userName);
        userAddressText = getView().findViewById(R.id.userAddressText);
        qrCode = getView().findViewById(R.id.qrCodeImage);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        progressBar = getView().findViewById(R.id.progressBar);
    }

    @Override
    public AlertDialog show() {
        prepareView();
        return super.show();
    }

    private void prepareView() {
        try {
            Account account = Account.get();
            View.OnClickListener copyToClipboardListener = (v) -> copyToClipboard(getContext(), account.getWalletAddress());
            userAddressText.setOnClickListener(copyToClipboardListener);
            qrCode.setOnClickListener(copyToClipboardListener);

            String address = account.getWalletAddress();
            int size = getActivity().getResources().getDisplayMetrics().widthPixels / 2;
            qrCode.setImageBitmap(Utils.createQrCode(address, size, size));

            userBalanceText.setText(String.valueOf(account.getBalance()));

            int userNameVisibility = account.hasUsername() ? View.VISIBLE : View.GONE;
            userNameLabel.setVisibility(userNameVisibility);
            userNameText.setVisibility(userNameVisibility);

            if (account.hasUsername()) {
                userNameText.setText(account.getUserName());
            }

            userAddressText.setText(account.getWalletAddress());

            swipeRefreshLayout.setOnRefreshListener(() -> refreshUserData(true));

            refreshUserData(false);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.show(getActivity(), e.getLocalizedMessage());
        }
    }

    private void refreshUserData(boolean bySwipe) {
        Single<BigDecimal> single = Single.fromCallable(() -> contract.getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        this.disposable = single.subscribeWith(new DisposableSingleObserver<BigDecimal>() {
            @Override
            protected void onStart() {
                super.onStart();
                if (!bySwipe) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(BigDecimal balance) {
                userBalanceText.setText(String.valueOf(balance));
                onComplete();
            }

            @Override
            public void onError(Throwable e) {
                Toast.show(getView().getContext(), e.getLocalizedMessage());
                onComplete();
            }

            private void onComplete() {
                if (!bySwipe) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        disposable.dispose();
    }
}
