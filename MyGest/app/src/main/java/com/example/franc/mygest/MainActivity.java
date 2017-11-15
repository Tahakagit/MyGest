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


    String beneficiario2 = null;
    String importo2 = null;
    String scadenza2;
    String conto2;
    String tipo2;


    DialBeneficiario dbeneficiario = new DialBeneficiario();
    DialImporto dimporto = new DialImporto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        Realm.init(this);
*/

        Realm mRealm = Realm.getDefaultInstance();
        // query
        final RealmResults<Movimento> realmSelect = mRealm.where(Movimento.class).findAll();
        setContentView(R.layout.activity_main);

        initUi(mRealm, realmSelect);
        realmSelect.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Movimento>>() {
            @Override
            public void onChange(RealmResults<Movimento> mResults, OrderedCollectionChangeSet changeSet) {
/*
                adapter.notifyDataSetChanged();
*/
                changeSet.getInsertions();
                Toast.makeText(MainActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getBeneficiario(String string) {
/*
        TextView risultato = (TextView) findViewById(R.id.risultato) ;
        risultato.setText(string);
*/

        RviewAdapter adapter = new RviewAdapter();
        adapter.setImporto(string);

        beneficiario2 = string;
        Log.i("FragmentAlertDialog", "Positive click!");
    }
    public void getImporto(String string) {
/*
        TextView risultato = (TextView) findViewById(R.id.risultato) ;
        risultato.setText(string);
*/
        RviewAdapter adapter = new RviewAdapter();
        adapter.setBeneficiario(string);

        importo2 = string;

        Log.i("FragmentAlertDialog", "Positive click!");
    }


    private void initUi(Realm mRealm, RealmResults<Movimento> realmSelect){

        final RviewAdapter adapter = new RviewAdapter(this, mRealm, realmSelect);
        adapter.setHasStableIds(true);
        RecyclerView rview = findViewById(R.id.recyclerview);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                dbeneficiario.show(getFragmentManager(), "dbeneficiario");
                dimporto.show(getFragmentManager(), "dimporto");
                //adapter.setAll(beneficiario2, importo2);
            }

        });


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
