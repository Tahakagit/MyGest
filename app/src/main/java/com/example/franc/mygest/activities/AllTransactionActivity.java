package com.example.franc.mygest.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterAllTransactions;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.util.Calendar;
import java.util.List;


public class AllTransactionActivity extends AppCompatActivity{

    private static RviewAdapterAllTransactions adapterAllTransactions;
    private static Context context;
    private static Calendar weekRange;
    private MovimentoViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer_filter);
        mWordViewModel = ViewModelProviders.of(this).get(MovimentoViewModel.class);

        mWordViewModel.viewUnchecked();
        mWordViewModel.getAllMovimentoChecked().observe(this, new Observer<List<EntityMovimento>>() {
            @Override
            public void onChanged(@Nullable final List<EntityMovimento> words) {
                // Update the cached copy of the words in the adapter.
                adapterAllTransactions.setResults(words);
            }
        });
        Switch btnCheck = findViewById(R.id.btn_check);
        btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnCheck.isChecked()){
                    mWordViewModel.viewChecked();

                }
                else {
                    mWordViewModel.viewUnchecked();

                }
            }
        });

        initUi();

    }

    //START USER INTERFACE
    private void initUi(){

        RecyclerView rview = findViewById(R.id.recyclerview);
        Toolbar myToolbar = findViewById(R.id.toolbar_creacontoactivity);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        //todo fix this
        adapterAllTransactions = new RviewAdapterAllTransactions(this);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterAllTransactions);
        FloatingActionButton fab = findViewById(R.id.fab_insert_transaction);
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
        final Intent creaConto = new Intent(this, AccountsManageActivity.class);

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
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });
        }


    }

}
