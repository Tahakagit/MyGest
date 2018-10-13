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
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.activities.MainActivity;
import com.example.franc.mygest.persistence.EntityConto;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RviewAdapterDailyTransaction extends RecyclerView.Adapter<RviewAdapterDailyTransaction.AccountDashboardViewHolder> {

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
    public AccountDashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mainactivity_account, parent, false);

        return new AccountDashboardViewHolder(view);
    }


    /**
     *
     * @param accountViewHolder the viewholder
     *
     * */
    private void startDatesRecyclerView(AccountDashboardViewHolder accountViewHolder){
        RecyclerView rviewDates;
        RviewAdapterGroupDates adapterDates;

        rviewDates = accountViewHolder.itemView.findViewById(R.id.rv_mainactivity_dates);
        adapterDates = new RviewAdapterGroupDates(context, app);
        adapterDates.setHasStableIds(true);
        rviewDates.setLayoutManager(new LinearLayoutManager(context));
        rviewDates.setAdapter(adapterDates);
        int accountId = mResults.get(accountViewHolder.getAdapterPosition()).getId();
        // OBSERVE DB FOR RESULTS
        movsVM.getAllDatesByAccount(MainActivity.getDateToSend().getTime(),
                accountId)
                .observe((LifecycleOwner)context, new Observer<List<EntityMovimento>>() {
                    @Override
                    public void onChanged(@Nullable List<EntityMovimento> dates) {
                        adapterDates.setResults(dates);
                    }
                });
    }

    private int getTotalTransactions(int account){

        return  movsVM.getTotalTransaction(account, MainActivity.getDateToSend().getTime());

    }


    /**
     * Subtract all transactions to current balance up to the selected date
     * @param account EntityAccount
     * @return new balance
     */
    private BigDecimal calculateNewBalance(EntityConto account){
        BigDecimal oldBalance = new BigDecimal(String.valueOf(account.getSaldoConto()));
        BigDecimal totExpences = new BigDecimal("0");
        totExpences = totExpences.add(new BigDecimal(String.valueOf(movsVM.getAllTransactionAmount(account.getId(),
                MainActivity.getDateToSend().getTime()))));
        BigDecimal newBalance = oldBalance.subtract(totExpences);
        return newBalance;
    }

    @Override
    public void onBindViewHolder(final AccountDashboardViewHolder accountViewHolder, int position) {
        if(mResults.get(position) != null) {
            position = accountViewHolder.getAdapterPosition();
            // CALCULATE BALANCES
            BigDecimal currentBalanceBigD = new BigDecimal(String.valueOf(mResults.get(position).getSaldoConto()));
            BigDecimal futureBalanceBigD = calculateNewBalance(mResults.get(position));

            String currentBalance = NumberFormat.getCurrencyInstance(Locale.ITALY).format(currentBalanceBigD);
            String futureBalance = NumberFormat.getCurrencyInstance(Locale.ITALY).format(futureBalanceBigD);
            String accountName = mResults.get(position).getNomeConto();
            String totalTransactions = String.valueOf(getTotalTransactions(mResults.get(position).getId()) + " movimenti in attesa");

            int accountColor = mResults.get(position).getColoreConto();

            // FILL CARD WITH DATA
            accountViewHolder.setData(accountName,
                    currentBalance,
                    futureBalance,
                    totalTransactions,
                    accountColor
                    );


            accountViewHolder.cv.setCardBackgroundColor(adjustBrightness(accountColor, 0.40f));
            accountViewHolder.bg1.setBackgroundColor(Color.WHITE);
            accountViewHolder.bg2.setBackgroundColor(Color.WHITE);

            // STARTS DATES GROUPING RV
            startDatesRecyclerView(accountViewHolder);
            // STARTING EXPAND COLLAPSE ACCOUNT CARDS
            accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(accountViewHolder.hiddenRv.getVisibility()==View.GONE){
                        accountViewHolder.hiddenRv.setVisibility(View.VISIBLE);
/*
                        accountViewHolder.cv.setCardElevation(15f);
*/
                    }
                    else {
                        accountViewHolder.hiddenRv.setVisibility(View.GONE);
/*
                        accountViewHolder.cv.setCardElevation(1f);
*/
                    }
                }
            });
            // CREATING EDIT ACCOUNT MENU
            accountViewHolder.moreIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIController uiController = new UIController(context);
                    uiController.displayAccountManageDialog(mResults.get(accountViewHolder.getAdapterPosition()));
                }
            });

        }



    }
    public int adjustBrightness( int color, float factor) {
        float[] hsv = new float[3];
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        Color.RGBToHSV(r, g, b, hsv);

        hsv[1] *= factor;
        return Color.HSVToColor(hsv);
    }


    /**
     * Updates mAdapterConti items list
     * @param results ArrayList to show
     */
    public void setResults(List<EntityConto> results){
        mResults = results;
        notifyDataSetChanged();
    }

    static class AccountDashboardViewHolder extends RecyclerView.ViewHolder{
        TextView accountFutureBalance;
        TextView accountCurrentBalance;
        TextView accountName;
        TextView totalTransactions;
        ImageView moreIc;
        RecyclerView hiddenRv;
        ConstraintLayout bg1;
        ConstraintLayout bg2;
        CardView cv;
        TextView currentLabel;
        TextView futureLabel;

        AccountDashboardViewHolder(View itemView) {
            super(itemView);
            bg1 = itemView.findViewById(R.id.constraintlayout_mainactivity_bgtop);
            bg2 = itemView.findViewById(R.id.constraintlayout_mainactivity_bgbottom);
            cv = itemView.findViewById(R.id.cv_mainactivity_account);
            accountName = itemView.findViewById(R.id.id_account_name);
            accountFutureBalance = itemView.findViewById(R.id.id_account_future_balance);
            accountCurrentBalance = itemView.findViewById(R.id.id_account_current_balance);
            moreIc = itemView.findViewById(R.id.ic_more);
            hiddenRv = itemView.findViewById(R.id.rv_mainactivity_dates);
            totalTransactions = itemView.findViewById(R.id.id_account_total);
            currentLabel = itemView.findViewById(R.id.tv_mainactivity_current);
            futureLabel = itemView.findViewById(R.id.tv_mainactivity_estimated);


        }

        void setData(String textscadenza, String currentBalance, String futureBalance, String total, int color){
            accountName.setText(textscadenza);
            accountFutureBalance.setText(futureBalance);
            accountCurrentBalance.setText(currentBalance);
            totalTransactions.setText(total);
            accountName.setTextColor(color);
            accountCurrentBalance.setTextColor(color);
            accountFutureBalance.setTextColor(color);
            moreIc.setColorFilter(color);
            currentLabel.setTextColor(color);
            futureLabel.setTextColor(color);





        }

    }

}