package com.example.franc.mygest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity{

    RealmHelper helper = new RealmHelper();
    RviewAdapterDailyTransaction adapterDailyTransaction;
    static Calendar weekRange;
    static Date dateToSend;
    static ArrayList<ContoObj> conti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navigation_drawer_main);

        weekRange = Calendar.getInstance();
        weekRange.add(Calendar.DAY_OF_MONTH, 7);

        dateToSend = weekRange.getTime();
        conti = helper.getTransactionsUntilGroupedByAccount(dateToSend);
        initUi(conti);
        showDatePicker();

        showCurrentBalances(conti);
    }

    /**
     * Starts UI
     * @param conti2 Arraylist of accounts with pending transactions
     */
    private void initUi(ArrayList<ContoObj> conti2){


        RecyclerView rview = findViewById(R.id.recyclerview_filter);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(this, conti2);
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterDailyTransaction);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(intent, 1);
            }
        });




        startNavDrawer();

    }
    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showDatePicker() {
        final DatePickerFragment date = new DatePickerFragment();


        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        final Button edittext = findViewById(R.id.selectdate);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

                //date object from spinner
                dateToSend = c.getTime();
                //formatted date string from spinner
                String formattedDate = sdf.format(c.getTime());




                edittext.setText(formattedDate);
                adapterDailyTransaction.setResultsRealm(helper.getTransactionsUntilGroupedByAccount(dateToSend));
            }
        };
        date.setCallBack(ondate);



    }
    /**
     * show accounts balances and let modify it
     */
    private void showCurrentBalances(final ArrayList<ContoObj> conti2){

        final EditText c1Balance = findViewById(R.id.id_c1_balance);
        final EditText c2Balance = findViewById(R.id.id_c2_balance);
        final Button c1Set = findViewById(R.id.id_btn_set_c1);
        final Button c2Set = findViewById(R.id.id_btn_set_c2);

        c1Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cleanString = c1Balance.getText().toString().replaceAll("[ €,.\\s]", "");
                BigDecimal newBalance = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                helper.updateBalance("c1", newBalance);
                conti2.get(0).setSaldoConto(newBalance);
                adapterDailyTransaction.updateResults();

            }
        });
        c2Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cleanString = c2Balance.getText().toString().replaceAll("[ €,.\\s]", "");
                BigDecimal newBalance = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);

                helper.updateBalance("c2", new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
                conti2.get(1).setSaldoConto(newBalance);

                adapterDailyTransaction.notifyDataSetChanged();

            }
        });
        c1Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1Balance.setText("");
                c1Balance.addTextChangedListener(new MoneyTextWatcher(c1Balance));

            }
        });
/*
        c1Balance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String cleanString = c1Balance.getText().toString().replaceAll("[ €,.\\s]", "");
                helper.updateBalance("c1", new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
            }
        });
*/
        c2Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c2Balance.setText("");
                c2Balance.addTextChangedListener(new MoneyTextWatcher(c2Balance));
            }
        });
/*
        c2Balance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String cleanString = c2Balance.getText().toString().replaceAll("[ €,.\\s]", "");
                helper.updateBalance("c2", new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
                adapterDailyTransaction.notifyDataSetChanged();
            }
        });

*/
        String c1BalanceFormatted = NumberFormat.getCurrencyInstance().format(helper.getAccountBalance("c1"));
        String c2BalanceFormatted = NumberFormat.getCurrencyInstance().format(helper.getAccountBalance("c2"));

        c1Balance.setText(c1BalanceFormatted);
        c2Balance.setText(c2BalanceFormatted);

    }
    /**
     * Starts navigationdrawer
     */
    private void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, CreaContoActivity.class);
        final Intent allTransaction = new Intent(this, AllTransactionActivity.class);

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
                                    startActivity(allTransaction);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                adapterDailyTransaction.notifyDataSetChanged();
                adapterDailyTransaction.updateResults();
            }
        }

    }
}
