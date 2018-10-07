package com.example.franc.mygest.fragments;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.franc.mygest.MoneyTextWatcher;
import com.example.franc.mygest.R;
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

public class MyDialogFragment extends DialogFragment {
    Date startDateToSend = null;
    Date endDateToSend = null;
    Date saldatoDateToSend = null;
    String beneficiario;
    Spinner spinner;

    String recurrence;
    EditText end;
    Spinner accountSpinner;
    Spinner typeSpinner;
    String contos;
    String type;
    String beneficiarioValue;
    static BigDecimal importoValue = null;





    private DialogListener mListener;
    public static MyDialogFragment newInstance() {
        MyDialogFragment f = new MyDialogFragment();
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mListener = (DialogListener) context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }

    }

    public void onAttach(AppCompatActivity activity) {
        try {
            this.mListener = (DialogListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }


    public interface DialogListener{
        void OnTransactionAdded(String beneficiario, String importo, java.util.Date scadenza, java.util.Date saldato, String nomeConto, int idConto, @Nullable final java.util.Date endDate, String recurrence, String tipo);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
            EditText scadenza = view.findViewById(R.id.select_date2);
*/
        EditText importo = view.findViewById(R.id.inputimporto2);
        AutoCompleteTextView beneficiario = view.findViewById(R.id.inputBeneficiario);
        ImageView save = view.findViewById(R.id.btn_save_transaction);
        importo.addTextChangedListener(new MoneyTextWatcher(importo));
        MovimentoViewModel mWordViewModel = new MovimentoViewModel(getActivity().getApplication());

        TextView title = view.findViewById(R.id.id_menu_bottom_insert);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mWordViewModel.getKnownBeneficiari());
        beneficiario.setAdapter(adapter);
        showStartDatePicker(view);
        showEndDatePicker(view);
        showSaldatoDatePicker(view);
        getRecurrenceFromSpinner(view);
        accountSpinner = view.findViewById(R.id.spinner_accounts2);
        typeSpinner = view.findViewById(R.id.spinner_transaction_types2);

/*
        List<String> list = new ArrayList<>();
*/
        populateAccountSpinner(accountSpinner);
        populateTypeSpinner(typeSpinner);


/*
            EntityMovimento mov = new EntityMovimento(beneficiario, importo, scadenza.getTime(), saldato, idConto, nomeConto, endDate, tipo);
*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cleanString = importo.getText().toString().replaceAll("[ €,.\\s]", "");

                if(!cleanString.matches(""))
                    importoValue = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
                else{
                    importo.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));

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

                ContoViewModel mContoViewModel = new ContoViewModel(getActivity().getApplication());
                int accountId = mContoViewModel.getAccountIdByName(contos).getId();

                mListener.OnTransactionAdded(beneficiarioValue, importoValue.toString(), startDateToSend, saldatoDateToSend, contos, accountId, endDateToSend, recurrence, type);


                dismiss();
            }
        });


    }

    void populateAccountSpinner(Spinner spinner){
        Application appCtx = getActivity().getApplication();
        ContoViewModel contoVM = new ContoViewModel(appCtx);

        ArrayList<String> list = new ArrayList<>();
        List<EntityConto> arraylist = new ArrayList<>();
        arraylist = contoVM.getAllAccountsList();
        for (EntityConto s:arraylist) {
            list.add(s.getNomeConto());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listRec);
        spinner = view.findViewById(R.id.recurrence);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recurrence =  parent.getItemAtPosition(position).toString();
                if (recurrence.equalsIgnoreCase("nessuna")){
                    end.setVisibility(View.GONE);
                }else{
                    end.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}

