package com.example.franc.mygest.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by franc on 23/04/2018.
 */
@Dao
public interface ContoDao {

    @Insert
    void insert(EntityConto conto);

    @Delete
    void delete(EntityConto conto);

    @Update(onConflict = REPLACE)
    void update(EntityConto conto);

    @Query("DELETE FROM conto_table")
    void deleteAll();

    @Query("SELECT * from conto_table")
    LiveData<List<EntityConto>> getAllAccounts();

    @Query("SELECT * from conto_table WHERE nomeConto LIKE :accountName")
    EntityConto getAccountIdByName(String accountName);

    @Query("SELECT * from conto_table")
    List<EntityConto> getAllAccountsName();

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM conto_table WHERE id IN (SELECT DISTINCT idConto FROM movimento_table WHERE scadenza <= :upTo AND checked ==  'unchecked')")
    LiveData<List<EntityConto>> getAllAccountsDist(Date upTo);
}
