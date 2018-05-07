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

public class DialogFragmentConto extends Fragment {
    static Spinner spinner;
    String contos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_conto, container, false);


    }

    private void populateSpinner(List<String> list){
        Application appCtx = (MyApplication) getActivity().getApplication();
        ContoViewModel contoVM = new ContoViewModel(appCtx);

        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;

        List<String> list = new ArrayList<>();
        populateSpinner(list);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contos = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                ((DialogActivity)getActivity()).getConto(contos);


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );



    }

}
