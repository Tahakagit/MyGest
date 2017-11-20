package com.example.franc.mygest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.w3c.dom.Element;

import java.util.Date;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by franc on 02/11/2017.
 */

public class NewRecord extends DialogFragment{
    MyInterface mCallback;

    public interface MyInterface {
        public void insBeneficiario(String beneficiario);
        public void insImporto(String importo);
        public void insScadenza(Date beneficiario);
        public void insConto(String beneficiario);
        public void insTipo(String beneficiario);


    }








    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Context context = this.getContext();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        final View viewbeneficiario = inflater.inflate(R.layout.dial_beneficiario, null);
        final View viewimporto = inflater.inflate(R.layout.dial_importo, null);
        //final View viewscadenza = inflater.inflate(R.layout.dial_scadenza, null);
        //final View viewconto = inflater.inflate(R.layout.dial_conto, null);
        //final View viewtipo = inflater.inflate(R.layout.dial_tipo, null);


        final EditText beneficiario = (EditText) viewbeneficiario.findViewById(R.id.inputBeneficiario);
        final EditText importo = (EditText) viewimporto.findViewById(R.id.inputBeneficiario);
        //final DatePicker scadenza = (DatePicker) viewscadenza.findViewById(R.id.inputBeneficiario);
        //final EditText conto = (EditText) viewconto.findViewById(R.id.inputBeneficiario);
        // final EditText tipo = (EditText) viewtipo.findViewById(R.id.inputBeneficiario);
        //beneficiario.addTextChangedListener(new MoneyTextWatcher(beneficiario));



        return builder.create();
    }

    public Dialog startDial(AlertDialog.Builder builder, View dialogview, final EditText field){
        builder.setView(dialogview);
        builder.setPositiveButton(R.string.inserisci, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String input = field.getText().toString();
                mCallback.insBeneficiario(input);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyInterface) {
            mCallback = (MyInterface) context;
        }
    }
    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }



    // make sure the Activity implemented it

}

