package com.example.franc.mygest.adapters;

/**
 * Created by franc on 24/12/2017.
 */
import android.app.Application;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.hiddenLayout.setVisibility(View.GONE);
/*
        holder.cv.setBackgroundColor(ContextCompat.getColor(app.getApplicationContext(), R.color.grey_bg_soft));
*/

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
                    if(holder.hiddenLayout.getVisibility()==View.GONE){
                        holder.hiddenLayout.setVisibility(View.VISIBLE);
                        holder.cv.setBackgroundColor(ContextCompat.getColor(app.getApplicationContext(), R.color.white));

                        holder.hiddenLayout2.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.hiddenLayout.setVisibility(View.GONE);
                        holder.hiddenLayout2.setVisibility(View.GONE);
                        holder.cv.setBackgroundColor(ContextCompat.getColor(app.getApplicationContext(), R.color.grey_bg_soft));


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movimenti, parent, false);
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
        LinearLayout hiddenLayout;
        LinearLayout hiddenLayout2;
        ImageView icRecurrence;
        LinearLayout cv;

        TransactionViewHolder(View itemView) {
            super(itemView);
            beneficiario = itemView.findViewById(R.id.id_card_beneficiario);
            importo = itemView.findViewById(R.id.id_card_importo);
            icRecurrence = itemView.findViewById(R.id.ic_recurrence);
            hiddenLayout = itemView.findViewById(R.id.hiddenlayout);
            hiddenLayout2 = itemView.findViewById(R.id.hiddenlayout_card_movimento_top);
            cv = itemView.findViewById(R.id.cv);
            tipo = itemView.findViewById(R.id.id_card_tipo);


        }

        void setData(int id, String textbeneficiario, String textimporto, String type){
            beneficiario.setText(textbeneficiario);
            importo.setText(textimporto);
            tipo.setText(type);
            transId = id;
        }
    }

}