package org.rm7370rf.estherproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class AMDialog {
    private Activity activity;
    private AlertDialog.Builder builder;

    public AMDialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }

    public void setLayout(int resource) {
        View builderView = activity.getLayoutInflater().inflate(resource, null);
        builder.setView(builderView);
    }

    public List<EditText> getEditTextList(int... resources) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resources) {
            list.add(activity.findViewById(resource));
        }

        return list;
    }

    public void show() {
        builder.create().show();
    }
}
