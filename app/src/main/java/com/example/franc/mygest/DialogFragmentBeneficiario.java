package com.example.franc.mygest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class DialogFragmentBeneficiario extends Fragment {


    EditText textBeneficiario;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_beneficiario, container, false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;

        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);

        textBeneficiario = view.findViewById(R.id.inputBeneficiario);

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String beneficiario = null;
                beneficiario = textBeneficiario.getText().toString();
                if (!beneficiario.matches("")){
                    ((DialogActivity)getActivity()).getBeneficiario(beneficiario);
                }else{
                    Log.w("DialogFragmentBen", "WARNING:: beneficiario null");
                    textBeneficiario.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    displayPopupWindow(getContext(), textBeneficiario, "Inserisci il beneficiario!");
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
        ;
        popup.setBackgroundDrawable(drawBackground);
        popup.showAtLocation(anchorView, Gravity.TOP, 150, 0);
    }

}