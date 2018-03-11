package com.example.franc.mygest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DialogActivity extends AppCompatActivity {

    static int i = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final ArrayList<Fragment> fragments = new ArrayList <>();
    static RviewAdapterMovimenti adapter;
    Realm mRealm;
    static RealmResults<Movimento> realmSelect;

    static Button next;
    static Button prev;
    static String beneficiario2 = null;
    static String importo2 = null;
    static String scadenza2 = null;
    static String conto2 = null;
    static String tipo2 = null;
    static String direzione2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragments.add(new DialogFragmentBeneficiario());
        fragments.add(new DialogFragmentScadenza());
        fragments.add(new DialogFragmentImporto());
        fragments.add(new DialogFragmentConto());

        fragmentTransaction.add(R.id.fragmentcontainer, fragments.get(i));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    //ON PREV PRESS
    public void goBack(){
        --i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }


    //ON NEXT PRESS RECUPERA DATI
    public void getBeneficiario(String beneficiario){
        beneficiario2 = beneficiario;
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getImporto(String importo) {
        importo2 = importo;

        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getScadenza(String scadenza) {
        scadenza2 = scadenza;
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getConto(String conto) {
        conto2 = conto;
/*
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
*/
/*
        adapter = new RviewAdapterMovimenti(this, mRealm, realmSelect);
*/
        RviewAdapterDailyTransaction.adapterMovimenti.setAll(beneficiario2, importo2, scadenza2);
        i = 0;
        finish();
    }

    public void EndDialogActivity(){
//todo adapter.setall aggiungere conto

    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DialogActivity.this, "Oncreateview", Toast.LENGTH_SHORT);
    }

}
