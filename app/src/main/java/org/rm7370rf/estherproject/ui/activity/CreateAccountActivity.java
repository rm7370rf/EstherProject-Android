package org.rm7370rf.estherproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.ui.dialog.CreateAccountDialog;

import butterknife.ButterKnife;

import static org.rm7370rf.estherproject.util.Verifier.isAccountExists;

public class CreateAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(isAccountExists()) {
            startTopicListActivity();
        }
    }

    private void startTopicListActivity() {
        startActivity(new Intent(this, TopicListActivity.class));
    }

    public void onClick(View view) {
        CreateAccountDialog dialog = new CreateAccountDialog();
        dialog.setLayout(view.getId() == R.id.createAccountBtn ? R.layout.dialog_create_account : R.layout.dialog_import_account);
        dialog.show(getSupportFragmentManager(), "");
    }
}