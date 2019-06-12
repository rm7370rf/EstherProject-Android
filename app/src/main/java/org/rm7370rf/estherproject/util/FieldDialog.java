package org.rm7370rf.estherproject.util;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public abstract class FieldDialog extends Dialog {
    @BindView(R.id.positiveBtn)
    FancyButton positiveBtn;
    @BindView(R.id.negativeBtn)
    FancyButton negativeBtn;

    private OnClickListener listener;
    private List<EditText> editTextList = new ArrayList<>();
    private int buttonNameResId = R.string.save;

    public FieldDialog() { }

    protected void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    protected void setOnClickListener(int buttonNameResId, OnClickListener listener) {
        this.buttonNameResId = buttonNameResId;
        setOnClickListener(listener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareOnClickListener();
    }

    protected void addToEditTextList(int resource) {
        editTextList.add(getView().findViewById(resource));
    }

    protected void addToEditTextList(Integer... resources) {
        for(int resource : resources) {
            addToEditTextList(resource);
        }
    }

    protected void collapsePositiveButton() {
        positiveBtn.collapse();
    }

    protected void expandPositiveButton() {
        positiveBtn.expand();
    }

    private void prepareOnClickListener() {
        positiveBtn.setText(getString(buttonNameResId));

        positiveBtn.setOnClickListener(button -> {
            hideKeyboard();
            if(listener != null) {
                listener.onClick(Utils.listOfEditTextToString(editTextList));
            }

        });
        negativeBtn.setOnClickListener(v-> dismiss());
    }

    private void hideKeyboard() {
        for (EditText et : editTextList) {
            Utils.hideKeyboard(et);
        }
    }

    protected abstract void fillEditText();

    public interface OnClickListener {
        void onClick(List<String> valueList);
    }
}
