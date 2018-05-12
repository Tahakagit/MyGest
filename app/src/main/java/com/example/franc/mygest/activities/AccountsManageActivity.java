package com.example.franc.mygest.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterConto;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;

import java.util.List;


public class AccountsManageActivity extends AppCompatActivity implements UIController.onAccountCreatedListener {
    int selectedColor = R.color.blue;

    ContoViewModel contoVM;
    RviewAdapterConto mAdapterConti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creaconto_activity_view);

        contoVM = new ContoViewModel(getApplication());

        final UIController uiController = new UIController(this);
        Toolbar toolbar = findViewById(R.id.toolbar_creacontoactivity);
        RecyclerView rv = findViewById(R.id.rv_account_manage);
        FloatingActionButton fab = findViewById(R.id.fab_account_create);
        mAdapterConti = new RviewAdapterConto(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiController.displayAccountDialog(null, null, 0);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapterConti);


        contoVM.getAllAccounts().observe(this, new Observer<List<EntityConto>>() {
            @Override
            public void onChanged(@Nullable List<EntityConto> entityContos) {
                mAdapterConti.setResults(entityContos);
            }
        });
    }

    @Override
    public void onAccountCreated(EntityConto conto) {
        contoVM.insert(conto);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}