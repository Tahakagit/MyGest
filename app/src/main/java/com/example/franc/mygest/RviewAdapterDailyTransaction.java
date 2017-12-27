package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.DataObjectHolder> {

    private LayoutInflater mInflater;
    private Realm movimentiRealm;
    private RealmResults<DailyTransaction> mResults;
    private RealmResults<Movimento> movimentiResults;

    public RviewAdapterDailyTransaction(Context context, Realm realm, RealmResults<DailyTransaction> results) {
        this.movimentiRealm = realm;
        this.mInflater = LayoutInflater.from(context);
        setResults(results);
    }

    public RviewAdapterDailyTransaction(){

    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView scadenza;
        LinearLayout hiddenlayout;
        public DataObjectHolder(View itemView) {
            super(itemView);
            scadenza = itemView.findViewById(R.id.scadenza);

/*
            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);
*/

        }

        //todo forse qua dentro popolo il recyclerview2
        public void setData(String textscadenza){
            scadenza.setText(textscadenza);


        }

    }


/*
    public void setAll(String beneficiario, String importo, String scadenza){
        Movimento movimento = new Movimento();
        movimento.setBeneficiario(beneficiario);
        movimento.setImporto(importo);
        movimento.setScadenza(scadenza);
        movimento.setTimestamp(System.currentTimeMillis());

        movimentiRealm.beginTransaction();
        movimentiRealm.copyToRealmOrUpdate(movimento);
        movimentiRealm.commitTransaction();
        notifyDataSetChanged();
    }
*/
    private void setResults(RealmResults<DailyTransaction> results){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dailytransaction, parent, false);
        DataObjectHolder dataOHolder = new DataObjectHolder(view);
        return dataOHolder;
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
/*
        holder.hiddenlayout.setVisibility(View.GONE);
*/

        DailyTransaction dailyTransaction = mResults.get(position);
        if(dailyTransaction.getDayOfYear() != null) {
            holder.setData(dailyTransaction.getDayOfYear());
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