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
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class AllTransactionActivity extends AppCompatActivity{

    Realm mRealm;
    static RealmResults<DailyTransaction> realmSelectDaysWithTransactions;
    static RealmResults<Movimento> realmSelectMovimenti;

    private static RviewAdapterDailyTransaction adapterDailyTransaction;

    private static Context context;
    private static Calendar weekRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer_main);

        weekRange = Calendar.getInstance();
        weekRange.add(Calendar.DAY_OF_MONTH, 7);

        RealmHelper helper = new RealmHelper();
        initUi(helper.getTransactionsAll());

    }

    //START USER INTERFACE
    private void initUi(RealmResults<Movimento> content){

        //todo riferisce allo stesso recycler dell'altra activity
        RecyclerView rview = findViewById(R.id.recyclerview_filter);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(content);
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

    }

    //NAVIGATION DRAWER
    private void startNavDrawer(){
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

    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
