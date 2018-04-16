package com.example.franc.mygest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 20/12/2017.
 */


public class Conto extends RealmObject {



    @PrimaryKey
    private String nomeConto;
    private String saldoConto;
    private int coloreConto;

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

    public BigDecimal getSaldoConto() {
        return new BigDecimal(saldoConto);
    }

    public void setSaldoConto(BigDecimal saldoConto) {
        this.saldoConto = saldoConto.toString();
    }

    public int getColoreConto() {
        return coloreConto;
    }

    public void setColoreConto(int coloreConto) {
        this.coloreConto = coloreConto;
    }

}
