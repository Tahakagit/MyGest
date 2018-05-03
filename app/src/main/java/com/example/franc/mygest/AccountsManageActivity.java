package com.example.franc.mygest;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.franc.mygest.persistence.ContoRepo;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class AccountsManageActivity extends AppCompatActivity implements UIController.onAccountCreatedListener{
    int selectedColor = R.color.blue;

    Realm realm;
    ContoViewModel contoVM;
    RviewAdapterConto mAdapterConti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creaconto_activity_view);
        RealmResults<Conto> dbAccountsSelect;

        contoVM = new ContoViewModel(getApplication());

        realm=Realm.getDefaultInstance();
        dbAccountsSelect = realm.where(Conto.class).findAllAsync();

/*
        initUi(dbAccountsSelect);
*/
        final UIController uiController = new UIController(this);

/*
        uiController.initUi();
*/
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
        realm.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        realm.close();
    }
}
