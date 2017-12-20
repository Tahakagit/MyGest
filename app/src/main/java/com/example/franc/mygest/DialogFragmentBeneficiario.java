package com.example.franc.mygest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class DialogFragmentBeneficiario extends Fragment {

    static Button next;
    static Button prev;

    static EditText beneficiario;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_beneficiario, container, false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        beneficiario = view.findViewById(R.id.inputBeneficiario);
        // or  (ImageView) view.findViewById(R.id.foo);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            /*
            *
            *       salva il dato
            *
            *
            *
            *
            * */

                ((DialogActivity)getActivity()).getBeneficiario(beneficiario.getText().toString());

            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );



    }
}