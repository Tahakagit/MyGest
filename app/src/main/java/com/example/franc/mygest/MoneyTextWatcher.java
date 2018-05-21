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

        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        String u = "";
        if(s.matches("-")){
            u = s + " ";
            return;
        }else if(s.matches("")){
            return;
        }

        editText.removeTextChangedListener(this);
        String cleanString = s.replaceAll("[ €,.\\s]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        editText.setText(u.concat(formatted));

        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);

    }
}
