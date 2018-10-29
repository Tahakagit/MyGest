/**
 * Activity showing accountdashboard
 *
 */
package com.example.franc.mygest.activities;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.franc.mygest.MoneyTextWatcher;
import com.example.franc.mygest.fragments.DatePickerFragment;
import com.example.franc.mygest.R;
import com.example.franc.mygest.adapters.RviewAdapterDailyTransaction;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.fragments.MyDialogFragment;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UIController.onAccountListener, MyDialogFragment.DialogListener, View.OnClickListener {

    static RviewAdapterDailyTransaction adapterDailyTransaction;
    static MovimentoViewModel movimentoViewModel;
    private ContoViewModel mAcountsViewModel;
    DrawerLayout mDrawerLayout;
    static Calendar dateToSend;
    Button edittext;
    TextView title;
    String direction;
    String contos;
    Date startDateToSend = null;
    Date endDateToSend = null;
    Date saldatoDateToSend = null;
    String type;
    EditText end;
    Spinner spinner;
    String recurrence;
    TextInputLayout textToHide;






    BottomSheetBehavior sheetBehavior;

    LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);
        mAcountsViewModel = new ContoViewModel(getApplication());
        UIController onAccountModifiedListener = new UIController(this);

        activity = this;
        edittext = findViewById(R.id.btn_mainactivity_toolbar_date);
        dateToSend = Calendar.getInstance();
        dateToSend.set(Calendar.HOUR_OF_DAY, 23);
        dateToSend.set(Calendar.SECOND, 59);
        // START MAIN RECYCLERVIEW, FAB AND ACTIONBAR
        initUi();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

        bottomSheet = findViewById(R.id.bottomsheet_mainactivity_insert);

        startBottomMenu(bottomSheet);
        String formattedDate = sdf.format(dateToSend.getTime());

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

    Activity activity;
    public static Calendar getDateToSend(){
        return dateToSend;
    }
    private void startBottomMenu(View bottomSheet){
        ImageView save = bottomSheet.findViewById(R.id.btn_save_transaction);

        // Parent activity must implements View.OnClickListener
        findViewById(R.id.view_mainactivity_scrim).setOnClickListener(this);
        title = findViewById(R.id.tv_bottomsheet_insert_title);

        title.setVisibility(View.VISIBLE);
        textToHide = bottomSheet.findViewById(R.id.endDate);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    hideKeyboard(activity);

                    save.setVisibility(View.GONE);
                    findViewById(R.id.view_mainactivity_scrim).setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    title.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);

                }else if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    title.setVisibility(View.GONE);
                    save.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.view_mainactivity_scrim).setVisibility(View.VISIBLE);
                findViewById(R.id.view_mainactivity_scrim).setAlpha(slideOffset);

            }
        });

        Spinner accountSpinner;
        Spinner typeSpinner;
        EditText importo = bottomSheet.findViewById(R.id.inputimporto2);
        AutoCompleteTextView beneficiario = bottomSheet.findViewById(R.id.inputBeneficiario);

        importo.addTextChangedListener(new MoneyTextWatcher(importo));
        MovimentoViewModel mWordViewModel = new MovimentoViewModel(getApplication());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mWordViewModel.getKnownBeneficiari());
        beneficiario.setAdapter(adapter);
        showStartDatePicker(bottomSheet);
/*
        showEndDatePicker(view);
*/
        showSaldatoDatePicker(bottomSheet);
        getRecurrenceFromSpinner(bottomSheet);
        accountSpinner = bottomSheet.findViewById(R.id.spinner_accounts2);
        typeSpinner = bottomSheet.findViewById(R.id.spinner_transaction_types2);

        populateAccountSpinner(accountSpinner);
        populateTypeSpinner(typeSpinner);


        inOutSelector(bottomSheet);
/*
            EntityMovimento mov = new EntityMovimento(beneficiario, importo, scadenza.getTime(), saldato, idConto, nomeConto, endDate, tipo);
*/

        save.setOnClickListener(new View.OnClickListener() {
            String beneficiarioValue;
            BigDecimal importoValue = null;


            @Override
            public void onClick(View view) {

                String cleanString = importo.getText().toString().replaceAll("[ €,.\\s]", "");

                if(!cleanString.matches(""))
                    importoValue = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                else{
                    importo.setHintTextColor(ContextCompat.getColor(activity, R.color.red));

                }
                beneficiarioValue = beneficiario.getText().toString();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDateToSend);
                cal.set(Calendar.HOUR_OF_DAY, 00);
                cal.set(Calendar.MINUTE, 00);
                cal.set(Calendar.SECOND, 00);
                cal.set(Calendar.MILLISECOND, 00);
                startDateToSend = cal.getTime();

                if (endDateToSend!= null){
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(endDateToSend);
                    cal2.set(Calendar.HOUR_OF_DAY, 00);
                    cal2.set(Calendar.MINUTE, 00);
                    cal2.set(Calendar.SECOND, 00);
                    cal2.set(Calendar.MILLISECOND, 00);
                    endDateToSend = cal2.getTime();

                }

                if(saldatoDateToSend == null){
                    saldatoDateToSend = startDateToSend;
                }

                ContoViewModel mContoViewModel = new ContoViewModel(activity.getApplication());
                int accountId = mContoViewModel.getAccountIdByName(contos).getId();

                movimentoViewModel = new MovimentoViewModel(getApplication());

                movimentoViewModel.insert(beneficiarioValue, importoValue.toString(), startDateToSend, saldatoDateToSend, contos, accountId, endDateToSend, recurrence, type, direction);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                beneficiario.setText("");
                importo.setText("");


            }
        });




    }
    void inOutSelector(View view){
        Button income;
        Button outcome;

        showEndDatePicker(view);
        income = view.findViewById(R.id.btn_mainactivity_bottomsheet_income);
        outcome = view.findViewById(R.id.btn_mainactivity_bottomsheet_outcome);
        outcome.setBackgroundColor(getResources().getColor(R.color.grey_bg_soft, activity.getTheme()));
        direction = "out";
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outcome.setBackgroundColor(Color.TRANSPARENT);
                income.setBackgroundColor(getResources().getColor(R.color.grey_bg_soft, activity.getTheme()));
                direction = "in";
            }
        });
        outcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                income.setBackgroundColor(Color.TRANSPARENT);
                outcome.setBackgroundColor(getResources().getColor(R.color.grey_bg_soft, activity.getTheme()));
                direction = "out";

            }
        });


    }
    void populateAccountSpinner(Spinner spinner){
        Application appCtx = activity.getApplication();
        ContoViewModel contoVM = new ContoViewModel(appCtx);

        ArrayList<String> list = new ArrayList<>();
        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contos = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    void populateTypeSpinner(Spinner spinner){
        ArrayList<String> arraylist = new ArrayList<>();

        arraylist.add("Assegno");
        arraylist.add("SSD");
        arraylist.add("Riba");
        arraylist.add("Bonifico");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showStartDatePicker(View view) {
        final EditText scadenza = view.findViewById(R.id.select_date2);

        final DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

                //date object from accountSpinner
                startDateToSend = c.getTime();
                //formatted date string from accountSpinner
                String formattedDate = sdf.format(c.getTime());



                scadenza.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        scadenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show(getFragmentManager(), "Date Picker");
            }
        });

/*
        date.show(getFragmentManager(), "Date Picker");
*/
    }
    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showEndDatePicker(View view) {
        end = view.findViewById(R.id.select_end_date2);

        final DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

                //date object from accountSpinner
                endDateToSend = c.getTime();
                //formatted date string from accountSpinner
                String formattedDate = sdf.format(c.getTime());



                end.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show(getFragmentManager(), "Date Picker");
            }
        });

/*
        date.show(getFragmentManager(), "Date Picker");
*/
    }
    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showSaldatoDatePicker(View view) {
        EditText saldato = view.findViewById(R.id.select_saldato2);

        final DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

                //date object from accountSpinner
                saldatoDateToSend = c.getTime();
                //formatted date string from accountSpinner
                String formattedDate = sdf.format(c.getTime());



                saldato.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        saldato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show(getFragmentManager(), "Date Picker");
            }
        });

/*
        date.show(getFragmentManager(), "Date Picker");
*/
    }

    public void getRecurrenceFromSpinner(View view){
        final String[] recurrenceType = {"NESSUNA", "DAILY", "WEEKLY", "MONTHLY", "YEARLY"};
        ArrayList<String> listRec = new ArrayList<>();
        listRec.addAll(Arrays.asList(recurrenceType));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listRec);
        spinner = view.findViewById(R.id.recurrence);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recurrence =  parent.getItemAtPosition(position).toString();
                if (recurrence.equalsIgnoreCase("nessuna")){
                    textToHide.setVisibility(View.GONE);
                }else{
                    textToHide.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public void OnTransactionAdded(String beneficiario, String importo, Date scadenza, Date saldato, String nomeConto, int idConto, @Nullable final Date endDate, String recurrence, String tipo, String direction) {
        movimentoViewModel = new MovimentoViewModel(getApplication());

        movimentoViewModel.insert(beneficiario, importo, scadenza, saldato, nomeConto, idConto, endDate, recurrence, tipo, direction);
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
        final Intent predictBalances = new Intent(this, MainActivity.class);

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
                                case R.id.action_category_3:
                                    startActivity(predictBalances);
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

