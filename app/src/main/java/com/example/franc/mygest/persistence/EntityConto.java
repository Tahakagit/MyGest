package com.example.franc.mygest.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.OnConflictStrategy;
import android.support.annotation.NonNull;

import java.math.BigDecimal;


/**
 * Created by franc on 23/04/2018.
 */
@Entity(tableName = "conto_table", primaryKeys = "nomeConto")
public class EntityConto {



    private int id;
    @NonNull
    public String nomeConto;
    private String saldoConto;
    private int coloreConto;


    public EntityConto(String nomeConto, String saldoConto, int coloreConto){
        this.nomeConto = nomeConto;
        this.saldoConto = saldoConto;
        this.coloreConto = coloreConto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNomeConto() {
        return nomeConto;
    }


    public void setNomeConto(@NonNull String nomeConto) {
        this.nomeConto = nomeConto;
    }

    public String getSaldoConto() {
        return saldoConto;
    }

    public void setSaldoConto(String saldoConto) {
        this.saldoConto = saldoConto;
    }

    public int getColoreConto() {
        return coloreConto;
    }

    public void setColoreConto(int coloreConto) {
        this.coloreConto = coloreConto;
    }

}
