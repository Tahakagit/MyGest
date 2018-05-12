package com.example.franc.mygest.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * Created by franc on 23/04/2018.
 */
@Dao
public interface MovimentoDao {

    @Insert
    void insert(EntityMovimento movimento);

    @Query("DELETE FROM movimento_table")
    void deleteAll();

    @Query("DELETE FROM movimento_table WHERE id LIKE :id")
    void deleteTransactionById(int id);

    @Query("SELECT * from movimento_table")
    LiveData<List<EntityMovimento>> getAllTransactions();

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT DISTINCT conto FROM movimento_table WHERE scadenza <= (:dayet)")
    LiveData<List<String>> getTransactionUpTo(Date dayet);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT scadenza, conto, id FROM movimento_table WHERE scadenza  <= :upTo AND conto == (:account) GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDates(Date upTo, String account);


    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza <= (:dayet) AND conto LIKE (:account)")
    LiveData<List<EntityMovimento>> getTransactionUpToByAccount(Date dayet, String account);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == (:dayet) AND conto LIKE (:account)")
    LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(Date dayet, String account);

}
