package com.example.franc.mygest;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity{

    Realm mRealm;
    static RealmResults<DailyTransaction> realmSelectDaysWithTransactions;
    static RealmResults<Movimento> realmSelectMovimenti;
    RealmHelper helper = new RealmHelper();

    static RviewAdapterDailyTransaction adapterDailyTransaction;

    static Context context;
    static Calendar weekRange;
    static Date dateToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.navigation_drawer_main);

        weekRange = Calendar.getInstance();
        weekRange.add(Calendar.DAY_OF_MONTH, 7);

        dateToSend = weekRange.getTime();
        initUi(helper.getTransactionsUntilGroupedByAccount(dateToSend));
        showDatePicker();

    }

    //START USER INTERFACE
    private void initUi(ArrayList<String> conti){

        // todo remove expanding toolbar
        RecyclerView rview = findViewById(R.id.recyclerview_filter);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        final Intent intent = new Intent(this, DialogActivity.class);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(conti);
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
                adapterDailyTransaction.setResults(helper.getTransactionsUntilGroupedByAccount(dateToSend));
            }
        };
        date.setCallBack(ondate);



    }

    //NAVIGATION DRAWER
    public void startNavDrawer(){
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
