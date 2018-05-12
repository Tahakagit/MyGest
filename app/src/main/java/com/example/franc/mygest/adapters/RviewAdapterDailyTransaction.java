package com.example.franc.mygest.adapters;

/*
 * Created by franc on 28/10/2017.
 *
 * Creates account overview card, each one displaying his own transactions up to the chosen date
 */
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.activities.MainActivity;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.DataObjectHolder> {

    private List<EntityConto> mResults;
    private Context context;
    MovimentoViewModel movsVM;
    private RviewAdapterDailyTransaction mAdapter;
    private Application app;

    public RviewAdapterDailyTransaction(Context context, Application app) {
        setResults(mResults);
        this.context = context;
        mAdapter = this;
        this.app = app;
        movsVM = new MovimentoViewModel(app);


    }


    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }
    @Override
    public long getItemId(int position){ return  0;}
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dailytransaction, parent, false);

        return new DataObjectHolder(view);
    }


    /**
     *
     * @param view needed to findViewById
     *
     * */
    private void startDatesRecyclerView(final BigDecimal currentBalance, View view, DataObjectHolder holder){
        RecyclerView rviewDates = view.findViewById(R.id.rv_dates);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rviewDates.getContext(),
                    RecyclerView.VERTICAL);
        RviewAdapterGroupDates adapterDates;

        adapterDates = new RviewAdapterGroupDates(context, app);
        adapterDates.setHasStableIds(true);

        String nomeConto = mResults.get(holder.getAdapterPosition()).getNomeConto();
        // SET UP RECYCLERVIEW
        rviewDates.addItemDecoration(mDividerItemDecoration);
        rviewDates.setLayoutManager(new LinearLayoutManager(context));
        rviewDates.setAdapter(adapterDates);
        // QUERY DB FOR RESULTS
/*
        movsVM.getAllMovimentoDistByAccount(MainActivity.getDateToSend().getTime(),
                mResults.get(holder.getAdapterPosition()).getNomeConto())
                .observe((LifecycleOwner)context, new Observer<List<EntityMovimento>>() {
                    @Override
                    public void onChanged(@Nullable List<EntityMovimento> entityMovimentos) {
                        Log.d("on change movimenti", " trovati  " + entityMovimentos.size() + "  movimenti per il conto  " + nomeConto);
                        holder.setNewBalance(NumberFormat.getCurrencyInstance().format(new BigDecimal(calculateNewBalance(currentBalance, entityMovimentos))));
                    }
                });
*/
/*
        SimpleDateFormat dayFormat = new SimpleDateFormat("DD/MM", Locale.ITALIAN);

        Date d = null;
        String strDate = dayFormat.format(MainActivity.getDateToSend().getTime());
        d= dayFormat.parse(strDate);
*/
        movsVM.getAllDates(MainActivity.getDateToSend().getTime(),
                mResults.get(holder.getAdapterPosition()).getNomeConto())
                .observe((LifecycleOwner)context, new Observer<List<EntityMovimento>>() {
                    @Override
                    public void onChanged(@Nullable List<EntityMovimento> dates) {
                        adapterDates.setResults(dates);
                        Log.d("on change movimenti", " trovate  " + dates.size() + "  date per il conto  " + nomeConto);
                    }
                });


    }


    /**
     * Subtract all transactions to current balance up to the selected date
     * @param oldBalance old balance
     * @return new balance
     */
    private String calculateNewBalance(BigDecimal oldBalance, List<EntityMovimento> movs){

        // SUM ALL UNCHECKED TRANSACTIONS
        BigDecimal totExpences = new BigDecimal("0");
        for (EntityMovimento res:movs) {
            totExpences = totExpences.add(new BigDecimal(String.valueOf(res.getImporto())));
        }

        // FUTURE ACCOUNT BALANCE
        final BigDecimal newBalance = oldBalance.subtract(totExpences);

        return newBalance.toString();
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
/*
        String nomeConto = mResults.get(position).getNomeConto();
*/
        position = holder.getAdapterPosition();
        BigDecimal currentBalance = new BigDecimal(String.valueOf(mResults.get(position).getSaldoConto()));



        startDatesRecyclerView(currentBalance, holder.itemView, holder);





        /*
        final BigDecimal newBalance = calculateNewBalance(currentBalance);
*/

        //Fills cards with account data
        if(mResults.get(position) != null) {
            holder.setData(mResults.get(position).getNomeConto(),
                        NumberFormat.getCurrencyInstance().format(new BigDecimal(String.valueOf(mResults.get(position).getSaldoConto()))));
            holder.cv.setCardBackgroundColor(mResults.get(position).getColoreConto());
            holder.nestedBg.setBackgroundColor(mResults.get(position).getColoreConto());



            //Expanding Collapsing Account cards
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(holder.hiddenlayout.getVisibility()==View.GONE){
                        holder.hiddenlayout.setVisibility(View.VISIBLE);
                        holder.cv.setCardElevation(15f);
                    }
                    else {
                        holder.hiddenlayout.setVisibility(View.GONE);
                        holder.cv.setCardElevation(1f);
                    }
                }
            });



            //Updating account balance
            final BigDecimal currentBalance2 = currentBalance;
            holder.moreIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIController uiController = new UIController(context);
                    uiController.displayAccountDialog(mResults.get(holder.getAdapterPosition()).getNomeConto(),
                            NumberFormat.getCurrencyInstance().format(new BigDecimal(String.valueOf(mResults.get(holder.getAdapterPosition()).getSaldoConto()))),
                            mResults.get(holder.getAdapterPosition()).getColoreConto());
                }
            });

        }



    }

    /**
     * Shows dialog to update account balance
     * @param position position to retrieve account
     */

    /**
     * Updates mAdapterConti items list
     * @param results ArrayList to show
     */
    public void setResults(List<EntityConto> results){
        mResults = results;
        notifyDataSetChanged();
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView accountFutureBalance;
        TextView accountCurrentBalance;
        TextView accountName;
        ImageView moreIc;
        LinearLayout hiddenlayout;
        LinearLayout nestedBg;

        CardView cv;

        DataObjectHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_account_dashboard);
            accountName = itemView.findViewById(R.id.id_account_name);
            accountFutureBalance = itemView.findViewById(R.id.id_account_future_balance);
            accountCurrentBalance = itemView.findViewById(R.id.id_account_current_balance);
            moreIc = itemView.findViewById(R.id.imageView);
            hiddenlayout = itemView.findViewById(R.id.hiddenlayout);
            nestedBg = itemView.findViewById(R.id.id_bg_nested);

        }

        void setNewBalance(String newBalance){
            accountFutureBalance.setText(newBalance);
        }
        void setData(String textscadenza, String currentBalance){
            accountName.setText(textscadenza);
/*
            accountFutureBalance.setText(futureBalance);
*/
            accountCurrentBalance.setText(currentBalance);



        }

    }

}