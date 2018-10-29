package com.example.franc.mygest.adapters;

/*
 * Created by franc on 28/10/2017.
 *
 * Creates account overview card, each one displaying his own transactions up to the chosen date
 */
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_dates, parent, false);

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

            dayScadenzaText = itemView.findViewById(R.id.tv_mainactivity_dates_day);
            monthScadenzaText = itemView.findViewById(R.id.tv_mainactivity_dates_month);
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
        RecyclerView rviewMovimenti = dateViewholder.itemView.findViewById(R.id.rv_mainactivity_transactions);
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

            /**
             * Called by ItemTouchHelper on RecyclerView's onDraw callback.
             * <p>
             * If you would like to customize how your View's respond to user interactions, this is
             * a good place to override.
             * <p>
             * Default implementation translates the child by the given <code>dX</code>,
             * <code>dY</code>.
             * ItemTouchHelper also takes care of drawing the child after other children if it is being
             * dragged. This is done using child re-ordering mechanism. On platforms prior to L, this
             * is
             * achieved via {@link ViewGroup#getChildDrawingOrder(int, int)} and on L
             * and after, it changes View's elevation value to be greater than all other children.)
             *
             * @param c                 The canvas which RecyclerView is drawing its children
             * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
             * @param viewHolder        The ViewHolder which is being interacted by the User or it was
             *                          interacted and simply animating to its original position
             * @param dX                The amount of horizontal displacement caused by user's action
             * @param dY                The amount of vertical displacement caused by user's action
             * @param actionState       The type of interaction on the View. Is either {@link
             * @param isCurrentlyActive True if this view is currently being controlled by the user or
             *                          false it is simply animating back to its original state.
             */
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View view = viewHolder.itemView;
                Paint textPaintDelete = new Paint();
                Paint textPaintArchive = new Paint();

                textPaintArchive.setARGB((2*(int)dX)/5,70,175,74);
                textPaintArchive.setTextAlign(Paint.Align.LEFT);
                textPaintDelete.setARGB(((2*(int)dX)/5)*-1,254,0,0);
                textPaintDelete.setTextAlign(Paint.Align.LEFT);
                Typeface typeface = ResourcesCompat.getFont(context, R.font.hindvadodara_semibold);

                textPaintArchive.setTypeface(typeface);
                textPaintArchive.setTextSize(50);
                textPaintDelete.setTypeface(typeface);
                textPaintDelete.setTextSize(50);

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    if (dX>0){
                        c.drawText("ARCHIVIA", (view.getWidth()/16)*1, (view.getHeight()/4)*3, textPaintArchive);

                    }else {
                        c.drawText("CANCELLA", (view.getWidth()/16)*10, (view.getHeight()/4)*3, textPaintDelete);

                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int transactionPosition = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    try {
                        int id = (int)adapterMovimenti.getItemId(transactionPosition);
                        movsVM.deleteTransactionById(id);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.w("swipe", "Skip timestamp cause ther's no result");
                    }
                }else {
                    try {
                        int id = (int)adapterMovimenti.getItemId(transactionPosition);

                        Log.d("swipe", "rimuovo transazione alla posizione " + transactionPosition + " del conto " + mResults.get(dateViewholder.getAdapterPosition()).getNomeConto());

                        movsVM.checkTransaction(id);


                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.w("swipe", "Skip timestamp cause ther's no result");
                    }

                }
/*
                adapterMovimenti.notifyItemRemoved(transactionPosition);
*/

            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rviewMovimenti);


    }

}