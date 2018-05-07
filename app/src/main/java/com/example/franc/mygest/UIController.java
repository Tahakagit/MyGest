package com.example.franc.mygest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.franc.mygest.adapters.RviewAdapterConto;
import com.example.franc.mygest.adapters.RviewAdapterDailyTransaction;
import com.example.franc.mygest.persistence.EntityConto;

import java.math.BigDecimal;


/**
 * Created by franc on 18/04/2018.
 */

public class UIController {
    RviewAdapterConto mAdapterConti;
    RviewAdapterDailyTransaction mAdapterDaily;
    AppCompatActivity mActivity;
    Context mContext;
    onAccountCreatedListener iface;

    int selectedColor = R.color.blue;

    public UIController(RviewAdapterConto adapterConti, Context context){
        mActivity = (AppCompatActivity) context;
        mContext = context;
        mAdapterConti = adapterConti;
        iface = (onAccountCreatedListener) context;
    }
    public UIController(Context context){
        mContext = context;
        iface = (onAccountCreatedListener) context;

    }
    public UIController(Context context, RviewAdapterDailyTransaction adapter){
        mContext = context;
        mAdapterDaily = adapter;
    }

    /**
     * Starts conti rv
     */
    public void initUi(){

        Toolbar toolbar = mActivity.findViewById(R.id.toolbar_creacontoactivity);
        RecyclerView rv = mActivity.findViewById(R.id.rv_account_manage);
        FloatingActionButton fab = mActivity.findViewById(R.id.fab_account_create);
        mAdapterConti = new RviewAdapterConto(mActivity);

        mActivity.setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAccountDialog(null, null, 0);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(mAdapterConti);
    }

    //DISPLAY INPUT DIALOG AND SAVE ACCOUNT
    public void displayAccountDialog(@Nullable final String nomeConto, @Nullable final String saldoConto, @Nullable final int coloreConto )    {
        final Dialog d = new Dialog(mContext);

        d.setTitle("Save to Realm");
        d.setContentView(R.layout.input_dialog_creaconto);

        Button saveBtn= d.findViewById(R.id.saveconto);

        d.show();
        final EditText nomeContoTxt= d.findViewById(R.id.nomeconto);
        final EditText saldoContoTxt= d.findViewById(R.id.saldoconto);
        final ColorPickerDialog colorDialog = displayColorsDialog(d);

        if (nomeConto!= null){
            nomeContoTxt.setText(nomeConto);
        }
        if (saldoConto != null){
            saldoContoTxt.setText(saldoConto.toString());
        }
        if (coloreConto != 0){
            selectedColor = coloreConto;
        }
        colorDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
            }
        });

        saldoContoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saldoContoTxt.setText("");
            }
        });
        saldoContoTxt.addTextChangedListener(new MoneyTextWatcher(saldoContoTxt));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cleanString = saldoContoTxt.getText().toString().replaceAll("[ €,.\\s]", "");
                if (checkForInputError(cleanString, nomeContoTxt, saldoContoTxt)){

                    EntityConto conto = new EntityConto(nomeContoTxt.getText().toString(),
                                new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR).toString(),
                                selectedColor);

                    iface.onAccountCreated(conto);
                    d.dismiss();
                }
            }
        });
    }

    public interface onAccountCreatedListener{
        void onAccountCreated(EntityConto conto);
    }

    /**
     * Checking input fields FIXME switch to setError
     * @param cleanstring input string to analize
     * @param nomeConto edittext to check
     * @param saldoConto editext to check
     * @return true if no errors
     */
    private boolean checkForInputError(String cleanstring, EditText nomeConto, EditText saldoConto){
        if(cleanstring.matches("")) {
            Log.w(this.getClass().getName(), "WARNING:: saldo null");
            displayErrorAlert(mContext, nomeConto, "Inserisci il saldo!");
            saldoConto.setError("Inserisci il saldo!");
            return false;
        }
        else if(nomeConto.getText().toString().matches("")){
            Log.w(this.getClass().getName(), "WARNING:: nome conto null");
            nomeConto.setHintTextColor(ContextCompat.getColor(mContext, R.color.red));
            displayErrorAlert(mContext, nomeConto, "Inserisci il nome del EntityConto!");
            return false;
        }else{
            return true;
        }
    }

    /**
     * Display color picker dialog
     * @param v
     * @return
     */
    private ColorPickerDialog displayColorsDialog(Dialog v){
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        int colors[] = {ContextCompat.getColor(mContext, R.color.blue_bg_black_text),
                ContextCompat.getColor(mContext, R.color.brown_bg_black_text),
                ContextCompat.getColor(mContext, R.color.green_bg_black_text),
                ContextCompat.getColor(mContext, R.color.grey_bg_black_text),
                ContextCompat.getColor(mContext, R.color.orange_bg_black_text),
                ContextCompat.getColor(mContext, R.color.pink_bg_black_text),
                ContextCompat.getColor(mContext, R.color.purple_bg_black_text),
                ContextCompat.getColor(mContext, R.color.red_bg_black_text),
                ContextCompat.getColor(mContext, R.color.yellow_bg_black_text)};
        final Button selectColorBtn = v.findViewById(R.id.btn_select_color);


        colorPickerDialog.initialize(R.string.title_dialog_select_colors, colors,
                selectedColor,
                3,
                colors.length);
        selectColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AppCompatActivity activity = (AppCompatActivity) mContext;

                colorPickerDialog.show(activity.getFragmentManager(), "colorPickerDialog");
            }
        });

        return colorPickerDialog;
    }

    /**
     * Starts custom alert anchored to a View
     * @param context Context
     * @param anchorView Anchor view
     * @param helpText Custom Message
     */
    private void displayErrorAlert(Context context, View anchorView, String helpText) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.popup_content, null);

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
        ll.addView(tv);

        PopupWindow popup = new PopupWindow(ll, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setContentView(ll);


        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        Drawable drawBackground = ContextCompat.getDrawable(context, R.drawable.dialog_background);

        popup.setBackgroundDrawable(drawBackground);
        popup.showAtLocation(anchorView, Gravity.TOP, 150, 0);
    }

}
