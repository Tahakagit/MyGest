package com.example.franc.mygest;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.CardView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 20/12/2017.
 */

public class RviewAdapterConto extends RecyclerView.Adapter<RviewAdapterConto.DataObjectHolder> {
    private LayoutInflater mInflater;
    private Realm contiRealm;
    private RealmResults<Conto> mResults;

    public RviewAdapterConto(Context context, Realm realm, RealmResults<Conto> results) {
        this.contiRealm = realm;
        this.mInflater = LayoutInflater.from(context);
        setResults(results);
    }

    public RviewAdapterConto(){

    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView scadenza;
        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            beneficiario = (TextView) itemView.findViewById(R.id.beneficiario);
            importo = (TextView) itemView.findViewById(R.id.importo);
            scadenza = (TextView) itemView.findViewById(R.id.scadenza);

            hiddenlayout = (LinearLayout) itemView.findViewById(R.id.hiddenlayout);

        }
        public void setData(String textbeneficiario, String textimporto, String textscadenza){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            scadenza.setText(textscadenza);
        }
    }


    public void setAll(String nome, String saldo, String colore){
        Conto conto = new Conto();
        conto.setNomeConto(nome);
        conto.setSaldoConto(saldo);
        conto.setColoreConto(colore);

        contiRealm.beginTransaction();
        contiRealm.copyToRealmOrUpdate(conto);
        contiRealm.commitTransaction();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
        RviewAdapterConto.DataObjectHolder dataHolder = new RviewAdapterConto.DataObjectHolder(view);
        return dataHolder;
    }

    @Override
    public void onBindViewHolder(final RviewAdapterConto.DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

        Conto conto = mResults.get(position);
        if(conto.getNomeConto() != null) {
            holder.setData(conto.getNomeConto(), conto.getSaldoConto(), conto.getColoreConto());
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
