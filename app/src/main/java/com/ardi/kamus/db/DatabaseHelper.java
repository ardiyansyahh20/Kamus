package com.ardi.kamus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.ardi.kamus.db.DatabaseContract.KamusColumns.DESC;
import static com.ardi.kamus.db.DatabaseContract.KamusColumns.WORD;
import static com.ardi.kamus.db.DatabaseContract.TABLE_EN_TO_ID;
import static com.ardi.kamus.db.DatabaseContract.TABLE_ID_TO_EN;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbkamus";
    private static final int DATABASE_VERSION = 1;

    private static String CREATE_TABLE_ID_TO_EN = "create table " + TABLE_ID_TO_EN +
            " (" + _ID + " integer primary key autoincrement, " +
            WORD + " text not null, " +
            DESC + " text not null);";

    private static String CREATE_TABLE_EN_TO_ID = "create table " + TABLE_EN_TO_ID +
            " (" + _ID + " integer primary key autoincrement, " +
            WORD + " text not null, " +
            DESC + " text not null);";

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ID_TO_EN);
        db.execSQL(CREATE_TABLE_EN_TO_ID);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_ID_TO_EN);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_EN_TO_ID);
        onCreate(db);
    }
}
