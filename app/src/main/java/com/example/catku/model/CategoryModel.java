package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CategoryModel {
    private String name;
    private String type;
    private sqLiteDb dbHelper;

    public CategoryModel(String name, String type, Context context){
        this.name = name;
        this.type = type;
        dbHelper = new sqLiteDb(context);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int addCategory(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY WHERE name ='"+getName()+"' AND type='"+getType()+"'",null);
            cursor.moveToFirst();
             data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "INSERT INTO CATEGORY (" +
                        "name, " +
                        "type " +
                        ") VALUES"
                        + "('" + getName() + "',"
                        + "'" + getType() + "');";
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

    public int updateCategory(int categoryID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int data;
        int status = 0;
        Log.d("CekUpdate", getName()+" "+getType());
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM category WHERE name ='"+getName()+"' AND type='"+getType()+"'",null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data == 0){
                String sqlinsert = "UPDATE category SET name='"+getName()+"',"+"type='"+getType()+"'  WHERE id_category='"+categoryID+"'";
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

    public String getDataCategory(String type){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";
        String query = "";

        if (type.isEmpty()){
            query = "SELECT * FROM category ";
        }else {
            query = "SELECT * FROM category WHERE type='"+type+"'";
        }

        Log.d("CekQueryCategory",query);

        try {
            Cursor cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    jsonObject.put("id_category", cursor.getString(cursor.getColumnIndex("id_category")));
                    jsonObject.put("name", cursor.getString(cursor.getColumnIndex("name")));
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

    public String deleteCategory(int categoryID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";

        try {
            db.execSQL("DELETE FROM category WHERE id_category='"+ categoryID +"'");
            reqpart.put("status", "0");
            reqpart.put("message", "Delete Data Category Successfully");
            arrayData.put(reqpart);
            msg = arrayData + "";
        }catch (Exception e){
            try {
                reqpart.put("status", "-1");
                reqpart.put("message", "Delete Data Category Failed");
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
