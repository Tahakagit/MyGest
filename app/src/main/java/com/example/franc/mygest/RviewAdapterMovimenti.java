package com.example.franc.mygest;

/**
 * Created by franc on 24/12/2017.
 */
import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import io.realm.RealmResults;
public class RviewAdapterMovimenti extends RecyclerView.Adapter<RviewAdapterMovimenti.DataObjectHolder> {

    private List<EntityMovimento> mResults;
    Application app;
    MovimentoViewModel transVM;

    RviewAdapterMovimenti(Application app){
        this.app = app;
        transVM = new MovimentoViewModel(app);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

        EntityMovimento movimento = mResults.get(position);
        if(movimento != null) {
            BigDecimal raw = new BigDecimal(String.valueOf(movimento.getImporto()));
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.ITALY);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ITALY);

            holder.setData(movimento.getId(), movimento.getBeneficiario(), importoFormatted,
                        dayFormat.format(movimento.getScadenza()),
                        monthFormat.format(movimento.getScadenza()).toUpperCase());
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

    }
    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }

    @Override
    public long getItemId(int position){ return  mResults.get(position).getId();}

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
        return new DataObjectHolder(view);
    }

    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }
    void deleteItemAt(int position){
        transVM.deleteTransactionById(mResults.get(position).getId());
/*
        mResults.remove(position);
*/
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView dayScadenzaText;
        TextView monthScadenzaText;

        int transId;
        LinearLayout hiddenlayout;
        DataObjectHolder(View itemView) {
            super(itemView);
            beneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            importo = itemView.findViewById(R.id.id_card_importo);
            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }
        void setData(int id, String textbeneficiario, String textimporto, String dayScadenza, String monthScadenza){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
            transId = id;
        }
        public int getTransId(){
            return transId;
        }
    }

}