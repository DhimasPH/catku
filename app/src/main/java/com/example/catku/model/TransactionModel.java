package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TransactionModel {
    private sqLiteDb dbHelper;

    public TransactionModel( Context context){
        dbHelper = new sqLiteDb(context);
    }

    public int add( int id_user, int id_category, String type, String image, int amount, String description, String memo, String date, String time){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data = 0;
        int status = 0;

        try {

            if (data == 0){
                String sqlinsert = "INSERT INTO transaksi (" +
                        "id_user, " +
                        "id_category, " +
                        "type, " +
                        "image, " +
                        "amount, " +
                        "description, " +
                        "memo, " +
                        "date, " +
                        "time " +
                        ") VALUES"
                        + "('" + id_user + "',"
                        + "'" + id_category + "',"
                        + "'" + type + "',"
                        + "'" + image + "',"
                        + "'" + amount + "',"
                        + "'" + description + "',"
                        + "'" + memo + "',"
                        + "'" + date + "',"
                        + "'" + time + "');";
                db.execSQL(sqlinsert);
                status = 0;
            }else {
                status = -1;
            }

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public int update(int id_transaction, int id_user, int id_category, String type, String image, int amount, String description, String memo, String date, String time){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data = 0;
        int status = 0;

        try {

            if (data == 0){
                String sqlinsert = "UPDATE transaksi SET "+
                        "id_user='"+id_user+"',"+
                        "id_category='"+id_category+"',"+
                        "type='"+type+"',"+
                        "image='"+image+"',"+
                        "amount='"+amount+"',"+
                        "description='"+description+"',"+
                        "memo='"+memo+"',"+
                        "date='"+date+"',"+
                        "time='"+time+"'"+
                        " WHERE id_transaction='"+id_transaction+"' AND id_user='"+id_user+"'";
                db.execSQL(sqlinsert);
                Log.d("CekQueryUpdateTran", sqlinsert);
                status = 0;
            }else {
                status = -1;
            }

        }catch (Exception e){
            status = -2;
            Log.d("ExceptionError",e.toString());
        }
        return status;
    }

    public String getData(int id_user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            Cursor cursor = db.rawQuery("SELECT a.* , b.name FROM transaksi as a INNER JOIN category as b ON a.id_category = b.id_category WHERE a.id_user='"+id_user+"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    jsonObject.put("id_transaction", cursor.getString(cursor.getColumnIndex("id_transaction")));
                    jsonObject.put("id_user", cursor.getString(cursor.getColumnIndex("id_user")));
                    jsonObject.put("id_category", cursor.getString(cursor.getColumnIndex("id_category")));
                    jsonObject.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    jsonObject.put("type", cursor.getString(cursor.getColumnIndex("type")));
                    jsonObject.put("image", cursor.getString(cursor.getColumnIndex("image")));
                    jsonObject.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
                    jsonObject.put("description", cursor.getString(cursor.getColumnIndex("description")));
                    jsonObject.put("memo", cursor.getString(cursor.getColumnIndex("memo")));
                    jsonObject.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    jsonObject.put("time", cursor.getString(cursor.getColumnIndex("time")));
                    jsonArray.put(jsonObject);
                    dataJson = jsonArray + "";
                }
                reqpart.put("status", "0");
                reqpart.put("message", dataJson);
                arrayData.put(reqpart);
                msg = arrayData + "";
            }else {
                reqpart.put("status", "-1");
                reqpart.put("message", "No data we are found");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }
            cursor.close();

        }catch (Exception e){
            try {
                reqpart.put("status", "-2");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionError",e.toString());
        }
        return msg;
    }

    public String delete(int id_transaction, int id_user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM transaksi WHERE id_transaction='"+ id_transaction +"' AND id_user='"+id_user+"'");
            reqpart.put("status", "0");
            reqpart.put("message", "Delete Data Transaction Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Delete Data Transaction Failed");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorDelete",e.toString());
        }

        return msg;
    }

}
