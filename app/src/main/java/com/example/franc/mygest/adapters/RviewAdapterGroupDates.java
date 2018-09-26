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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
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
    Drawable background;

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
        TextView yearScadenzaText;

        DateDashboardViewHolder(View itemView) {
            super(itemView);

            dayScadenzaText = itemView.findViewById(R.id.id_card_scadenza_day);
            monthScadenzaText = itemView.findViewById(R.id.id_card_scadenza_month);
/*
            yearScadenzaText = itemView.findViewById(R.id.id_card_scadenza_year);
*/

        }

        void setData(String dayScadenza, String monthScadenza){
            dayScadenzaText.setText(dayScadenza);
            monthScadenzaText.setText(monthScadenza);
/*
            yearScadenzaText.setText(yearScadenza);
*/

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
/*
            SimpleDateFormat yearFormat = new SimpleDateFormat("YY", Locale.ITALY);
*/

            dateViewholder.setData(dayFormat.format(current.getScadenza()), monthFormat.format(current.getScadenza()).toUpperCase());
        }
    }


    /**
     *
     * @param dateViewholder viewholder
     *
     * */
    private void startTransactionRecyclerView(DateDashboardViewHolder dateViewholder){
        RecyclerView rviewMovimenti = dateViewholder.itemView.findViewById(R.id.rv_movimenti);
        RviewAdapterMovimenti adapterMovimenti;
        rviewMovimenti.setLayoutManager(new LinearLayoutManager(context));

/*
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rviewMovimenti.getContext(),
                DividerItemDecoration.VERTICAL);
        rviewMovimenti.addItemDecoration(dividerItemDecoration);
*/
        adapterMovimenti = new RviewAdapterMovimenti(app);
        adapterMovimenti.setHasStableIds(true);
        // QUERY DB FOR RESULTS
        movsVM.getDailyTransactionsByAccount(mResults.get(dateViewholder.getAdapterPosition()).getScadenza(),
                mResults.get(dateViewholder.getAdapterPosition()).getIdConto())
                .observe((LifecycleOwner)context, new Observer<List<EntityMovimento>>() {
                    @Override
                    public void onChanged(@Nullable List<EntityMovimento> entityMovimentos) {
                        adapterMovimenti.setResults(entityMovimentos);
                    }
                });

        rviewMovimenti.setAdapter(adapterMovimenti);
        // SET UP SWIPE
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {

/*
            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
*/
/*
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
*//*

                background = new ColorDrawable(context.getResources().getColor(R.color.grey_bg_soft));

                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                c.drawPaint(paint);

                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                c.drawText("Some Text", 10, 25, paint);

*/
/*
                background.setBounds(viewHolder.itemView.getRight() + (int) dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                background.draw(c);
*//*


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            }
*/

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    try {
                        int transactionPosition = viewHolder.getAdapterPosition();
                        int id = (int)adapterMovimenti.getItemId(transactionPosition);
                        movsVM.deleteTransactionById(id);
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


    }

}