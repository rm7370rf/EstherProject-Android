package org.rm7370rf.estherproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.createAccountBtn, R.id.importAccountBtn})
    public void onClick(View view) {
        int dialogId;

        switch (view.getId()) {
            case R.id.createAccountBtn:
                dialogId = R.layout.dialog_create_account;
                break;
            case R.id.importAccountBtn:
                dialogId = R.layout.dialog_import_account;
                break;
            default:
                return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View builderView = getLayoutInflater().inflate(dialogId, null);
        builderView.setOnClickListener(v -> {

        });

        builder.setView(builderView);
        builder.create().show();
    }
}
