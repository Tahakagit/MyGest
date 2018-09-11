package com.example.franc.mygest.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.franc.mygest.R;
import com.example.franc.mygest.activities.DialogActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DialogFragmentScadenza extends Fragment{

    EditText startDateText;
    EditText endDateText;
    EditText saldatoDateText;

    Spinner spinner;

    String recurrence;
    Date startDateToSend;
    Date endDateToSend;
    Date saldatoDateToSend;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_fragment_scadenza, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Button next;
        Button prev;

        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        // or  (ImageView) view.findViewById(R.id.foo);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).getScadenza(startDateToSend, endDateToSend, saldatoDateToSend, recurrence);

            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );

        showStartDatePicker(view);
        showEndDatePicker(view);
        showSaldatoDatePicker(view);
        getRecurrenceFromSpinner(view);

    }

    /**
     * Starts date selection, waits for user choice and gets back selected date
     * todo trasformare editext in button?
     */
    private void showStartDatePicker(View view) {
        startDateText = view.findViewById(R.id.select_date);

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



                startDateText.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        startDateText.setOnClickListener(new View.OnClickListener() {
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
        endDateText = view.findViewById(R.id.select_end_date);

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



                endDateText.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        endDateText.setOnClickListener(new View.OnClickListener() {
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
        saldatoDateText = view.findViewById(R.id.select_saldato);

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



                saldatoDateText.setText(formattedDate);
            }
        };
        date.setCallBack(ondate);

        saldatoDateText.setOnClickListener(new View.OnClickListener() {
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
        spinner = view.findViewById(R.id.spinner_recurrence);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recurrence =  parent.getItemAtPosition(position).toString();
                if (recurrence.equalsIgnoreCase("nessuna")){
                    endDateText.setVisibility(View.GONE);
                }else{
                    endDateText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}