package org.rm7370rf.estherproject.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

public abstract class FieldDialog extends Dialog {
    private FancyButton positiveBtn;
    private FancyButton negativeBtn;
    private OnClickListener listener;
    private List<EditText> editTextList = new ArrayList<>();
    private int buttonNameResId = R.string.save;

    public FieldDialog() { }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnClickListener(int buttonNameResId, OnClickListener listener) {
        this.buttonNameResId = buttonNameResId;
        setOnClickListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, null);
        positiveBtn = v.findViewById(R.id.positiveBtn);
        negativeBtn = v.findViewById(R.id.negativeBtn);
        return v;
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
                listener.onClick(button.getId(), Utils.listOfEditTextToString(editTextList));
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
        void onClick(int buttonId, List<String> valueList);
    }
}
