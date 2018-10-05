package com.example.franc.mygest.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.franc.mygest.MoneyTextWatcher;
import com.example.franc.mygest.R;
import com.example.franc.mygest.activities.DialogActivity;

import java.math.BigDecimal;


public class DialogFragmentImporto extends Fragment {
    static Button next;
    static Button prev;

    static EditText importo;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_dialog_fragment_importo, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        importo = view.findViewById(R.id.inputimporto);

        importo.addTextChangedListener(new MoneyTextWatcher(importo));
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String cleanString = importo.getText().toString().replaceAll("[ €,.\\s]", "");

                if(!cleanString.matches(""))
                    ((DialogActivity)getActivity()).getImporto(new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
                else{
                    displayPopupWindow(getContext(), importo, "Inserisci la data di dayScadenzaText!");
                    importo.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));

                }
            }
        } );
        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );


    }
    /**
     * Starts Alert
     * @param anchorView Anchor view
     * @param helpText Custom Message
     */
    private void displayPopupWindow(Context context, View anchorView, String helpText) {
        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);

        LinearLayout ll = new LinearLayout(context);
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(context);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(linLayoutParam);


        tv.setText(helpText);
        tv.setLayoutParams(linLayoutParam);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(ContextCompat.getColor(context, R.color.red));
        tv.setTextSize(25);
/*
        tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
*/
        ll.addView(tv);

        PopupWindow popup = new PopupWindow(ll, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setContentView(ll);


/*
        popup.setHeight(ll.getHeight());
        popup.setWidth(ll.getWidth());
*/
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        Drawable drawBackground = ContextCompat.getDrawable(context, R.drawable.dialog_background);
        popup.setBackgroundDrawable(drawBackground);
        popup.showAtLocation(anchorView, Gravity.TOP, 150, 0);
    }



}
