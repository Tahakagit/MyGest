package com.example.franc.mygest;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    static BigDecimal importo2 = null;
    static Date scadenza2 = null;
    static Date endDate2 = null;
    static String conto2 = null;
    static String recurrence2 = null;
    static String tipo2 = null;
    static String direzione2 = null;
    private MovimentoViewModel mWordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mWordViewModel = ViewModelProviders.of(this).get(MovimentoViewModel.class);


        fragments.add(new DialogFragmentBeneficiario());
        fragments.add(new DialogFragmentScadenza());
        fragments.add(new DialogFragmentImporto());
        fragments.add(new DialogFragmentConto());

        fragmentTransaction.add(R.id.fragment_container, fragments.get(i));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    //ON PREV PRESS
    public void goBack(){
        if (i>0) {
            --i;
            FragmentTransaction ft2 = fragmentManager.beginTransaction();
            ft2.replace(R.id.fragment_container, fragments.get(i));
            ft2.addToBackStack(null);
            ft2.commit();
        }
    }


    //ON NEXT PRESS RECUPERA DATI
    public void getBeneficiario(String beneficiario){
        beneficiario2 = beneficiario;
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragment_container, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getImporto(BigDecimal importo) {
        importo2 = importo;

        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragment_container, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getScadenza(Date scadenza, Date endDate, String recurrence) {
        scadenza2 = scadenza;
        endDate2 = endDate;
        recurrence2 = recurrence;
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragment_container, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }

    public void getConto(String conto) {
        conto2 = conto;
        RealmHelper helper = new RealmHelper();


        EntityMovimento mov = new EntityMovimento(beneficiario2, importo2.toString(), scadenza2, conto2, endDate2);
        mWordViewModel.insert(mov);
/*
        helper.saveMovimento(beneficiario2, importo2, scadenza2, conto2, endDate2, recurrence2);
*/
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);


/*
        MainActivity.conti.add(helper.getAccountObjectByName(conto2));
*/
        i = 0;
        finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DialogActivity.this, "Oncreateview", Toast.LENGTH_SHORT);
    }

}
