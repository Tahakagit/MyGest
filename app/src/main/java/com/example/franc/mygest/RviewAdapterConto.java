package com.example.franc.mygest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 20/12/2017.
 */

public class RviewAdapterConto extends RecyclerView.Adapter<RviewAdapterConto.DataObjectHolder> {
    private Realm contiRealm;
    private RealmResults<Conto> mResults;
    RealmHelper helper = new RealmHelper();

    public RviewAdapterConto(Context context, Realm realm, RealmResults<Conto> results) {
        this.contiRealm = realm;
        setResults(results);
    }

    public RviewAdapterConto(){

    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView saldo;
        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.cardNomeconto);
            saldo = itemView.findViewById(R.id.cardSaldoconto);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }
        public void setData(String textbeneficiario, String textimporto){
            nome.setText(textbeneficiario);
            saldo.setText(textimporto);
        }
    }


    public void setConto(final Conto conto){
        contiRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Conto s=realm.copyToRealm(conto);
            }
        });
        notifyDataSetChanged();
    }
    private void setResults(RealmResults<Conto> results){
        mResults = results;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mResults.size();
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

        Conto conto = mResults.get(position);
        if(conto.getNomeConto() != null) {
            BigDecimal rawBalanceFromDb = helper.getAccountBalance(conto.getNomeConto());
            String balance = NumberFormat.getCurrencyInstance().format(rawBalanceFromDb);
            holder.setData(conto.getNomeConto(), balance);
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
