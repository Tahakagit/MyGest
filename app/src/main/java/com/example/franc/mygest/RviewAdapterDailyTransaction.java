package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.DataObjectHolder> {

    ArrayList<ContoObj> mResults;

    static RviewAdapterMovimenti adapterMovimenti;
    final Realm mRealm = Realm.getDefaultInstance();


/*
    RviewAdapterDailyTransaction(ArrayList<String> results) {
        setResults(results);
    }
*/


    RviewAdapterDailyTransaction(ArrayList<ContoObj> mResults) {
        setResultsRealm(mResults);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView accountBalance;
        TextView accountName;
        LinearLayout hiddenlayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.id_account_name);
            accountBalance = itemView.findViewById(R.id.id_account_balance);

            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }

        //todo forse qua dentro popolo il recyclerview2
        public void setData(String textscadenza, String balance){
            accountName.setText(textscadenza);
            accountBalance.setText(balance);


        }

    }


    void setResultsRealm(ArrayList<ContoObj> results){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dailytransaction, parent, false);
        return new DataObjectHolder(view);
    }
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        RecyclerView rviewMovimenti = holder.itemView.findViewById(R.id.rv_transaction);

        RealmHelper helper = new RealmHelper();

        String account = mResults.get(position).getNomeConto();

        RealmResults<Movimento> movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.weekRange.getTime(), account);

        adapterMovimenti = new RviewAdapterMovimenti(movs);
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        rviewMovimenti.setAdapter(adapterMovimenti);
/**
 *
 *
 *todo foreach movs somma gli importi, ritrova il saldo e sottrai gli importi
 */

        if(mResults.get(position) != null) {
            holder.setData(mResults.get(position).getNomeConto(), NumberFormat.getCurrencyInstance().format(mResults.get(position).getSaldoConto()));
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

    public void updateData(){
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRealm.close();

    }
}