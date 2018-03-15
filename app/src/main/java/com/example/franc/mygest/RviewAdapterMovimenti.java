package com.example.franc.mygest;

/**
 * Created by franc on 24/12/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
//questo adapter fornisce i dati alla recyclerview all'interno del dataholder della rv dei giorni con
//transazioni
public class RviewAdapterMovimenti extends RecyclerView.Adapter<RviewAdapterMovimenti.DataObjectHolder> {

    private RealmResults<Movimento> mResults;
    Realm mRealm;

    public RviewAdapterMovimenti(RealmResults<Movimento> results) {
        setResults(results);
    }

    public RviewAdapterMovimenti(){

    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView scadenza;
        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            beneficiario = itemView.findViewById(R.id.beneficiario);
            importo = itemView.findViewById(R.id.importo);
            scadenza = itemView.findViewById(R.id.scadenza);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }

        public void setData(String textbeneficiario, String textimporto){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
        }
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
    public long getItemId(int position){ return  0;}
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
        return new DataObjectHolder(view);
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);

        Movimento movimento = mResults.get(position);
        if(movimento.getBeneficiario() != null) {
            holder.setData(movimento.getBeneficiario(), movimento.getImporto());
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