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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.mygest.R;
import com.example.franc.mygest.persistence.EntityMovimento;
import com.example.franc.mygest.persistence.MovimentoViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

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

        if (movimento.getEndscadenza()!= null){
            holder.icRecurrence.setVisibility(View.VISIBLE);
        }
        if(movimento != null) {
            BigDecimal raw = new BigDecimal(String.valueOf(movimento.getImporto()));
            String importoFormatted = NumberFormat.getCurrencyInstance().format(raw);
            holder.setData(movimento.getId(), movimento.getBeneficiario(), importoFormatted,
                        movimento.getTipo());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.hiddenMenu.getVisibility()==View.GONE){
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

    @Override
    public long getItemId(int position){
        return  (long)mResults.get(position).getId();
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dashboard_transactions, parent, false);
        return new TransactionViewHolder(view);
    }

    public void setResults(List<EntityMovimento> results){
        mResults = results;
        notifyDataSetChanged();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView beneficiario;
        TextView importo;
        TextView tipo;
        int transId;
        ImageView icRecurrence;
        ImageView icBeneficiario;
        Group hiddenMenu;

        TransactionViewHolder(View itemView) {
            super(itemView);
            beneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            importo = itemView.findViewById(R.id.id_card_importo);
            icRecurrence = itemView.findViewById(R.id.ic_recurrence);
            tipo = itemView.findViewById(R.id.id_card_tipo);
            icBeneficiario = itemView.findViewById(R.id.ic_beneficiario);
            hiddenMenu = itemView.findViewById(R.id.hidden_transaction_group);

        }

        void setData(int id, String textbeneficiario, String textimporto, String type){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            tipo.setText(type);
            transId = id;
        }
    }

}