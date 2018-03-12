package com.example.franc.mygest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity{

    Realm mRealm;
    static RealmResults<DailyTransaction> realmSelectDaysWithTransactions;
    static RealmResults<Movimento> realmSelectMovimenti;

    static RviewAdapterDailyTransaction adapterDailyTransaction;

    static Context context;
    static Calendar weekRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer);

        weekRange = Calendar.getInstance();
        weekRange.add(Calendar.DAY_OF_MONTH, 7);

        mRealm = Realm.getDefaultInstance();

        RealmHelper helper = new RealmHelper();
        RealmResults<Movimento> allTransaction = helper.getTransactionsUntil(weekRange.getTime());

        // cerco i conti con transazioni in scadenza nel range todo fix this
        RealmResults<Movimento> conti = allTransaction.where().distinct("conto");
        ArrayList<String> listConti = new ArrayList<>();

        for (Movimento mov:conti) {
            listConti.add(mov.getConto().toString());
        }
        realmSelectDaysWithTransactions = mRealm.where(DailyTransaction.class).findAllAsync();
        adapterDailyTransaction = initUi(mRealm, listConti);
//todo non mostra risultati DailyTransactionss

        realmSelectDaysWithTransactions.addChangeListener(new RealmChangeListener<RealmResults<DailyTransaction>>() {
            @Override
            public void onChange(RealmResults<DailyTransaction> mResults) {
                adapterDailyTransaction.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //START USER INTERFACE
    private RviewAdapterDailyTransaction initUi(Realm mRealm, ArrayList<String> conti){

        RecyclerView rview = findViewById(R.id.recyclerview);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(this, mRealm, conti);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterDailyTransaction);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(intent);
            }
        });




        startNavDrawer();

        return adapterDailyTransaction;
    }

    //NAVIGATION DRAWER
    public void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, CreaContoActivity.class);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            switch (menuItem.getItemId())
                            {
                                case R.id.action_category_1:
                                    startActivity(creaConto);
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
    //RIGHT SWIPE
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
*/


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
