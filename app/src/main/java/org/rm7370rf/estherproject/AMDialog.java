package org.rm7370rf.estherproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AMDialog {
    private AMDialog.OnClickListener listener;
    private Activity activity;
    private AlertDialog.Builder builder;

    public AMDialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }

    public void setLayout(int resource) {
        View builderView = activity.getLayoutInflater().inflate(resource, null);
        ButterKnife.bind(activity, builderView);
        builder.setView(builderView);
    }

    public List<EditText> getEditTextList(int... resources) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resources) {
            list.add(activity.findViewById(resource));
        }

        return list;
    }

    @OnClick({R.id.positiveBtn, R.id.negativeBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.positiveBtn:
                listener.onPositiveClick();
                break;
            case R.id.negativeBtn:
                listener.onNegativeClick();
                break;
        }
    }

    public void setOnClickListener(AMDialog.OnClickListener listener) {
        this.listener = listener;
    }

    public void show() {
        builder.create().show();
    }

    interface OnClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }
}
