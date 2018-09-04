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

    @Query("UPDATE movimento_table SET checked = 'checked' WHERE id LIKE :id")
    void checkTransaction(int id);

    @Query("SELECT * from movimento_table WHERE checked == 'unchecked'")
    LiveData<List<EntityMovimento>> getAllTransactions();

    @Query("SELECT * from movimento_table WHERE checked == :checked")
    LiveData<List<EntityMovimento>> getAllTransactionsChecked(String checked);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT scadenza, nomeConto, idConto, id, checked FROM movimento_table WHERE scadenza  <= :upTo AND idConto == (:account) AND checked == 'unchecked' GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesByAccount(Date upTo, int account);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT scadenza, nomeConto, idConto, id, checked FROM movimento_table WHERE checked == :checked GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDates(String checked);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza <= (:dayet) AND checked == 'unchecked' AND idConto  LIKE (:account)")
    List<EntityMovimento> getTransactionUpToByAccount(Date dayet, int account);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == (:dayet) AND idConto LIKE (:account) AND checked == 'unchecked'")
    LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(Date dayet, int account);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == (:dayet) AND checked == 'unchecked'")
    LiveData<List<EntityMovimento>> getDailyTransactions(Date dayet);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == :dayet AND checked == :checked")
    LiveData<List<EntityMovimento>> getDailyTransactionsChecked(Date dayet, String checked);

}
