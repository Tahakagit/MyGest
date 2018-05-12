package com.example.franc.mygest.activities;

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

import com.example.franc.mygest.fragments.DialogFragmentBeneficiario;
import com.example.franc.mygest.fragments.DialogFragmentConto;
import com.example.franc.mygest.fragments.DialogFragmentImporto;
import com.example.franc.mygest.fragments.DialogFragmentScadenza;
import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogActivity extends AppCompatActivity {

    static int i = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final ArrayList<Fragment> fragments = new ArrayList <>();

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

    public void getConto(String conto, String tipo) {
        conto2 = conto;
        tipo2 = tipo;
        Calendar cal = Calendar.getInstance();
        cal.setTime(scadenza2);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(scadenza2);
        cal2.set(Calendar.HOUR_OF_DAY, 00);
        cal2.set(Calendar.MINUTE, 00);
        cal2.set(Calendar.SECOND, 00);
        cal2.set(Calendar.MILLISECOND, 00);

        mWordViewModel.insert(beneficiario2, importo2.toString(), cal.getTime(), conto2, cal2.getTime(), recurrence2, tipo2);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        i = 0;
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DialogActivity.this, "Oncreateview", Toast.LENGTH_SHORT);
    }

}
