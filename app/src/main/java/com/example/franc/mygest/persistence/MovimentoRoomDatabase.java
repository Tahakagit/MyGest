package com.example.franc.mygest.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by franc on 23/04/2018.
 */

@Database(entities = {EntityMovimento.class, EntityConto.class}, version = 1, exportSchema = false)
public abstract class MovimentoRoomDatabase extends RoomDatabase {

    public abstract MovimentoDao movimentoDao();
    public abstract ContoDao contoDao();

    private static MovimentoRoomDatabase INSTANCE;


    static MovimentoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovimentoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovimentoRoomDatabase.class, "movimento_database")
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}
