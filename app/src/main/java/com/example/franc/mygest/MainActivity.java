package com.example.franc.mygest;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DialogFragment;

import java.util.Date;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class MainActivity extends Activity{


    static String beneficiario2 = null;
    static String importo2 = null;
    String scadenza2;
    String conto2;
    String tipo2;

    static Realm mRealm;
    static RealmResults<Movimento> realmSelect;

    static RviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        Realm.init(this);
*/
        this.mRealm = Realm.getDefaultInstance();
        this.realmSelect = mRealm.where(Movimento.class).findAllAsync();
        this.adapter = initUi(mRealm, realmSelect);
        //this.adapter.setHasStableIds(true);



/*
        final RviewAdapter adapt = initUi(mRealm, realmSelect);
*/
        realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Movimento>>() {
            @Override
            public void onChange(RealmResults<Movimento> mResults) {
                //rilevo modifiche al db
                adapter.notifyDataSetChanged();
/*
                changeSet.getInsertions();
*/
                Toast.makeText(MainActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private RviewAdapter initUi(Realm mRealm, RealmResults<Movimento> realmSelect){

        final DialBeneficiario dbeneficiario = new DialBeneficiario();
        final DialImporto dimporto = new DialImporto();

        this.adapter = new RviewAdapter(this, mRealm, realmSelect);
        RecyclerView rview = findViewById(R.id.recyclerview);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(this.adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dbeneficiario.show(getFragmentManager(), "dbeneficiario");
                dimporto.show(getFragmentManager(), "dimporto");
/*              //Todo qua viene inserito al click del menuzz
                adapter.setAll(beneficiario2, importo2);
*/
            }
        });
        return adapter;

    }


    public void getBeneficiario(String string) {
// imposto le variabili locali con i dati in arrivo
        this.beneficiario2 = string;
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    // imposto le variabili locali con i dati in arrivo
    public void getImporto(String string) {

        this.importo2 = string;

        Log.i("FragmentAlertDialog", "Positive click!");
    }


    //passo i dati ricevuti dai dialog all'adapter per il salvataggio
    public void saveData(){
/*
        RviewAdapter adapter = new RviewAdapter();
*/
        adapter.setAll(beneficiario2, importo2);

    }
    protected void onResume()
    {
        super.onResume();
        Toast.makeText(MainActivity.this, "On resume triggered", Toast.LENGTH_SHORT).show();

    }
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
