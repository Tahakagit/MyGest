package com.example.franc.mygest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Tahaka on 18-Jul-17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
//prova

    ArrayList<String> movimenti = new ArrayList<String>();


    public static final String DATABASE_NAME = "Movimenti.db";
    public static final String TABLE_MOVIMENTI = "Movimenti";

    public static final String COL_ID = "ID";
    public static final String COL_BENEFICIARIO = "BENEFICIARIO";
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create an empty array list with an initial capacity

        db.execSQL("CREATE TABLE " + TABLE_MOVIMENTI + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BENEFICIARIO + " VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMENTI + ";");
        onCreate(db);
    }
//Todo: il db salva solamente l'ultimo record
    //Inserisci nel db
    public void addMovimento(String movimento){
        ContentValues values = new ContentValues();
        values.put(COL_BENEFICIARIO, movimento);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MOVIMENTI, null, values);
        db.close();
    }

    //delete movimento
    public void delMovimento(String movimento){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MOVIMENTI + " WHERE " + COL_BENEFICIARIO + "=\"" + movimento + "\";");
    }

    //inserisco nella lista di dati
    public ArrayList<String> dbToString(){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIMENTI + " WHERE 1";

        //cursor point
        Cursor c = db.rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToLast())
        {
            do
            {
                try{
                    movimenti.add(c.getString(c.getColumnIndex("BENEFICIARIO")));
                } catch (Exception h){
                    Log.i("FAILED", c.getString(c.getColumnIndex("BENEFICIARIO")));
                }
            }
            while (c.moveToNext());

        }

        c.close();
        db.close();
        return movimenti;
    }

}
