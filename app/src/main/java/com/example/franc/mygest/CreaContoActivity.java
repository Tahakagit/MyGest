package com.example.franc.mygest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class CreaContoActivity extends AppCompatActivity {

    String conto2;
    String tipo2;

    static Realm mRealm;
    static RealmResults<Conto> realmSelect;

    static RviewAdapterConto adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_listview);
        mRealm = Realm.getDefaultInstance();
        realmSelect = mRealm.where(Conto.class).findAllAsync();
        adapter = initUi(mRealm, realmSelect);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Conto>>() {
            @Override
            public void onChange(RealmResults<Conto> mResults) {
                adapter.notifyDataSetChanged();
                Toast.makeText(CreaContoActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private RviewAdapterConto initUi(Realm mRealm, RealmResults<Conto> realmSelect){

        final DialBeneficiario dbeneficiario = new DialBeneficiario();
        final DialImporto dimporto = new DialImporto();
        final DialScadenza dscadenza = new DialScadenza();
        CollapsingToolbarLayout collapsingToolbar;

        final Intent intent = new Intent(this, DialogActivity.class);


        this.adapter = new RviewAdapterConto(this, mRealm, realmSelect);
        ListView rview = findViewById(R.id.recyclerview);
        rview.set(new LinearLayoutManager(this));
        rview.setAdapter(this.adapter);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

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
        Toast.makeText(CreaContoActivity.this, "On resume triggered", Toast.LENGTH_SHORT).show();

    }
    protected void onPause(){
        super.onPause();
    }
}
