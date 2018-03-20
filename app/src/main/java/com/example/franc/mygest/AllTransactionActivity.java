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

    private static RviewAdapterAllTransactions adapterAllTransactions;
    private static Context context;
    private static Calendar weekRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer_filter);


        RealmHelper helper = new RealmHelper();
        initUi(helper.getTransactionsAll());

    }

    //START USER INTERFACE
    private void initUi(RealmResults<Movimento> content){

        RecyclerView rview = findViewById(R.id.recyclerview);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        //todo fix this
        adapterAllTransactions = new RviewAdapterAllTransactions(this, content);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterAllTransactions);
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

        mDrawerLayout = findViewById(R.id.drawer_layout_filter);
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

}
