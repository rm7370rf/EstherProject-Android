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
import org.rm7370rf.estherproject.model.Topic;
import org.rm7370rf.estherproject.ui.presenter.AccountDataPresenter;
import org.rm7370rf.estherproject.ui.view.AccountDataView;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.RefreshAnimationUtil;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Utils;

import java.math.BigDecimal;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class AccountDataDialog extends Dialog implements AccountDataView {
    @InjectPresenter
    AccountDataPresenter presenter;

    private TextView userBalanceText,
                     userNameLabel,
                     userNameText,
                     userAddressText;

    private ImageButton qrCode;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;


    private RefreshAnimationUtil refreshAnimationUtil = new RefreshAnimationUtil();

    public AccountDataDialog() {
        super();
        setLayout(R.layout.dialog_account_data);
        setUI();
        setRefreshAnimationUtil();
    }

    private void setUI() {
        userBalanceText = getView().findViewById(R.id.userBalance);
        userNameLabel = getView().findViewById(R.id.userNameLabel);
        userNameText = getView().findViewById(R.id.userName);
        userAddressText = getView().findViewById(R.id.userAddressText);
        qrCode = getView().findViewById(R.id.qrCodeImage);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshUserData(true));
    }

    public void setRefreshAnimationUtil() {
        progressBar = getView().findViewById(R.id.progressBar);

        refreshAnimationUtil.setTopProgressBar(progressBar);
        refreshAnimationUtil.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    public void prepareView(Account account) {
        try {

            View.OnClickListener copyToClipboardListener = (v) -> copyToClipboard(getContext(), account.getWalletAddress());

            userAddressText.setOnClickListener(copyToClipboardListener);
            qrCode.setOnClickListener(copyToClipboardListener);

            String address = account.getWalletAddress();
            int size = getActivity().getResources().getDisplayMetrics().widthPixels / 2;
            qrCode.setImageBitmap(Utils.createQrCode(address, size, size));

            int userNameVisibility = account.hasUsername() ? View.VISIBLE : View.GONE;
            userNameLabel.setVisibility(userNameVisibility);
            userNameText.setVisibility(userNameVisibility);

            if (account.hasUsername()) {
                userNameText.setText(account.getUserName());
            }

            userAddressText.setText(account.getWalletAddress());

            presenter.refreshUserData(false);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.show(getActivity(), e.getLocalizedMessage());
        }
    }

    @Override
    public void setBalance(String balance) {
        userBalanceText.setText(balance);
    }

    @Override
    public void showToast(String message) {
        Toast.show(getContext(), message);
    }

    @Override
    public void enabledLoading(boolean bySwipe) {
        if (!bySwipe) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableLoading(boolean bySwipe) {
        if (!bySwipe) {
            progressBar.setVisibility(View.GONE);

        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
