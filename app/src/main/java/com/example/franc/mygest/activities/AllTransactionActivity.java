package com.example.franc.mygest.activities;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.franc.mygest.MyApplication;
import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterAllTransactions;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AllTransactionActivity extends AppCompatActivity{

    private RviewAdapterAllTransactions adapterAllTransactions;
    private Context context;
    private static Calendar weekRange;
    private MovimentoViewModel mWordViewModel;
    static String conto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_filter);

        Switch btnCheck = findViewById(R.id.btn_check);
        EditText edittextBeneficiario = findViewById(R.id.id_edittext_filter_beneficiario);
        EditText edittextConto = findViewById(R.id.id_edittext_filter_conto);

        context = this;
        mWordViewModel = ViewModelProviders.of(this).get(MovimentoViewModel.class);

        mWordViewModel.viewUnchecked();
        mWordViewModel.getAllDates().observe(this, new Observer<List<EntityMovimento>>() {
            @Override
            public void onChanged(@Nullable final List<EntityMovimento> movimentos) {
                // Update the cached copy of the movimentos in the adapter.
                adapterAllTransactions.setResults(movimentos);
            }
        });

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
        edittextBeneficiario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWordViewModel.filterBeneficiario(edittextBeneficiario.getText().toString());

                conto = edittextBeneficiario.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
/*
        edittextConto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWordViewModel.filterConto(edittextConto.getText().toString());

                conto = edittextConto.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/

        initUi();

    }

    public String getBeneficiario(){
        return conto;
    }
    //START USER INTERFACE
    private void initUi(){
        DividerItemDecoration mDividerItemDecoration;
        Spinner accountSpinner = findViewById(R.id.spinner);

        RecyclerView rview = findViewById(R.id.recyclerview);
        ActionBar myToolbar = getSupportActionBar();
/*
        setSupportActionBar(myToolbar);
*/


        myToolbar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = new Intent(this, DialogActivity.class);
        mDividerItemDecoration = new DividerItemDecoration(rview.getContext(),
                RecyclerView.VERTICAL);

        populateAccountSpinner(accountSpinner);
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

    void populateAccountSpinner(Spinner spinner){
        Application appCtx = (MyApplication) context.getApplicationContext();

        ContoViewModel contoVM = new ContoViewModel(appCtx);

        ArrayList<String> list = new ArrayList<>();
        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(appCtx, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                contos = parent.getItemAtPosition(position).toString();
*/
                mWordViewModel.filterConto(String.valueOf(contoVM.getAccountIdByName(parent.getItemAtPosition(position).toString()).getId()));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


}
