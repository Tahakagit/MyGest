package com.example.franc.mygest.adapters;

/**
 * Created by franc on 24/12/2017.
 */
import android.app.Application;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RviewAdapterMovimenti extends RecyclerView.Adapter<RviewAdapterMovimenti.TransactionViewHolder> {

    private List<EntityMovimento> mResults;
    Application app;
    MovimentoViewModel transVM;

    RviewAdapterMovimenti(Application app){
        this.app = app;
        transVM = new MovimentoViewModel(app);
    }

    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, int position) {
        holder.hiddenMenu.setVisibility(View.GONE);
        EntityMovimento movimento = mResults.get(position);

/*
        if (movimento.getEndscadenza()!= null){
            holder.icRecurrence.setVisibility(View.VISIBLE);
        }
*/
        if(movimento != null) {

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.ITALY);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ITALY);

            BigDecimal raw = new BigDecimal(String.valueOf(movimento.getImporto()));
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);
        holder.setData(movimento.getId(), movimento.getBeneficiario(), importoFormatted,
                movimento.getTipo(),
                dayFormat.format(movimento.getSaldato()),
                monthFormat.format(movimento.getSaldato()).toUpperCase(),
                movimento.getNomeConto()
                );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.hiddenMenu.getVisibility()==View.GONE){
                        //workaround to make visibility working with Constraint Group
                        holder.hiddenMenu.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.hiddenMenu.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mResults != null)
            return mResults.size();
        else return 0;
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        if (mResults.get(position).getDirection().equals("in")){
            if (mResults.get(position).getChecked().equalsIgnoreCase("unchecked")){
                return 0;

            }else {
                return 2;
            }
        }else {
            if (mResults.get(position).getChecked().equalsIgnoreCase("unchecked")){
                return 1;

            }else {
                return 3;
            }
        }
    }

    @Override
    public long getItemId(int position){
        return  (long)mResults.get(position).getId();
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_income, parent, false);

        }else if (viewType ==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_outcome, parent, false);

        }else if (viewType == 2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_income_check, parent, false);

        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_outcome_check, parent, false);

        }
        return new TransactionViewHolder(view);
    }

    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView saldatoDay;
        TextView saldatoMonth;
        TextView account;

        TextView tipo;
        int transId;
        Group hiddenMenu;

        TransactionViewHolder(View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.tv_card_all_account);

            beneficiario = itemView.findViewById(R.id.tv_card_all_beneficiario);
            importo = itemView.findViewById(R.id.tv_card_all_importo);
            tipo = itemView.findViewById(R.id.tv_card_all_tipo);
            hiddenMenu = itemView.findViewById(R.id.hidden_transaction_group);
            saldatoDay = itemView.findViewById(R.id.tv_card_all_saldatoday);
            saldatoMonth = itemView.findViewById(R.id.tv_card_all_saldatomonth);

        }

        public int getTransId(){
            return transId;
        }
        void setData(int id, String textbeneficiario, String textimporto, String type, String day, String month, String textAccount){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            tipo.setText(type);
            transId = id;
            saldatoDay.setText(day);
            saldatoMonth.setText(month);
            account.setText(textAccount);

        }
    }

}