package com.example.catku.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqLiteDb extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "catku.db";
    private static final int DATABASE_VERSION = 1;


    public String USER = "CREATE TABLE IF NOT EXISTS user( " +
            "id_user integer primary key AUTOINCREMENT, " +
            "name text null, " +
            "username text null, " +
            "password text null, " +
            "balance integer null)";

    public String SESLOG = "CREATE TABLE IF NOT EXISTS session_login( "
            + " id integer primary key AUTOINCREMENT,"
            + " id_user integer null,"
            + " username text null,"
            + " name text null);";

    public String TRANSACTION = "CREATE TABLE IF NOT EXISTS transaksi( "
            + " id_transaction integer primary key AUTOINCREMENT,"
            + " id_user integer null,"
            + " id_category integer null,"
            + " type text null,"
            + " image text null,"
            + " amount integer null,"
            + " description text null,"
            + " memo text null,"
            + " date text null,"
            + " time text null);";

    public String SCHEDULE = "CREATE TABLE IF NOT EXISTS schedule( "
            + " id_schedule integer primary key AUTOINCREMENT,"
            + " set_every text null,"
            + " id_category integer null,"
            + " time text null,"
            + " date text null,"
            + " description text null,"
            + " amount integer null,"
            + " type text null);";

    public String CATEGORY = "CREATE TABLE IF NOT EXISTS category( "
            + " id_category integer primary key AUTOINCREMENT,"
            + " name text null,"
            + " type text null);";

    public sqLiteDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER);
        db.execSQL(SESLOG);
        db.execSQL(TRANSACTION);
        db.execSQL(SCHEDULE);
        db.execSQL(CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close(SQLiteDatabase db) {
        db.close();
    }
}
