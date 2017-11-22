package com.example.franc.mygest;

/**
 * Created by franc on 21/11/2017.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.*;


public class DialScadenza extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        String formattedDate = sdf.format(c.getTime());
        ((MainActivity) getActivity()).getScadenza(formattedDate);

    }
}





/*
public class DialScadenza extends DialogFragment{

    private StringBuilder tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

*/
/*
        setCurrentDateOnView();
        addListenerOnButton();
*//*


    }

    // display current date
*/
/*
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        dpResult.init(year, month, day, null);

    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btnChangeDate);

        btnChangeDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

    }
*//*


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //final EditText importo = (EditText) yourCustomView.findViewById(R.id.inputimporto);

        return new DatePickerDialog(getContext(), datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            tvDisplayDate =  (new StringBuilder().append(month +1).append("-").append(day).append("-").append(year).append(" "));
            //((MainActivity) getActivity()).getScadenza(tvDisplayDate.toString());

            // set selected date into datepicker also
            //dpResult.init(year, month, day, null);
        }

        //Todo getactivity interface
//        return new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton){
//                ((MainActivity) getActivity()).getScadenza(tvDisplayDate);
//
//            }
*/
/*
            StringBuilder strbuilder = new StringBuilder();
*//*

                // set selected date into textview


    };

}
*/
