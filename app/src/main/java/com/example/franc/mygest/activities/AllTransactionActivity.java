package com.example.franc.mygest.activities;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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


public class AllTransactionActivity extends AppCompatActivity implements View.OnClickListener{

    private RviewAdapterAllTransactions adapterAllTransactions;
    private Context context;
    private static Calendar weekRange;
    private MovimentoViewModel mWordViewModel;
    static String beneficiario = null;
    static String conto = null;
    static String checked = null;
    BottomSheetBehavior sheetBehavior;

    LinearLayout bottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_filter);


        bottomSheet = findViewById(R.id.bottom_sheet);

        startBottomMenu(bottomSheet);
        context = this;
        mWordViewModel = ViewModelProviders.of(this).get(MovimentoViewModel.class);

        setAccount(String.valueOf(10));
        checked = "unchecked";
        mWordViewModel.viewUnchecked();
        mWordViewModel.getAllDates().observe(this, new Observer<List<EntityMovimento>>() {
            @Override
            public void onChanged(@Nullable final List<EntityMovimento> movimentos) {
                // Update the cached copy of the movimentos in the adapter.
                adapterAllTransactions.setResults(movimentos);
            }
        });


        initUi();

    }

    private void startBottomMenu(View bottomSheet){

        // Parent activity must implements View.OnClickListener
        findViewById(R.id.bg).setOnClickListener(this);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)

                    findViewById(R.id.bg).setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.bg).setVisibility(View.VISIBLE);
                findViewById(R.id.bg).setAlpha(slideOffset);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_sheet: {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            }
            case R.id.bg: {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                }

            }
        }
        return super.dispatchTouchEvent(event);
    }



    /**
     *
     * Starts ui elements
     */
    private void initUi(){
        RecyclerView rview = findViewById(R.id.recyclerview);
        ActionBar myToolbar = getSupportActionBar();

        myToolbar.setDisplayHomeAsUpEnabled(true);

        startFilterMenu();
        adapterAllTransactions = new RviewAdapterAllTransactions(this, this, getApplication());
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterAllTransactions);

        startNavDrawer();

    }

    public String getBeneficiario(){
        return beneficiario;
    }
    public String getAccount(){
        return conto;
    }
    public void setAccount(String account){
        conto = account;
    }
    public String getChecked(){
        return checked;
    }

    //NAVIGATION DRAWER
    private void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, AccountsManageActivity.class);

        mDrawerLayout = findViewById(R.id.drawer_layout_filter);
        NavigationView navigationView = findViewById(R.id.navigationview_main);

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
        list.add("Tutte");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(appCtx, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                contos = parent.getItemAtPosition(position).toString();
*/

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Tutte")){
                    conto = String.valueOf(10);
                }else {
                    conto = String.valueOf(contoVM.getAccountIdByName(parent.getItemAtPosition(position).toString()).getId());

                }

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Tutte")){
                    mWordViewModel.filterConto(String.valueOf(10));
                }else {
                    mWordViewModel.filterConto(String.valueOf(contoVM.getAccountIdByName(parent.getItemAtPosition(position).toString()).getId()));

                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void startFilterMenu(){
        Switch btnCheck = findViewById(R.id.btn_check);
        AutoCompleteTextView edittextBeneficiario = findViewById(R.id.id_edittext_filter_beneficiario);
        Spinner accountSpinner = findViewById(R.id.spinner);

        btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnCheck.isChecked()){
                    checked = "checked";
                    mWordViewModel.viewChecked();
                }
                else {
                    checked = "unchecked";

                    mWordViewModel.viewUnchecked();
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mWordViewModel.getKnownBeneficiari());

        edittextBeneficiario.setAdapter(adapter);
        edittextBeneficiario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mWordViewModel.filterBeneficiario(edittextBeneficiario.getText().toString());

                beneficiario = edittextBeneficiario.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        populateAccountSpinner(accountSpinner);

    }

}
