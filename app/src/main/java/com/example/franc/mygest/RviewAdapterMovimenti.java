package com.example.franc.mygest;

/**
 * Created by franc on 24/12/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.annotation.Nonnull;

import io.realm.RealmResults;
//questo adapter fornisce i dati alla recyclerview all'interno del dataholder della rv dei giorni con
//transazioni
public class RviewAdapterMovimenti extends RecyclerView.Adapter<RviewAdapterMovimenti.DataObjectHolder> {

    private RealmResults<Movimento> mResults;
    public RviewAdapterMovimenti(RealmResults<Movimento> results) {
        setResults(results);
    }
    public RviewAdapterMovimenti(){

    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

        Movimento movimento = mResults.get(position);
        if(movimento.getBeneficiario() != null) {
            BigDecimal raw = movimento.getImporto();
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");


            holder.setData(movimento.getBeneficiario(), importoFormatted, dayFormat.format(movimento.getScadenza()), monthFormat.format(movimento.getScadenza()).toUpperCase());
/*
            holder.setData(movimento.getBeneficiario(), String.valueOf(movimento.getTimestamp()), sdf.format(movimento.getScadenza()));
*/

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
    @Override
    public int getItemCount() {
        return mResults.size();
    }
    @Override
    public long getItemId(@Nonnull int position){ return  mResults.get(position).getTimestamp();}
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
        return new DataObjectHolder(view);
    }

    private void setResults(RealmResults<Movimento> results){
        mResults = results;
        notifyDataSetChanged();

    }
    public void deleteItemAt(long timestamp){
        RealmHelper helper = new RealmHelper();
        helper.removeMovimento(timestamp);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView dayScadenzaText;
        TextView monthScadenzaText;

        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            beneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            importo = itemView.findViewById(R.id.id_card_importo);
            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }
        public void setData(String textbeneficiario, String textimporto, String dayScadenza, String monthScadenza){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
        }
    }

}