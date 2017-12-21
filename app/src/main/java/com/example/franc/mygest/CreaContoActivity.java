package com.example.franc.mygest;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CreaContoActivity extends AppCompatActivity {

    Realm realm;
    RviewAdapterConto adapter;
    RecyclerView rv;
    static RealmResults<Conto> realmSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creaconto_activity_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        rv= findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        realm=Realm.getDefaultInstance();
        realmSelect = realm.where(Conto.class).findAllAsync();

        adapter=new RviewAdapterConto(this,realm,realmSelect);
        rv.setAdapter(adapter);

        realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Conto>>() {
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
        final EditText nomeConto= d.findViewById(R.id.nomeconto);
        final EditText saldoConto= d.findViewById(R.id.saldoconto);

        Button saveBtn= d.findViewById(R.id.saveconto);
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
