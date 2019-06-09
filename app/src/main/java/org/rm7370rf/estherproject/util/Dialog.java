package org.rm7370rf.estherproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class Dialog extends AlertDialog.Builder implements DialogInterface.OnDismissListener {
    private Activity activity;
    private AlertDialog dialog;
    private View view;

    public Dialog(@NonNull Activity activity) {
        super(activity);
        setOnDismissListener(this);
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public Context getContext() {
        return activity;
    }

    public View getView() {
        return view;
    }

    public void setLayout(int resource) {
        view = activity.getLayoutInflater().inflate(resource, null);
        setView(view);
    }

    public void hide() {
        dialog.hide();
    }

    @Override
    public AlertDialog show() {
        dialog = create();
        dialog.show();
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) { }
}