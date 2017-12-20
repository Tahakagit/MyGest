package com.example.franc.mygest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).getImporto(importo.getText().toString());

            }
        } );
        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );


    }

}
