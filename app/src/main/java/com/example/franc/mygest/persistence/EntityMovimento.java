package com.example.franc.mygest.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by franc on 23/04/2018.
 */

@Entity (tableName = "movimento_table")
@TypeConverters(DateConverter.class)
public class EntityMovimento{

    private String beneficiario;
    private String importo;
    private String direction;
    private Date scadenza;
    private Date endscadenza;
    private int idConto;
    private String nomeConto;
    private String tipo;
    @android.arch.persistence.room.PrimaryKey(autoGenerate = true)
    private int id;
    private boolean checked = false;

    public EntityMovimento(final String beneficiario, final String importo, final Date scadenza, final int idConto, final String nomeConto, @Nullable final Date endscadenza, final String tipo){
        this.beneficiario = beneficiario;
        setImporto(importo);
        this.scadenza = scadenza;
        this.idConto = idConto;
        this.nomeConto = nomeConto;
        this.tipo = tipo;

        this.endscadenza = endscadenza;
    }
    //todo attrs checked, direzione


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNomeConto() {
        return nomeConto;
    }

    public void setNomeConto(String nomeConto) {
        this.nomeConto = nomeConto;
    }

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

    public int getIdConto() {
        return idConto;
    }

    public void setIdConto(int contoId) {
        this.idConto = contoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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