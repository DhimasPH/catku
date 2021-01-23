package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginModel {
    private String username;
    private String password;
    private sqLiteDb dbHelper;
    public LoginModel(String username, String password, Context context){
        this.username = username;
        this.password = password;
        dbHelper = new sqLiteDb(context);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



     public int checkUserValidity(String username, String password){
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         int data;
         int status = 0;

         try {
             Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username ='" + username +"'",null);
             cursor.moveToFirst();
             data = cursor.getCount();

             if (data > 0){
                 for (int i = 0; i<cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    if (password.equals(cursor.getString(cursor.getColumnIndex("password")))){
                        try {
                            String sqlinsert = "INSERT INTO session_login (" +
                                    "id, " +
                                    "id_user, " +
                                    "username, " +
                                    "name " +
                                    ") VALUES"
                                    + "('" + 1 + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("id_user")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("username")) + "',"
                                    + "'" + cursor.getString(cursor.getColumnIndex("name")) + "');";
                            db.execSQL(sqlinsert);
                            status = 0;
                        }catch (Exception e){
                            Log.d("ExceptionErrorInsert",e.toString());
                        }
                    }else {
                        status = -1;
                    }
                 }
             }else {
                 status = -2;
             }
             cursor.close();

         }catch (Exception e){
             status = -3;
             Log.d("ExceptionError",e.toString());
         }
         return status;
     }

    public String getDataUser(int id_user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM user WHERE id_user='"+id_user+"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data > 0){

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    jsonObject.put("id_user", cursor.getString(cursor.getColumnIndex("id_user")));
                    jsonObject.put("username", cursor.getString(cursor.getColumnIndex("username")));
                    jsonObject.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    jsonObject.put("balance", cursor.getString(cursor.getColumnIndex("balance")));
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

     public String checkSession(){
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         JSONArray arrayData = new JSONArray();
         JSONObject reqpart = new JSONObject();
         int data;
         String msg = "";
         String dataJson = "";

         try {
             Cursor cursor = db.rawQuery("SELECT * FROM session_login ",null);
             cursor.moveToFirst();
             data = cursor.getCount();

             if (data > 0){
                 JSONArray jsonArray = new JSONArray();
                 for (int i = 0; i<cursor.getCount(); i++){
                     JSONObject jsonObject = new JSONObject();
                     cursor.moveToPosition(i);
                     jsonObject.put("id_user", cursor.getString(cursor.getColumnIndex("id_user")));
                     jsonObject.put("username", cursor.getString(cursor.getColumnIndex("username")));
                     jsonObject.put("name", cursor.getString(cursor.getColumnIndex("name")));
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

    public String logOut(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM session_login");
            reqpart.put("status", "0");
            reqpart.put("message", "Logout Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Logout Failed");
                arrayData.put(reqpart);
                msg = arrayData + "";
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.d("ExceptionErrorLogout",e.toString());
        }

        return msg;
    }
}