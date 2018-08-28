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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RviewAdapterGroupDates extends RecyclerView.Adapter<RviewAdapterGroupDates.DateDashboardViewHolder> {

    private List<EntityMovimento> mResults;
    private Context context;
    MovimentoViewModel movsVM;
    private Application app;

    public RviewAdapterGroupDates(Context context, Application app) {
        setResults(mResults);
        this.context = context;
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
    public DateDashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dashboard_dates, parent, false);

        return new DateDashboardViewHolder(view);
    }


    /**
     *
     * @param dateViewholder viewholder
     *
     * */
    private void startTransactionRecyclerView(DateDashboardViewHolder dateViewholder){
        RecyclerView rviewMovimenti = dateViewholder.itemView.findViewById(R.id.rv_movimenti);
        RviewAdapterMovimenti adapterMovimenti;

        adapterMovimenti = new RviewAdapterMovimenti(app);
        adapterMovimenti.setHasStableIds(true);

        String nomeConto = mResults.get(dateViewholder.getAdapterPosition()).getNomeConto();
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(context));
        rviewMovimenti.setAdapter(adapterMovimenti);
        // SET UP SWIPE
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.RIGHT) {
                    try {
                        int transactionPosition = viewHolder.getAdapterPosition();
                        int id = (int)adapterMovimenti.getItemId(transactionPosition);
                        movsVM.deleteTransactionById(id);
/*
                        adapterMovimenti.notifyItemRemoved(transactionPosition);
*/


                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.w("swipe", "Skip timestamp cause ther's no result");
                    }
                }else {
                    try {
                        int transactionPosition = viewHolder.getAdapterPosition();
                        int id = (int)adapterMovimenti.getItemId(transactionPosition);

                        Log.d("swipe", "rimuovo transazione alla posizione " + transactionPosition + " del conto " + mResults.get(dateViewholder.getAdapterPosition()).getNomeConto());

                        movsVM.checkTransaction(id);
                        adapterMovimenti.notifyItemRemoved(transactionPosition);


                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.w("swipe", "Skip timestamp cause ther's no result");
                    }

                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rviewMovimenti);


        // QUERY DB FOR RESULTS
        movsVM.getDailyTransactionsByAccount(mResults.get(dateViewholder.getAdapterPosition()).getScadenza(),
                mResults.get(dateViewholder.getAdapterPosition()).getIdConto())
                .observe((LifecycleOwner)context, new Observer<List<EntityMovimento>>() {
                    @Override
                    public void onChanged(@Nullable List<EntityMovimento> entityMovimentos) {
                        adapterMovimenti.setResults(entityMovimentos);
                        Log.d("on change movimenti", " trovati  " + entityMovimentos.size() + "  movimenti per il conto  " + nomeConto);
                    }
                });
    }

    /**
     * Updates mAdapterConti items list
     * @param results ArrayList to show
     */
    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }

    static class DateDashboardViewHolder extends RecyclerView.ViewHolder{
        TextView dayScadenzaText;
        TextView monthScadenzaText;

        DateDashboardViewHolder(View itemView) {
            super(itemView);

            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);

        }

        void setData(String dayScadenza, String monthScadenza){
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
        }
    }

    @Override
    public void onBindViewHolder(final DateDashboardViewHolder dateViewholder, int position) {
        position = dateViewholder.getAdapterPosition();
        startTransactionRecyclerView(dateViewholder);
        EntityMovimento current = mResults.get(position);

        if(current != null) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.ITALY);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ITALY);
            dateViewholder.setData(dayFormat.format(current.getScadenza()), monthFormat.format(current.getScadenza()).toUpperCase());
        }
    }
}