package com.example.franc.mygest.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
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


    @Query("SELECT DISTINCT(beneficiario) from movimento_table")
    List<String> getKnownBeneficiari();

    @Query("SELECT * from movimento_table WHERE checked == :checked")
    LiveData<List<EntityMovimento>> getAllTransactionsChecked(String checked);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT scadenza, nomeConto, idConto, id, checked FROM movimento_table WHERE scadenza  <= :upTo AND idConto == (:account) AND checked == 'unchecked' GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesByAccount(Date upTo, int account);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE scadenza  == :upTo AND idConto == :account AND checked == :checked AND beneficiario == :beneficiario")
    LiveData<List<EntityMovimento>> getAllDayFiltered(Date upTo, int account, String checked, String beneficiario);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedBeneNoACcount(String checked, String beneficiario);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and idConto == :account GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedNoBeneAccount(String checked, int account);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedNoBeneAllAccount(String checked);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario AND idConto == :account GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedBeneAccount(int account, String checked, String beneficiario);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedBeneAllAccount(String checked, String beneficiario);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked GROUP BY scadenza")
    LiveData<List<EntityMovimento>> getAllDatesCheckedNoBeneNoAccountGroup(String checked);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario AND scadenza == :date")
    LiveData<List<EntityMovimento>> getDailyCheckedBeneNoACcount(String checked, String beneficiario, Date date);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and idConto == :account AND scadenza == :date")
    LiveData<List<EntityMovimento>> getDailyCheckedNoBeneAccount(String checked, int account, Date date);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked AND scadenza == :date")
    LiveData<List<EntityMovimento>> getDailyCheckedNoBeneAllAccount(String checked, Date date);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario AND idConto == :account AND scadenza == :date")
    LiveData<List<EntityMovimento>> getDailyCheckedBeneAccount(int account, String checked, String beneficiario, Date date);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked and beneficiario == :beneficiario AND scadenza == :date")
    LiveData<List<EntityMovimento>> getDailyCheckedBeneAllAccount(String checked, String beneficiario, Date date);
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked AND scadenza == :date ")
    LiveData<List<EntityMovimento>> getDailyCheckedNoBeneNoAccountGroup(String checked, Date date);



    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM movimento_table WHERE checked == :checked")
    LiveData<List<EntityMovimento>> getAllDatesNoBene(String checked);


    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza <= (:dayet) AND checked == 'unchecked' AND direction == (:direction) AND idConto LIKE (:account)")
    List<EntityMovimento> getTransactionsUpToByAccount(Date dayet, int account, String direction);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT COUNT(*) FROM movimento_table WHERE scadenza <= (:dayet) AND checked == 'unchecked' AND idConto  LIKE (:account)")
    int getTotalTransactionsUpToByAccount(Date dayet, int account);

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


    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == :dayet AND checked == :checked AND idConto == :account")
    LiveData<List<EntityMovimento>> getDailyTransactionsNoBeneAccount(Date dayet, String checked, String account);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == :dayet AND checked == :checked AND idConto == :account AND beneficiario ==  :beneficiario")
    LiveData<List<EntityMovimento>> getDailyTransactionsBeneAccount(Date dayet, String checked, String beneficiario, String account);

    @TypeConverters(DateConverter.class)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM movimento_table WHERE scadenza == :dayet AND checked == :checked AND beneficiario == :beneficiario")
    LiveData<List<EntityMovimento>> getDailyTransactionsCheckedFilter(Date dayet, String checked, String beneficiario);

}
