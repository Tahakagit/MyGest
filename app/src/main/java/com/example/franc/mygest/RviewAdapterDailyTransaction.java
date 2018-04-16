package com.example.franc.mygest;

/**
 * Created by franc on 28/10/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
//questo diventa il recycler view dei giorni con transazioni all'interno del quale ogni viewholer
//avra la sua recycler view popolata dai singoli movimenti

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.DataObjectHolder> {

    private ArrayList<ContoObj> mResults;
    private Context context;
    final Realm mRealm = Realm.getDefaultInstance();
    BigDecimal currentBalance;

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
        nomeConto = mResults.get(position).getNomeConto();

        movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend.getTime(), nomeConto);


        // START NESTED TRANSACTIONS RECYCLERVIEW
        RecyclerView rviewMovimenti = holder.itemView.findViewById(R.id.rv_transaction);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rviewMovimenti.getContext(), RecyclerView.VERTICAL);
        rviewMovimenti.addItemDecoration(mDividerItemDecoration);

        final RviewAdapterMovimenti adapterMovimenti = new RviewAdapterMovimenti(movs);
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(context));
        rviewMovimenti.setAdapter(adapterMovimenti);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
/*
            this.movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend, mResults.get(position).getNomeConto());
*/

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long timestamp = 0;
                movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend.getTime(), mResults.get(position).getNomeConto());
                try {
                    timestamp = movs.get(viewHolder.getAdapterPosition()).getTimestamp();
                    adapterMovimenti.deleteItemAt(timestamp);

                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.w("OnSwipe", "Skip timestamp cause ther's no result");
                }
                if (movs.size() == 0) {
                    Log.d("delete on swipe", "posizione adapter dailytransactions" + String.valueOf(position));
                    mResults.remove(position);
                    notifyDataSetChanged();
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
        final BigDecimal newBalance = mResults.get(position).getSaldoConto().subtract(totExpences);
        currentBalance = mResults.get(position).getSaldoConto();
        BigDecimal currentBalance = mResults.get(position).getSaldoConto();
        if(mResults.get(position) != null) {
            holder.setData(mResults.get(position).getNomeConto(), NumberFormat.getCurrencyInstance().format(newBalance), NumberFormat.getCurrencyInstance().format(currentBalance));
            holder.cv.setCardBackgroundColor(mResults.get(position).getColoreConto());

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(holder.hiddenlayout.getVisibility()==View.GONE){
                    holder.hiddenlayout.setVisibility(View.VISIBLE);
/*
                    holder.cv.setCardElevation(5.0f);
*/
                    holder.cv.setCardElevation(15f);


                }
                else {
                    holder.hiddenlayout.setVisibility(View.GONE);
                    holder.cv.setCardElevation(1f);


                }
            }
        });


        final BigDecimal currentBalance2 = currentBalance;
        holder.moreIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccountCurrentBalance(NumberFormat.getCurrencyInstance().format(currentBalance2), NumberFormat.getCurrencyInstance().format(newBalance), nomeConto, position);
            }
        });


    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRealm.close();

    }

    void updateAccountCurrentBalance(String currentBalance, final String newBalance, String accountName, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Aggiorna saldo!");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_account, null);
        builder.setView(dialogView);

        final EditText balanceText = dialogView.findViewById(R.id.id_input_new_balance);

        balanceText.setText(currentBalance);
        balanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balanceText.setText("");
                balanceText.addTextChangedListener(new MoneyTextWatcher(balanceText));

            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cleanString = balanceText.getText().toString().replaceAll("[ €,.\\s]", "");

                RealmHelper helper = new RealmHelper();
/*
                helper.updateConto(nomeConto, new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
*/
                helper.updateBalance(mResults.get(position), new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR));
                updateItem(position);
            }
        });
        builder.create().show();
    }
    void setResultsRealm(ArrayList<ContoObj> results){
        movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.dateToSend.getTime(), nomeConto);

        mResults = results;
        notifyDataSetChanged();
/*
        notifyItemInserted(results.size()-1);
*/
    }
    void updateItem(int position){
        this.notifyItemChanged(position);
    }

    void updateResults(){
/*
        movs = helper.getTransactionsUntilGroupedBySingleAccount(MainActivity.weekRange.getTime(), mResults.get(position).getNomeConto());
*/


        this.notifyDataSetChanged();
    }
    Context getContext(){

        return context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView accountFutureBalance;
        TextView accountCurrentBalance;
        TextView accountName;
        ImageView moreIc;
        LinearLayout hiddenlayout;
        CardView cv;

        public DataObjectHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_account_dashboard);
            accountName = itemView.findViewById(R.id.id_account_name);
            accountFutureBalance = itemView.findViewById(R.id.id_account_future_balance);
            accountCurrentBalance = itemView.findViewById(R.id.id_account_current_balance);
            moreIc = itemView.findViewById(R.id.imageView);
            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);

        }

        public void setData(String textscadenza, String futureBalance, String currentBalance){
            accountName.setText(textscadenza);
            accountFutureBalance.setText(futureBalance);
            accountCurrentBalance.setText(currentBalance);



        }

    }

}