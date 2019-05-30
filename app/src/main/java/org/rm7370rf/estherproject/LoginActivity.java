package org.rm7370rf.estherproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick({R.id.createAccountBtn, R.id.importAccountBtn})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.createAccountBtn:

                break;
            case R.id.importAccountBtn:

                break;
        }
    }
}
