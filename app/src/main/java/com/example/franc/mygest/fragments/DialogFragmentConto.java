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
    static Spinner accountSpinner;
    static Spinner typeSpinner;

    String contos;
    String type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_conto, container, false);


    }

    private ArrayList<String> getAccountList(){
        Application appCtx = getActivity().getApplication();
        ContoViewModel contoVM = new ContoViewModel(appCtx);

        ArrayList<String> list = new ArrayList<>();
        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }
        return  list;

    }
    private ArrayList<String> getTypeList(){
        ArrayList<String> arraylist = new ArrayList<>();

        arraylist.add("Assegno");
        arraylist.add("SSD");
        arraylist.add("Riba");
        arraylist.add("Bonifico");
        return arraylist;
    }


    void populateAccountSpinner(Spinner spinner){
        Application appCtx = getActivity().getApplication();
        ContoViewModel contoVM = new ContoViewModel(appCtx);

        ArrayList<String> list = new ArrayList<>();
        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
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

    }
    void populateTypeSpinner(Spinner spinner){
        ArrayList<String> arraylist = new ArrayList<>();

        arraylist.add("Assegno");
        arraylist.add("SSD");
        arraylist.add("Riba");
        arraylist.add("Bonifico");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;

        accountSpinner = view.findViewById(R.id.spinner);
        typeSpinner = view.findViewById(R.id.spinner_transaction_types);

        List<String> list = new ArrayList<>();
        populateAccountSpinner(accountSpinner);
        populateTypeSpinner(typeSpinner);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);

        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){


                ((DialogActivity)getActivity()).getConto(contos, type);


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );



    }

}
