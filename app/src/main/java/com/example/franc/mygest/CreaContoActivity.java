package com.example.franc.mygest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class CreaContoActivity extends AppCompatActivity {

    Realm realm;
    RviewAdapterConto adapter;
    RecyclerView rv;
    static RealmResults<Conto> realmSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creaconto_activity_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        rv= findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        realm=Realm.getDefaultInstance();
        realmSelect = realm.where(Conto.class).findAllAsync();

        adapter=new RviewAdapterConto(this,realm,realmSelect);
        rv.setAdapter(adapter);

        realmSelect.addChangeListener(new RealmChangeListener<RealmResults<Conto>>() {
            @Override
            public void onChange(RealmResults<Conto> mResults) {
                adapter.notifyDataSetChanged();
                Toast.makeText(CreaContoActivity.this, "On change triggered", Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });
    }

    //DISPLAY INPUT DIALOG AND SAVE ACCOUNT
    private void displayInputDialog()    {
        final Dialog d=new Dialog(this);
        d.setTitle("Save to Realm");

        //Todo: creare layout dialog inserimento conto
        d.setContentView(R.layout.input_dialog_creaconto);
        final EditText nomeConto= d.findViewById(R.id.nomeconto);
        final EditText saldoConto= d.findViewById(R.id.saldoconto);

        Button saveBtn= d.findViewById(R.id.saveconto);

        d.show();

        saldoConto.addTextChangedListener(new MoneyTextWatcher(saldoConto));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE
                RealmHelper helper = new RealmHelper();

                String cleanString = saldoConto.getText().toString().replaceAll("[ €,.\\s]", "");
                if (!cleanString.matches("")&&!nomeConto.getText().toString().matches("")) {
                    helper.saveConto(nomeConto.getText().toString(), new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));

                    //REFRESH ADAPTER
                    adapter.notifyDataSetChanged();
                    d.dismiss();

                }
                else if(cleanString.matches("")) {
                    Log.w(this.getClass().getName(), "WARNING:: saldo null");
                    saldoConto.setHintTextColor(ContextCompat.getColor(CreaContoActivity.this, R.color.red));
                    displayPopupWindow(getBaseContext(), nomeConto, "Inserisci il saldo!");

                }
                else if (saldoConto.getText().toString().matches("")){
                    Log.w(this.getClass().getName(), "WARNING:: nome conto null");
                    nomeConto.setHintTextColor(ContextCompat.getColor(CreaContoActivity.this, R.color.red));
                    displayPopupWindow(getBaseContext(), nomeConto, "Inserisci il nome del Conto!");

                }
                //CLEAN FORM FIELDS
/*
                nomeConto.setText("");
                saldoConto.setText("");
*/

                //CLOSE DIALOG
            }
        });
    }

    /**
     * Starts Alert
     * @param context Context
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
