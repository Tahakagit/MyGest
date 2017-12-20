package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

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

public class RviewAdapter extends RecyclerView.Adapter<RviewAdapter.DataObjectHolder> {

    private LayoutInflater mInflater;
    private Realm mRealm;
    private RealmResults<Movimento> mResults;

    public RviewAdapter(Context context, Realm realm, RealmResults<Movimento> results) {
        this.mRealm = realm;
        this.mInflater = LayoutInflater.from(context);
        setResults(results);
    }

    public RviewAdapter(){

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


    public void setAll(String beneficiario, String importo){
        Movimento movimento = new Movimento();
        movimento.setBeneficiario(beneficiario);
        movimento.setImporto(importo);
/*
        movimento.setScadenza(scadenza);
*/
        movimento.setTimestamp(System.currentTimeMillis());

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(movimento);
        mRealm.commitTransaction();
        notifyDataSetChanged();
    }
    private void setResults(RealmResults<Movimento> results){
        mResults = results;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }
    @Override
    public long getItemId(int position){ return  mResults.get(position).getTimestamp();}
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
        DataObjectHolder dataOHolder = new DataObjectHolder(view);
        return dataOHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

        Movimento movimento = mResults.get(position);
        if(movimento.getBeneficiario() != null) {
            holder.setData(movimento.getBeneficiario(), movimento.getImporto(), movimento.getScadenza());
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