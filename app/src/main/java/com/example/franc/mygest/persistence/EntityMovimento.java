package com.example.franc.mygest.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 23/04/2018.
 */

@Entity (tableName = "movimento_table")
@TypeConverters(DateConverter.class)
public class EntityMovimento{

    private String beneficiario;
    private String importo;
    private Date scadenza;
    private Date endscadenza;
    private String conto;
    private String tipo;
    @android.arch.persistence.room.PrimaryKey(autoGenerate = true)
    private int id;

    public EntityMovimento(final String beneficiario, final String importo, final Date scadenza, final String conto, @Nullable final Date endscadenza){
        this.beneficiario = beneficiario;
        setImporto(importo);
        this.scadenza = scadenza;
        this.conto = conto;
        this.endscadenza = endscadenza;
    }
    //todo attrs checked, direzione


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }
    public String getImporto(){
        return importo;
    }

    public Date getEndscadenza() {
        return endscadenza;
    }

    public void setEndscadenza(Date endscadenza) {
        this.endscadenza = endscadenza;
    }

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

/*
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
*/
}