package com.example.franc.mygest;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {
    static int i = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    final ArrayList<Fragment> fragments = new ArrayList <>();
    static Button next;
    static Button prev;
    static String beneficiario2 = null;
    static String importo2 = null;
    static String scadenza2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        fragments.add(new DialogFragmentBeneficiario());
        fragments.add(new DialogFragmentImporto());
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragmentcontainer, fragments.get(i));


        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();






/*
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ++i;
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
                ft2.replace(R.id.fragmentcontainer, fragments.get(i));
                ft2.addToBackStack(null);
                ft2.commit();

            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                --i;
                fragmentTransaction.replace(R.id.fragmentcontainer, fragments.get(i));
            }
        } );

*/



    }

    public void goBack(){

        --i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();

    }
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
        MainActivity.adapter.setAll(beneficiario2, importo2);
        i = 0;
        finish();

/*
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
*/
    }
    public void getScadenza(String scadenza) {

        scadenza2 = scadenza;
        ++i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DialogActivity.this, "Oncreateview", Toast.LENGTH_SHORT);


    }

}
