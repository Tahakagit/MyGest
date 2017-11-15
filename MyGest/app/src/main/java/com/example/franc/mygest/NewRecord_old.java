package com.example.franc.mygest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by franc on 02/11/2017.
 */

public class NewRecord_old extends DialogFragment{
    MyInterface mCallback;

    public interface MyInterface {
        public void save(String beneficiario);
    }


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

        builder.setPositiveButton(R.string.insert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        String input = beneficiario.getText().toString();
                        mCallback.save(input);
                    }
                })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it

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

