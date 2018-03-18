package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

import android.content.ClipData;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.DataObjectHolder> {

    ArrayList<ContoObj> mResults;
    static RviewAdapterMovimenti adapterMovimenti;
    final Realm mRealm = Realm.getDefaultInstance();

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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {


        // START NESTED TRANSACTIONS RECYCLERVIEW
        RecyclerView rviewMovimenti = holder.itemView.findViewById(R.id.rv_transaction);
        RealmHelper helper = new RealmHelper();

        RealmResults<Movimento> movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.weekRange.getTime(), mResults.get(position).getNomeConto());
        adapterMovimenti = new RviewAdapterMovimenti(movs);
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        rviewMovimenti.setAdapter(adapterMovimenti);
        // END NESTED RECYCLERVIEW


        // NO TRANSACTION LEFT TO WAIT
        movs.addChangeListener(new RealmChangeListener<RealmResults<Movimento>>() {
            @Override
            public void onChange(RealmResults<Movimento> realmResults) {
                if (realmResults.size() == 0){
                    mResults.remove(mResults.get(position));
                    notifyItemRemoved(position);
                }
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapterMovimenti.deleteItemAt(viewHolder.getAdapterPosition());
                MainActivity.adapterDailyTransaction.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rviewMovimenti);

        // SUM ALL UNCHECKED TRANSACTIONS
        BigDecimal totExpences = new BigDecimal("0");
        for (Movimento res:movs) {
            totExpences = totExpences.add(res.getImporto());
        }

        // FUTURE ACCOUNT BALANCE
        BigDecimal newBalance = mResults.get(position).getSaldoConto().subtract(totExpences);
        if(mResults.get(position) != null) {
            holder.setData(mResults.get(position).getNomeConto(), NumberFormat.getCurrencyInstance().format(newBalance));
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
}