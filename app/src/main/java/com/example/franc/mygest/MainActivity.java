/**
 * Activity showing accountdashboard
 *
 * FIXME Rename Class?
 */
package com.example.franc.mygest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.franc.mygest.persistence.ContoDao;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoDao;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UIController.onAccountCreatedListener{

    private RealmHelper helper = new RealmHelper();
    static RviewAdapterDailyTransaction adapterDailyTransaction;
    private ContoViewModel mAcountsViewModel;
    private MovimentoViewModel mTransViewModel;

    static Calendar dateToSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);
        mTransViewModel = new MovimentoViewModel(getApplication());
        mAcountsViewModel = new ContoViewModel(getApplication());
        UIController onAccountModifiedListener = new UIController(this);

        dateToSend = Calendar.getInstance();
        dateToSend.set(Calendar.HOUR_OF_DAY, 23);
        dateToSend.set(Calendar.SECOND, 59);
        initUi();
        mAcountsViewModel.setDate(dateToSend.getTime());
        mAcountsViewModel.getAllAccoutsByName().observe(this, new Observer<List<EntityConto>>() {
            @Override
            public void onChanged(@Nullable List<EntityConto> entityContos) {
                Log.d("on change movimenti", " trovati  " + entityContos.size() + "  conti  ");

                adapterDailyTransaction.setResults(entityContos);
                adapterDailyTransaction.notifyDataSetChanged();
            }
        });
        showDatePicker();

    }

    /**
     * Starts UI
     */
    private void initUi(){

        FloatingActionButton fab = findViewById(R.id.fab_insert_transaction);

        RecyclerView rview = findViewById(R.id.recyclerview_filter);
        Toolbar myToolbar = findViewById(R.id.toolbar_mainactivity);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        final Intent intent = new Intent(this, DialogActivity.class);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(this, getApplication());
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterDailyTransaction);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(intent, 1);
            }
        });




        startNavDrawer();

    }

    @Override
    public void onAccountCreated(EntityConto conto) {
        mAcountsViewModel.update(conto);

    }

    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        final Button edittext = findViewById(R.id.selectdate);
        final DatePickerFragment datePickerFragment;

        datePickerFragment = initDatePicker(calendar.get(calendar.DAY_OF_MONTH),calendar.get(calendar.MONTH), calendar.get(calendar.YEAR));
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                /**
                 * new date starts at 00:00 so to filter up to eg. 12/02 we need
                 * to set 13/02.
                 * dateToDisplay is going to show 12/02
                 */
/*
                Calendar dateToDisplay = Calendar.getInstance();
*/
                Calendar dateTo = Calendar.getInstance();
                dateTo.set(year, monthOfYear, dayOfMonth);
/*
                dateToDisplay.set(year, monthOfYear, dayOfMonth);
*/
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
                dateToSend.setTime(dateTo.getTime());
                dateToSend.set(Calendar.HOUR_OF_DAY, 23);
                dateToSend.set(Calendar.SECOND, 59);

                String formattedDate = sdf.format(dateTo.getTime());
                edittext.setText(formattedDate);
                mAcountsViewModel.setDate(dateToSend.getTime());
            }
        };
        datePickerFragment.setCallBack(ondate);
    }

    /**
     * Init DatePickerFragment with selected date
     * @param day default day
     * @param month default month
     * @param year default year
     * @return A DatePickerFragment
     */
    private DatePickerFragment initDatePicker(int day, int month, int year){
        final DatePickerFragment datePickerFragment = new DatePickerFragment();


        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    DrawerLayout mDrawerLayout;

    /**
     * Starts navigationdrawer
     */
    private void startNavDrawer(){
        final Intent creaConto = new Intent(this, AccountsManageActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
/*
                List<EntityConto> contos = mAcountsViewModel.getAllAccoutsByName(dateToSend.getTime());
                adapterDailyTransaction.setResults(contos);
*/
            }
        }

    }
}
