package org.rm7370rf.estherproject.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.rm7370rf.estherproject.ui.activity.TopicListActivity;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static org.rm7370rf.estherproject.R.string.copied_to_clipboard;

public class Utils {
    public static Intent createIntentWithoutHistory(Activity from, Class to) {
        return new Intent(from, to).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
    public static Bitmap createQrCode(String text, int width, int height) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.show(context, copied_to_clipboard);
    }

    public static String timestampToDate(BigInteger timestamp) {
        return new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US).format(new Date (timestamp.longValue()*1000));
    }

    public static List<String> listOfEditTextToString(List<EditText> editTextList) {
        List<String> result = new ArrayList<>();
        for (EditText editText : editTextList) {
            result.add(editText.getText().toString());
        }
        return result;
    }
}
