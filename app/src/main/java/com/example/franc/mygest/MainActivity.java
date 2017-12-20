package com.example.franc.mygest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity{


/*
    static String beneficiario2 = null;
    static String importo2 = null;
    static String scadenza2 = null;
*/
    String conto2;
    String tipo2;

    static Realm mRealm;
    static RealmResults<Movimento> realmSelect;

    static RviewAdapterMovimento adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        mRealm = Realm.getDefaultInstance();
        realmSelect = mRealm.where(Movimento.class).findAllAsync();
        adapter = initUi(mRealm, realmSelect);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Movimento>>() {
            @Override
            public void onChange(RealmResults<Movimento> mResults) {
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private RviewAdapterMovimento initUi(Realm mRealm, RealmResults<Movimento> realmSelect){

        final DialBeneficiario dbeneficiario = new DialBeneficiario();
        final DialImporto dimporto = new DialImporto();
        final DialScadenza dscadenza = new DialScadenza();
        CollapsingToolbarLayout collapsingToolbar;

        final Intent intent = new Intent(this, DialogActivity.class);


        this.adapter = new RviewAdapterMovimento(this, mRealm, realmSelect);
        RecyclerView rview = findViewById(R.id.recyclerview);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(this.adapter);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(intent);
            }
        });
        return adapter;
    }

    public void startNavDrawer(){
        final DrawerLayout mDrawerLayout;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            switch (menuItem.getItemId())
                            {
                                case R.id.action_category_1:
                                    //tabLayout.getTabAt(0).select();
                                    break;
                                case R.id.action_category_2:
                                    //tabLayout.getTabAt(1).select();
                                    break;
                                case R.id.action_category_3:
                                    //tabLayout.getTabAt(2).select();
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });
        }


    }
/*
    public void getBeneficiario(String string) {
// imposto le variabili locali con i dati in arrivo
        this.beneficiario2 = string;
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    // imposto le variabili locali con i dati in arrivo
    public void getImporto(String string) {

        importo2 = string;

        Log.i("FragmentAlertDialog", "Positive click!");
    }
    public void getScadenza(String string) {

        scadenza2 = string;

        Log.i("FragmentAlertDialog", "Positive click!");
    }
*/

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(MainActivity.this, "Elemento rimosso ", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            Movimento item = realmSelect.get(position);
            mRealm.beginTransaction();
            item.deleteFromRealm();
            mRealm.commitTransaction();
            adapter.notifyItemRemoved(position);
        }
    };


    //passo i dati ricevuti dai dialog all'adapter per il salvataggio
    public void saveData(String beneficiario2, String importo2, String scadenza2){
/*
        RviewAdapter adapter = new RviewAdapter();
*/
/*
        adapter.setAll(beneficiario2, importo2, scadenza2);
*/

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