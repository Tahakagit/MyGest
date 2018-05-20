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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityMovimento;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterAllTransactions extends RecyclerView.Adapter<RviewAdapterAllTransactions.DataObjectHolder> {

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
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);
        return new DataObjectHolder(view);
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

/*
        Movimento movimento = mResults.get(position);
*/
        EntityMovimento current = mResults.get(position);

        if(current != null) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            BigDecimal raw = new BigDecimal(String.valueOf(current.getImporto()));
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);

            holder.setData(current.getBeneficiario(), current.getImporto(), dayFormat.format(current.getScadenza()), monthFormat.format(current.getScadenza()).toUpperCase());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(holder.hiddenlayout.getVisibility()==View.GONE){
                    holder.hiddenlayout.setVisibility(View.VISIBLE);
                }
                else {
                    holder.hiddenlayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }
    void updateResults(){
        notifyDataSetChanged();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView textBeneficiario;
        TextView textImporto;
        TextView dayScadenzaText;
        TextView monthScadenzaText;
        Group hiddenlayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textImporto = itemView.findViewById(R.id.id_card_importo);
            textBeneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);

            hiddenlayout = itemView.findViewById(R.id.hidden_transaction_group);

        }

        public void setData(String textbeneficiario, String textimporto, String dayScadenza, String monthScadenza){
            textImporto.setText(textimporto);
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
            textBeneficiario.setText(textbeneficiario);


        }

    }

}