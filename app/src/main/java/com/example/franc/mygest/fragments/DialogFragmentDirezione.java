package com.example.franc.mygest.fragments;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.franc.mygest.MyApplication;
import com.example.franc.mygest.R;
import com.example.franc.mygest.activities.DialogActivity;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;

import java.util.ArrayList;
import java.util.List;

public class DialogFragmentDirezione extends Fragment {
    static Spinner accountSpinner;
    static Spinner typeSpinner;

    String contos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_direzione, container, false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;



/*
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                ((DialogActivity)getActivity()).getDirezione(contos);


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );
*/



    }

}
