/**
 * Activity showing accountdashboard
 *
 * FIXME Rename Class?
 */
package com.example.franc.mygest.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franc.mygest.fragments.DatePickerFragment;
import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterDailyTransaction;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.fragments.MyDialogFragment;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UIController.onAccountListener, MyDialogFragment.DialogListener, View.OnClickListener {

    static RviewAdapterDailyTransaction adapterDailyTransaction;
    static MovimentoViewModel movimentoViewModel;
    private ContoViewModel mAcountsViewModel;
    DrawerLayout mDrawerLayout;
    static Calendar dateToSend;
    Button edittext;
    TextView title;

    BottomSheetBehavior sheetBehavior;

    LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);
        mAcountsViewModel = new ContoViewModel(getApplication());
        UIController onAccountModifiedListener = new UIController(this);

        edittext = findViewById(R.id.btn_mainactivity_toolbar_date);
        dateToSend = Calendar.getInstance();
        dateToSend.set(Calendar.HOUR_OF_DAY, 23);
        dateToSend.set(Calendar.SECOND, 59);
        // START MAIN RECYCLERVIEW, FAB AND ACTIONBAR
        initUi();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

        bottomSheet = findViewById(R.id.bottomsheet_mainactivity_container);

        startBottomMenu(bottomSheet);
        String formattedDate = sdf.format(dateToSend.getTime());

        title = findViewById(R.id.tv_mainactivity_bottomsheet_label);
        edittext.setText(formattedDate);
        mAcountsViewModel.setDate(dateToSend.getTime());
        mAcountsViewModel.getActiveAccounts().observe(this,
                    new Observer<List<EntityConto>>() {
            @Override
            public void onChanged(@Nullable List<EntityConto> entityContos) {

                adapterDailyTransaction.setResults(entityContos);
            }
        });

        // SET UP DATE PICKER
        showDatePicker();
    }

    public static Calendar getDateToSend(){
        return dateToSend;
    }
    private void startBottomMenu(View bottomSheet){

        // Parent activity must implements View.OnClickListener
        findViewById(R.id.view_mainactivity_scrim).setOnClickListener(this);


        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    findViewById(R.id.view_mainactivity_scrim).setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }else {
                    title.setVisibility(View.GONE);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.view_mainactivity_scrim).setVisibility(View.VISIBLE);
                findViewById(R.id.view_mainactivity_scrim).setAlpha(slideOffset);

            }
        });
        showDialog();


    }

    @Override
    public void onClick(View v) {
/*
        switch (v.getId()) {
            case R.id.bottom_sheet: {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                title.setVisibility(View.GONE);

                break;
            }
            case R.id.bg: {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                title.setVisibility(View.VISIBLE);

                break;
            }
        }
*/
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

        }else {

        }
        return super.dispatchTouchEvent(event);
    }


    /**
     * Starts UI
     */
    private void initUi(){
        ActionBar actionbar;
        FloatingActionButton fab = findViewById(R.id.fab_main);
        RecyclerView rview = findViewById(R.id.rv_mainactivity_account);
        Toolbar myToolbar = findViewById(R.id.toolbar_mainactivity);

        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        // don't reuse viewholder because i need to restart everything in it everytimes rview updates
        rview.getRecycledViewPool().setMaxRecycledViews(0, 0);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_logo);

        adapterDailyTransaction = new RviewAdapterDailyTransaction(this, getApplication());
        rview.setLayoutManager(new LinearLayoutManager(this));
        rview.setAdapter(adapterDailyTransaction);

/*
        fab.setImageResource(R.drawable.ic_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showDialog();

            }
        });
*/




        startNavDrawer();

    }

    @Override
    public void onAccountUpdated(EntityConto conto) {
        mAcountsViewModel.update(conto);

    }


    @Override
    public void onAccountCreated(EntityConto conto) {

    }

    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        final DatePickerFragment datePickerFragment;

        datePickerFragment = initDatePicker(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment.show(getFragmentManager(), "Date Picker");
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

    @Override
    public void OnTransactionAdded(String beneficiario, String importo, java.util.Date scadenza, java.util.Date saldato, String nomeConto, int idConto, @Nullable final java.util.Date endDate, String recurrence, String tipo) {
        movimentoViewModel = new MovimentoViewModel(getApplication());

        movimentoViewModel.insert(beneficiario, importo, scadenza, saldato, nomeConto, idConto, endDate, recurrence, tipo);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        showDialog();
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


    /**
     * Starts navigationdrawer
     */
    private void startNavDrawer(){
        final Intent creaConto = new Intent(this, AccountsManageActivity.class);
        final Intent allTransaction = new Intent(this, AllTransactionActivity.class);
        NavigationView navigationView = findViewById(R.id.navigationview_main);

        mDrawerLayout = findViewById(R.id.drawer_main);

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
                List<EntityConto> contos = mAcountsViewModel.getActiveAccounts(dateToSend.getTime());
                adapterDailyTransaction.setResults(contos);
*/
            }
        }

    }


    void showDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = MyDialogFragment.newInstance();
/*
        newFragment.show(ft, "dialog");
*/

        ft.add(R.id.fl_mainactivity_bottomsheet_content, newFragment).commit();

    }


}

