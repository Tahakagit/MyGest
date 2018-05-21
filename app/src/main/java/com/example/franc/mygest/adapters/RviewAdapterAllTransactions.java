package com.example.franc.mygest.adapters;

/**
 * Created by franc on 28/10/2017.
 */

import android.content.Context;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityMovimento;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterAllTransactions extends RecyclerView.Adapter<RviewAdapterAllTransactions.TransactionViewHolder> {

    List<EntityMovimento> mResults;
    private Context context;
    static RviewAdapterMovimenti adapterMovimenti;
    public RviewAdapterAllTransactions(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }
    @Override
    public long getItemId(int position){ return  0;}
    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);
        return new TransactionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final TransactionViewHolder transactionHolder, final int position) {

        EntityMovimento current = mResults.get(position);

        if(current != null) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.ITALY);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ITALY);
            BigDecimal raw = new BigDecimal(String.valueOf(current.getImporto()));
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);

            transactionHolder.setData(current.getBeneficiario(), importoFormatted, dayFormat.format(current.getScadenza()), monthFormat.format(current.getScadenza()).toUpperCase());
            transactionHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(transactionHolder.hiddenlayout.getVisibility()==View.GONE){
                        transactionHolder.hiddenlayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        transactionHolder.hiddenlayout.setVisibility(View.GONE);
                    }
                }
            });

        }

    }

    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView textBeneficiario;
        TextView textImporto;
        TextView dayScadenzaText;
        TextView monthScadenzaText;
        Group hiddenlayout;

        TransactionViewHolder(View itemView) {
            super(itemView);
            textImporto = itemView.findViewById(R.id.id_card_importo);
            textBeneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);

            hiddenlayout = itemView.findViewById(R.id.hidden_transaction_group);

        }

        void setData(String textbeneficiario, String textimporto, String dayScadenza, String monthScadenza){
            textImporto.setText(textimporto);
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
            textBeneficiario.setText(textbeneficiario);
        }
    }
}