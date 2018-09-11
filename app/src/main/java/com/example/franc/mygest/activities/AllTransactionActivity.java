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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterAllTransactions;
import com.example.franc.mygest.adapters.RviewAdapterGroupDates;
import com.example.franc.mygest.persistence.DateViewModel;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.DateViewModel;

import java.util.Calendar;
import java.util.List;


public class AllTransactionActivity extends AppCompatActivity{

    private RviewAdapterAllTransactions adapterAllTransactions;
    private Context context;
    private static Calendar weekRange;
    private DateViewModel mWordViewModel;
    static String beneficiario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer_filter);
        mWordViewModel = ViewModelProviders.of(this).get(DateViewModel.class);

        mWordViewModel.viewUnchecked();
        mWordViewModel.getAllDates().observe(this, new Observer<List<EntityMovimento>>() {
            @Override
            public void onChanged(@Nullable final List<EntityMovimento> movimentos) {
                // Update the cached copy of the movimentos in the adapter.
                adapterAllTransactions.setResults(movimentos);
            }
        });
        Switch btnCheck = findViewById(R.id.btn_check);
        EditText text = findViewById(R.id.id_filter_beneficiario);
        Button btn = findViewById(R.id.btn_filter);
        btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnCheck.isChecked()){
                    mWordViewModel.viewChecked();
                    adapterAllTransactions.notifyDataSetChanged();
                }
                else {
                    mWordViewModel.viewUnchecked();
                    adapterAllTransactions.notifyDataSetChanged();

                }
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWordViewModel.filterBeneficiario(text.getText().toString());

                beneficiario = text.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        initUi();

    }

    public String getBeneficiario(){
        return beneficiario;
    }
    //START USER INTERFACE
    private void initUi(){
        DividerItemDecoration mDividerItemDecoration;

        RecyclerView rview = findViewById(R.id.recyclerview);
        Toolbar myToolbar = findViewById(R.id.toolbar_creacontoactivity);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);
        mDividerItemDecoration = new DividerItemDecoration(rview.getContext(),
                RecyclerView.VERTICAL);

        adapterAllTransactions = new RviewAdapterAllTransactions(this, this, getApplication());
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterAllTransactions);
        rview.addItemDecoration(mDividerItemDecoration);
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
