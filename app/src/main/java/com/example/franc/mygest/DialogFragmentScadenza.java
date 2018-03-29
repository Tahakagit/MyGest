package com.example.franc.mygest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DialogFragmentScadenza extends Fragment{

    EditText startDateText;
    EditText endDateText;
    Spinner spinner;

    String recurrence;
    Date startDateToSend = null;
    Date endDateToSend;

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
                if (startDateToSend!= null){
                    ((DialogActivity)getActivity()).getScadenza(startDateToSend, endDateToSend, recurrence);
                }else{
                    Log.w("DialogFragmentBen", "WARNING:: beneficiario null");
                    displayPopupWindow(getContext(), startDateText, "Inserisci la data di scadenza!");
                    startDateText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }


            }
        } );

        prev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ((DialogActivity)getActivity()).goBack();

            }
        } );

        showStartDatePicker(view);
        showEndDatePicker(view);
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

                //date object from spinner
                startDateToSend = c.getTime();
                //formatted date string from spinner
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

                //date object from spinner
                endDateToSend = c.getTime();
                //formatted date string from spinner
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    /**
     * Starts Alert
     * @param anchorView Anchor view
     * @param helpText Custom Message
     */
    private void displayPopupWindow(Context context, View anchorView, String helpText) {
        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);

        LinearLayout ll = new LinearLayout(context);
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(context);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(linLayoutParam);


        tv.setText(helpText);
        tv.setLayoutParams(linLayoutParam);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(ContextCompat.getColor(context, R.color.red));
        tv.setTextSize(25);
/*
        tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
*/
        ll.addView(tv);

        PopupWindow popup = new PopupWindow(ll, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setContentView(ll);


/*
        popup.setHeight(ll.getHeight());
        popup.setWidth(ll.getWidth());
*/
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        Drawable drawBackground = ContextCompat.getDrawable(context, R.drawable.dialog_background);
        ;
        popup.setBackgroundDrawable(drawBackground);
        popup.showAtLocation(anchorView, Gravity.TOP, 150, 0);
    }

}