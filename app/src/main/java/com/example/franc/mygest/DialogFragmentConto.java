package com.example.franc.mygest;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmResults;


public class DialogFragmentConto extends Fragment {
    static Spinner spinner;
    Realm realm;
    static RealmResults<Conto> realmSelect;
    String contos;
    static Conto conto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        realmSelect = realm.where(Conto.class).findAllAsync();


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_fragment_conto, container, false);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button next;
        Button prev;
        List<Conto> arraylist = retrieve();

        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        ArrayAdapter<Conto> dataAdapter = new ArrayAdapter<Conto>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
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
    public List<Conto> retrieve()
    {
        List<Conto> nomeConto=new ArrayList<Conto>();

        RealmResults<Conto> conti=realm.where(Conto.class).findAll();
        for(Conto s: conti)
        {
            nomeConto.add(s);
        }
        return nomeConto;
    }


}
