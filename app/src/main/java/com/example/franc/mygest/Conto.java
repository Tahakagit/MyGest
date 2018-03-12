package com.example.franc.mygest;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 20/12/2017.
 */

public class Conto extends RealmObject {



    @PrimaryKey
    private String nomeConto;
    private String saldoConto;
    private String coloreConto;
    private int transactionNumber = 0;

    @Override
    public String toString(){
        return this.nomeConto;
    }
    public String getNomeConto() {
        return nomeConto;
    }

    public void setNomeConto(String nomeConto) {
        this.nomeConto = nomeConto;
    }

    public String getSaldoConto() {
        return saldoConto;
    }

    public void setSaldoConto(String saldoConto) {
        this.saldoConto = saldoConto;
    }

    public String getColoreConto() {
        return coloreConto;
    }

    public void setColoreConto(String coloreConto) {
        this.coloreConto = coloreConto;
    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
