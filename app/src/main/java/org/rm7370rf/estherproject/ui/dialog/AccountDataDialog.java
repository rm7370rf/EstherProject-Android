package org.rm7370rf.estherproject.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.model.Account;
import org.rm7370rf.estherproject.ui.presenter.AccountDataPresenter;
import org.rm7370rf.estherproject.ui.view.AccountDataView;
import org.rm7370rf.estherproject.util.Dialog;
import org.rm7370rf.estherproject.util.Toast;
import org.rm7370rf.estherproject.util.Utils;

import butterknife.BindView;
import moxy.presenter.InjectPresenter;

import static org.rm7370rf.estherproject.util.Utils.copyToClipboard;

public class AccountDataDialog extends Dialog implements AccountDataView {
    @InjectPresenter
    AccountDataPresenter presenter;

    @BindView(R.id.userBalanceText)
    TextView userBalanceText;
    @BindView(R.id.userNameLabel)
    TextView userNameLabel;
    @BindView(R.id.userNameText)
    TextView userNameText;
    @BindView(R.id.userAddressText)
    TextView userAddressText;
    @BindView(R.id.qrCodeImage)
    ImageButton qrCode;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

//    private RefreshAnimationUtil refreshAnimationUtil = new RefreshAnimationUtil(getActivity());

    public AccountDataDialog() {
        setLayout(R.layout.dialog_account_data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setOnRefreshListener();
    }

    private void setOnRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshUserData(true));
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
