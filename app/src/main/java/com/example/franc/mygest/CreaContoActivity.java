package com.example.franc.mygest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CreaContoActivity extends AppCompatActivity {

    Realm realm;
    ArrayList<String> conti;
    RviewAdapterConto adapter;
    RecyclerView rv;
    EditText nameeditTxt;
    static RealmResults<Conto> realmSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creaconto_activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        rv= findViewById(R.id.rv);

        realm=Realm.getDefaultInstance();
        realmSelect = realm.where(Conto.class).findAllAsync();
        //SETUP REALM
/*
        RealmConfiguration config=new RealmConfiguration.Builder(this).build();
*/
        //RETRIEVE
        ContoRealmHelper helper=new ContoRealmHelper(realm);
/*
        conti=helper.retrieve();
*/
        //BIND
        adapter=new RviewAdapterConto(this,realm,realmSelect);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        //ITEM CLICKS
/*
        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreaContoActivity.this,conti.get(position),Toast.LENGTH_SHORT).show();
            }
        });
*/       realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Conto>>() {
            @Override
            public void onChange(RealmResults<Conto> mResults) {
                adapter.notifyDataSetChanged();
                Toast.makeText(CreaContoActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
    }
    //DISPLAY INPUT DIALOG
    private void displayInputDialog()    {
        final Dialog d=new Dialog(this);
        d.setTitle("Save to Realm");

        //Todo: creare layout dialog inserimento conto
        d.setContentView(R.layout.input_dialog_creaconto);
        final EditText nomeConto= (EditText) d.findViewById(R.id.nomeconto);
        final EditText saldoConto= (EditText) d.findViewById(R.id.saldoconto);

        Button saveBtn= (Button) d.findViewById(R.id.saveconto);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                Conto s=new Conto();
                s.setNomeConto(nomeConto.getText().toString());
                s.setSaldoConto(saldoConto.getText().toString());
                //SAVE
/*
                ContoRealmHelper helper=new ContoRealmHelper(realm);
*/
                adapter.setConto(s);
                nomeConto.setText("");
                saldoConto.setText("");
                d.dismiss();
                //RETRIEVE
/*

                conti=helper.retrieve();
*/
/*
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
*/
            }
        });
        d.show();
    }
}
