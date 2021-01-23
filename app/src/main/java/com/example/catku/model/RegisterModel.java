package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;


public class RegisterModel {
    private String name;
    private String username;
    private String password;
    private sqLiteDb dbHelper;

    public RegisterModel(String name, String username, String password, Context context){
        this.name = name;
        this.username = username;
        this.password = password;
        dbHelper = new sqLiteDb(context);
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int addUserData(String name, String username, String password){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username ='" + username +"'",null);
            cursor.moveToFirst();
             data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO user (" +
                        "name, " +
                        "username, " +
                        "password, " +
                        "balance " +
                        ") VALUES"
                        + "('" + name + "',"
                        + "'" + username + "',"
                        + "'" + password + "',"
                        + "'" + 0 + "');";
                db.execSQL(sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public int updateUser(int userID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data = 0;
        int status = 0;
        try {
//            Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE username ='"+getUsername()+"'",null);
//            cursor.moveToFirst();
//            data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "";
                if (getPassword().isEmpty()){
                    sqlinsert = "UPDATE user SET name='"+getName()+"',"+"username='"+getUsername()+"'  WHERE id_user='"+userID+"'";
                }else {
                    sqlinsert = "UPDATE user SET name='"+getName()+"',"+"username='"+getUsername()+"',"+"password='"+getPassword()+"'  WHERE id_user='"+userID+"'";
                }
                db.execSQL(sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
//            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public int updateUserBalance(int userID,int balance){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username ='"+getUsername()+"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "UPDATE user SET balance='"+balance+"' WHERE id_user='"+userID+"'";
                db.execSQL(sqlinsert);
                status = 0;
            }else {
                status = -1;
            }
            cursor.close();

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }
}
