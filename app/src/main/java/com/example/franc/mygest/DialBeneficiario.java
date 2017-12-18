package com.example.franc.mygest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.Date;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by franc on 02/11/2017.
 */

public class DialBeneficiario extends DialogFragment{

    public interface MyInterface {
        public void insBeneficiario(String beneficiario);
        public void insImporto(String importo);
        public void insScadenza(Date scadenza);
        public void insConto(String conto);
        public void insTipo(String tipo);
    }
    MyInterface mCallback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final Context context = this.getContext();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());




        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View yourCustomView = inflater.inflate(R.layout.new_record, null);
        final EditText beneficiario = (EditText) yourCustomView.findViewById(R.id.inputBeneficiario);
        //beneficiario.addTextChangedListener(new MoneyTextWatcher(beneficiario));


        builder.setView(yourCustomView);

        return new AlertDialog.Builder(getActivity()).setView(yourCustomView)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
/*
                                ((MainActivity)getActivity()).getBeneficiario(beneficiario.getText().toString());
                                ((MainActivity)getActivity()).saveData();
*/

                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();


/*
        builder.setPositiveButton(R.string.insert, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {



                String input = beneficiario.getText().toString();
                mCallback.insBeneficiario(input);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it

        return builder.create();
*/

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
            //Todo sembra la strada giusta ma bisogna trovare il modo di farlo eseguire dopo il dialog
            //((MainActivity)getActivity()).sendData((String) ((MainActivity) getActivity()).beneficiario2,(String) ((MainActivity) getActivity()).importo2);

        }
    }



/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyInterface) {
            mCallback = (MyInterface) context;
        }
    }
*/
    @Override
    public void onDetach() {
/*
        mCallback = null;
*/
        super.onDetach();
    }



    // make sure the Activity implemented it

}

