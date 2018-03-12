package com.example.franc.mygest;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Pasticceria Duomo on 28/07/2017.
 */

public class Movimento extends RealmObject{

    @PrimaryKey
    private String beneficiario;
    private String importo;
    private Date scadenza;
    private String conto;
    private String tipo;
    private long timestamp;
//todo attrs checked, direzione
    public String getConto() {
        return conto;
    }

    public void setConto(String conto) {
        this.conto = conto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}