package com.example.franc.mygest.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityConto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by franc on 20/12/2017.
 */

public class RviewAdapterConto extends RecyclerView.Adapter<RviewAdapterConto.DataObjectHolder> {
    List<EntityConto> mResults;
    private Context context;


    public RviewAdapterConto(Context context) {
        this.context = context;
    }

    public RviewAdapterConto(){

    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView saldo;
        CardView cv;
        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.cardNomeconto);
            saldo = itemView.findViewById(R.id.cardSaldoconto);
            cv = itemView.findViewById(R.id.cv_account_dashboard);
            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }
        public void setData(String textbeneficiario, String textimporto){
            nome.setText(textbeneficiario);
            saldo.setText(textimporto);
        }
    }


    public void setResults(List<EntityConto> results){
        mResults = results;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }
/*
    public long getItemId(int position){ return  mResults.get(position).getTimestamp();}
*/
    public RviewAdapterConto.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_conti, parent, false);
        return new RviewAdapterConto.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final RviewAdapterConto.DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

/*
        Conto conto = mResults.get(position);
*/
        EntityConto current = mResults.get(position);

        if(current.getNomeConto() != null) {
            BigDecimal rawBalanceFromDb = new BigDecimal(String.valueOf(current.getSaldoConto()));
            String balance = NumberFormat.getCurrencyInstance().format(rawBalanceFromDb);
            holder.setData(current.getNomeConto(), balance);
            holder.cv.setCardBackgroundColor(mResults.get(position).getColoreConto());

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

}
