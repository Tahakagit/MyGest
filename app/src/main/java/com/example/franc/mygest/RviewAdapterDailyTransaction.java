package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

    private ArrayList<ContoObj> mResults;
    private Context context;
    final Realm mRealm = Realm.getDefaultInstance();

    RviewAdapterDailyTransaction(Context context, ArrayList<ContoObj> mResults) {
        setResultsRealm(mResults);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dailytransaction, parent, false);
        return new DataObjectHolder(view);
    }

    String nomeConto = null;
    RealmResults<Movimento> movs;

    RealmHelper helper = new RealmHelper();

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.weekRange.getTime(), mResults.get(position).getNomeConto());
        nomeConto = mResults.get(position).getNomeConto();


        // START NESTED TRANSACTIONS RECYCLERVIEW
        RecyclerView rviewMovimenti = holder.itemView.findViewById(R.id.rv_transaction);

        final RviewAdapterMovimenti adapterMovimenti = new RviewAdapterMovimenti(movs);
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(context));
        rviewMovimenti.setAdapter(adapterMovimenti);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            RealmResults<Movimento> movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend, mResults.get(position).getNomeConto());

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long timestamp = 0;
                try {
                    timestamp = movs.get(viewHolder.getAdapterPosition()).getTimestamp();
                    adapterMovimenti.deleteItemAt(timestamp);

                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.w("OnSwipe", "Skip timestamp cause ther's no result");
                }
                if (movs.size() == 0) {
                    Log.d("delete on swipe", "posizione adapter dailytransactions" + String.valueOf(position));
                    mResults.remove(position);
                    notifyDataSetChanged();//is necessary?
                    notifyItemRemoved(position);
                }else {
                    adapterMovimenti.notifyItemRemoved(viewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                }
/*
                movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend, mResults.get(position).getNomeConto());
*/
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
/*
            holder.setData(mResults.get(position).getNomeConto(), NumberFormat.getCurrencyInstance().format(newBalance));
*/
            holder.setData(mResults.get(position).getNomeConto(), String.valueOf(position));

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

    void setResultsRealm(ArrayList<ContoObj> results){
        mResults = results;
        notifyDataSetChanged();
    }
    void updateResults(){
        notifyDataSetChanged();
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

}