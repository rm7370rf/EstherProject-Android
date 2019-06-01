package org.rm7370rf.estherproject.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

public class AMDialog {
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View view;

    public AMDialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }

    public void setLayout(int resource) {
        view = activity.getLayoutInflater().inflate(resource, null);
        builder.setView(view);
    }

    public List<EditText> getEditTextList(List<Integer> resourceList) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resourceList) {
            list.add(view.findViewById(resource));
        }

        return list;
    }

    public void setOnClickListener(AMDialog.OnClickListener listener) {
        FancyButton positiveBtn = view.findViewById(R.id.positiveBtn),
                               negativeBtn = view.findViewById(R.id.negativeBtn);

        positiveBtn.setOnClickListener(v -> listener.onClick(positiveBtn));
        negativeBtn.setOnClickListener(v-> alertDialog.cancel());
    }

    public void show() {
        alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnClickListener {
        void onClick(FancyButton button);
    }
}
