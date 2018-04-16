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

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;
//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterAllTransactions extends RecyclerView.Adapter<RviewAdapterAllTransactions.DataObjectHolder> {

    RealmResults<Movimento> mResults;
    private Context context;
    static RviewAdapterMovimenti adapterMovimenti;
    final Realm mRealm = Realm.getDefaultInstance();

    RviewAdapterAllTransactions(Context context, RealmResults<Movimento> results) {
        setResultsRealm(results);
        this.context = context;
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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        if(mResults.get(position) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");

            holder.setData(mResults.get(position).getImporto().toString(), mResults.get(position).getBeneficiario(), sdf.format(mResults.get(position).getScadenza()));
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
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRealm.close();

    }

    void setResultsRealm(RealmResults<Movimento> results){
        mResults = results;
        notifyDataSetChanged();
    }
    void updateResults(){
        notifyDataSetChanged();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView textBeneficiario;
        TextView textImporto;
        TextView textScadenza;
        LinearLayout hiddenlayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textImporto = itemView.findViewById(R.id.id_card_importo);
            textBeneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            textScadenza = itemView.findViewById(R.id.id_card_scadenza_day);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }

        public void setData(String importo, String beneficiario, String scadenza){
            textImporto.setText(importo);
            textScadenza.setText(scadenza);
            textBeneficiario.setText(beneficiario);


        }

    }

}