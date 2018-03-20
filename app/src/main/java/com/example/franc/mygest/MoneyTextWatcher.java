package com.example.franc.mygest;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

import io.realm.RealmResults;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by franc on 05/11/2017.
 */

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Context context = getApplicationContext();

        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        //String s = editable.toString();€
        editText.removeTextChangedListener(this);
        String cleanString = editable.toString().replaceAll("[ €,.\\s]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        editText.setText(formatted);

        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);

        RealmHelper helper = new RealmHelper();
        try {
/*
            helper.updateBalance("c1", parsed);
*/
        } catch (NullPointerException e) {
            Log.w("textBeneficiario", "Account balance not changed");
        }

    }
}
