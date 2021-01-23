package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ScheduleModel {
    private sqLiteDb dbHelper;

    public ScheduleModel( Context context){
        dbHelper = new sqLiteDb(context);
    }

    public int addSchedule(String set_every, int id_category, String time, String date, String description, int amount, String type){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data = 0;
        int status = 0;

        try {

            if (data == 0){
                String sqlinsert = "INSERT INTO schedule (" +
                        "set_every, " +
                        "id_category, " +
                        "time, " +
                        "date, " +
                        "description, " +
                        "amount, " +
                        "type " +
                        ") VALUES"
                        + "('" + set_every + "',"
                        + "'" + id_category + "',"
                        + "'" + time + "',"
                        + "'" + date + "',"
                        + "'" + description + "',"
                        + "'" + amount + "',"
                        + "'" + type + "');";
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

    public int updateSchedule(int id_schedule, String set_every, int id_category, String time, String date, String description, int amount, String type){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data = 0;
        int status = 0;
        Log.d("CekDesc", description);
        try {

            if (data == 0){
                String sqlinsert = "UPDATE schedule SET "+
                        "set_every='"+set_every+"',"+
                        "id_category='"+id_category+"',"+
                        "time='"+time+"',"+
                        "date='"+date+"',"+
                        "description='"+description+"',"+
                        "amount='"+amount+"',"+
                        "type='"+type+"'"+
                        " WHERE id_schedule='"+id_schedule+"'";
                Log.d("CekQuery",sqlinsert);
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

    public String getDataSchedule(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM schedule",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    jsonObject.put("id_schedule", cursor.getString(cursor.getColumnIndex("id_schedule")));
                    jsonObject.put("set_every", cursor.getString(cursor.getColumnIndex("set_every")));
                    jsonObject.put("id_category", cursor.getString(cursor.getColumnIndex("id_category")));
                    jsonObject.put("time", cursor.getString(cursor.getColumnIndex("time")));
                    jsonObject.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    jsonObject.put("description", cursor.getString(cursor.getColumnIndex("description")));
                    jsonObject.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
                    jsonObject.put("type", cursor.getString(cursor.getColumnIndex("type")));
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

    public String delete(int id_schedule){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM schedule WHERE id_schedule='"+ id_schedule +"'");
            reqpart.put("status", "0");
            reqpart.put("message", "Delete Data Schedule Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Delete Data Schedule Failed");
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
