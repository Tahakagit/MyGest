package com.example.franc.mygest.adapters;

import android.app.Application;
import android.content.Context;
import android.support.constraint.Group;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.UIController;
import com.example.franc.mygest.persistence.ContoViewModel;
import com.example.franc.mygest.persistence.EntityConto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by franc on 20/12/2017.
 */

public class RviewAdapterConto extends RecyclerView.Adapter<RviewAdapterConto.AccountViewHolder> {
    private List<EntityConto> mResults;
    private Context context;
    private ContoViewModel contoVM;


    public RviewAdapterConto(Context context) {
        this.context = context;
        contoVM = new ContoViewModel((Application) context.getApplicationContext());
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        TextView saldo;
        ImageView updateAccount;
        ImageView deleteAccount;
        CardView cv;
        Group hiddenlayout;

        AccountViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tv_accountmanage_accountname);
            saldo = itemView.findViewById(R.id.tv_accountmanage_accountbalance);
            cv = itemView.findViewById(R.id.cv_accountmanage);
            hiddenlayout = itemView.findViewById(R.id.group_accountmanage_cardconti);
            updateAccount = itemView.findViewById(R.id.img_edit_account);
            deleteAccount = itemView.findViewById(R.id.img_delete_account);

        }

        void setData(String textbeneficiario, String textimporto){
            nome.setText(textbeneficiario);
            saldo.setText(textimporto);
        }
    }

    public void setResults(List<EntityConto> results){
        mResults = results;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }

    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_accountmanage_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AccountViewHolder holder, int position) {
        holder.hiddenlayout.setVisibility(View.GONE);
        EntityConto current = mResults.get(position);
        if(current != null) {
            BigDecimal rawBalanceFromDb = new BigDecimal(String.valueOf(current.getSaldoConto()));
            String formattedBalance = NumberFormat.getCurrencyInstance().format(rawBalanceFromDb);
            holder.setData(current.getNomeConto(), formattedBalance);
            holder.cv.setCardBackgroundColor(mResults.get(position).getColoreConto());
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
            holder.updateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIController uiController = new UIController(context);
                    uiController.displayAccountManageDialog(mResults.get(position));
                }
            });
            holder.deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //DELETE ACCOUNT
                    //what about transactions in it?
                    contoVM.delete(mResults.get(position));
                }
            });
        }
    }
}
