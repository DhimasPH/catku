package com.example.catku.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.catku.db.sqLiteDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ReportModel {
    private sqLiteDb dbHelper;
    public ReportModel(Context context){
        dbHelper = new sqLiteDb(context);
    }

    public String getTotalReport(int id_user, String date){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            String query = "";
            if (date.equals("")){
                query = "SELECT SUM(CASE WHEN type ='income' THEN amount ELSE 0 END) AS total_income ," +
                        "SUM(CASE WHEN type ='expense' THEN amount ELSE 0 END) AS total_expense " +
                        " FROM transaksi WHERE id_user ='"+id_user+"'";
            }else {
                query = "SELECT SUM(CASE WHEN type ='income' THEN amount ELSE 0 END) AS total_income ," +
                        "SUM(CASE WHEN type ='expense' THEN amount ELSE 0 END) AS total_expense " +
                        " FROM transaksi WHERE id_user ='"+id_user+"' AND date ='"+date+"'";
            }
            Log.d("CekQueryReport", query);
            Cursor cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            data = cursor.getCount();
            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    if (cursor.getString(cursor.getColumnIndex("total_income")) != null){
                        jsonObject.put("total_income", cursor.getString(cursor.getColumnIndex("total_income")));
                    }else {
                        jsonObject.put("total_income",0);
                    }
                    if (cursor.getString(cursor.getColumnIndex("total_expense")) != null){
                        jsonObject.put("total_expense", cursor.getString(cursor.getColumnIndex("total_expense")));
                    }else {
                        jsonObject.put("total_expense",0);
                    }
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

    public String getIncomeReport(int id_user, String date){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            String query = "";
            if (date.equals("")){
                query = "SELECT xx.id_category, xx.name_category, ROUND((xx.income / xx.income_total) * 100) AS percentage " +
                        " FROM (SELECT a.id_category, b.name as name_category,"+
                        " SUM(CASE WHEN a.type = 'income' THEN a.amount ELSE 0 END) AS income, c.income_total"+
                        " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category"+
                        " CROSS JOIN ( SELECT type,SUM(CASE  WHEN type = 'income' THEN amount ELSE 0 END) AS income_total"+
                        " FROM transaksi WHERE id_user = '"+id_user+"' AND type = 'income' ) c"+
                        " WHERE a.id_user = '"+id_user+"' AND a.type = 'income' GROUP BY  a.id_category ) xx";
            }else {
                query = "SELECT xx.id_category, xx.name_category, ROUND((xx.income / xx.income_total) * 100) AS percentage " +
                        " FROM (SELECT a.id_category, b.name as name_category,"+
                        " SUM(CASE WHEN a.type = 'income' THEN a.amount ELSE 0 END) AS income, c.income_total"+
                        " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category"+
                        " CROSS JOIN ( SELECT type,SUM(CASE  WHEN type = 'income' THEN amount ELSE 0 END) AS income_total"+
                        " FROM transaksi WHERE id_user = '"+id_user+"' AND date = '"+date+"' AND type = 'income' ) c"+
                        " WHERE a.id_user = '"+id_user+"' AND a.date = '"+date+"' AND a.type = 'income' GROUP BY  a.id_category ) xx";
            }
            Log.d("CekQueryIncome",query);
            Cursor cursor = db.rawQuery(query,null);
            Cursor c = db.rawQuery("SELECT xx.id_category, xx.name_category, xx.income" +
                    " FROM (SELECT a.id_category, b.name as name_category," +
                    " SUM(CASE WHEN a.type = 'income' THEN a.amount ELSE 0 END) AS income" +
                    " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category" +
                    " WHERE a.id_user = '"+id_user+"' AND a.type = 'income' GROUP BY  a.id_category ) xx",null);
            Cursor c1 = db.rawQuery("SELECT type, SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END) AS income_total FROM transaksi WHERE id_user = 1 AND `type` = 'income'",null);
            Cursor c2 = db.rawQuery("SELECT a.id_category, b.name as name_category, SUM(CASE WHEN a.type = 'income' THEN a.amount ELSE 0 END) AS income" +
                    " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category",null);
            cursor.moveToFirst();
            c.moveToNext();
            c1.moveToFirst();
            c2.moveToNext();
            data = cursor.getCount();
            if (c.getCount() > 0){
                for (int i = 0; i<c.getCount(); i++){
//                    Log.d("CekIdResult", c.getString(c.getColumnIndex("id_category")));
//                    Log.d("CekNameResult", c.getString(c.getColumnIndex("name_category")));
//                    Log.d("CekResultIncome", c.getString(c.getColumnIndex("income")));
                }
            }
            if (c1.getCount() > 0){
                for (int i = 0; i<c1.getCount(); i++){
//                    Log.d("IncomeTypeCek", c1.getString(c1.getColumnIndex("type")));
//                    Log.d("IncomeTotalCek", c1.getString(c1.getColumnIndex("income_total")));
                    
                }
            }

            if (c2.getCount() > 0){
                for (int i = 0; i<c2.getCount(); i++){
                    Log.d("CekIdCategory2", c2.getString(c2.getColumnIndex("id_category")));
                    Log.d("CekNameCategory2", c2.getString(c2.getColumnIndex("name_category")));
                    Log.d("CekIncome2", c2.getString(c2.getColumnIndex("income")));

                }
            }

            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    if (cursor.getString(cursor.getColumnIndex("id_category")) != null){
                        jsonObject.put("id_category", cursor.getString(cursor.getColumnIndex("id_category")));
                    }else {
                        jsonObject.put("id_category",0);
                    }

                    if (cursor.getString(cursor.getColumnIndex("name_category")) != null){
                        jsonObject.put("name_category", cursor.getString(cursor.getColumnIndex("name_category")));
                    }else {
                        jsonObject.put("name_category","");
                    }

                    if (cursor.getString(cursor.getColumnIndex("percentage")) != null){
                        jsonObject.put("percentage", cursor.getString(cursor.getColumnIndex("percentage")));
                        Log.d("IncomePercen", cursor.getString(cursor.getColumnIndex("percentage")));
                    }else {
                        jsonObject.put("percentage",0);
                    }

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

    public String getExpenseReport(int id_user, String date){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        int data;
        String msg = "";
        String dataJson = "";

        try {
            String query = "";
            if (date.equals("")){
                query = "SELECT xx.id_category, xx.name_category, ROUND((xx.income / xx.income_total) * 100) AS percentage" +
                        " FROM (SELECT a.id_category, b.name as name_category," +
                        " SUM(CASE WHEN a.type = 'expense' THEN a.amount ELSE 0 END) AS income, c.income_total" +
                        " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category" +
                        " CROSS JOIN ( SELECT type, SUM (CASE WHEN type = 'expense' THEN amount ELSE 0 END) AS income_total" +
                        " FROM transaksi WHERE id_user = '"+id_user+"' AND type = 'expense') c" +
                        " WHERE a.id_user = '"+id_user+"' AND a.type = 'expense' GROUP BY  a.id_category ) xx";
            }else {
                query = "SELECT xx.id_category, xx.name_category, ROUND((xx.income / xx.income_total) * 100) AS percentage" +
                        " FROM (SELECT a.id_category, b.name as name_category," +
                        " SUM(CASE WHEN a.type = 'expense' THEN a.amount ELSE 0 END) AS income, c.income_total" +
                        " FROM transaksi a INNER JOIN category b ON a.id_category = b.id_category" +
                        " CROSS JOIN ( SELECT type, SUM (CASE WHEN type = 'expense' THEN amount ELSE 0 END) AS income_total" +
                        " FROM transaksi WHERE id_user = '"+id_user+"' AND date = '"+date+"' AND type = 'expense') c" +
                        " WHERE a.id_user = '"+id_user+"' AND a.date = '"+date+"' AND a.type = 'expense' GROUP BY  a.id_category ) xx";
            }
            Log.d("CekQueryExpense",query);
            Cursor cursor = db.rawQuery(query,null);
            cursor.moveToFirst();
            data = cursor.getCount();

            if (data > 0){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i<cursor.getCount(); i++){
                    JSONObject jsonObject = new JSONObject();
                    cursor.moveToPosition(i);
                    if (cursor.getString(cursor.getColumnIndex("id_category")) != null){
                        jsonObject.put("id_category", cursor.getString(cursor.getColumnIndex("id_category")));
                    }else {
                        jsonObject.put("id_category",0);
                    }

                    if (cursor.getString(cursor.getColumnIndex("name_category")) != null){
                        jsonObject.put("name_category", cursor.getString(cursor.getColumnIndex("name_category")));
                    }else {
                        jsonObject.put("name_category","");
                    }

                    if (cursor.getString(cursor.getColumnIndex("percentage")) != null){
                        jsonObject.put("percentage", cursor.getString(cursor.getColumnIndex("percentage")));
                        Log.d("ExpensePercen", cursor.getString(cursor.getColumnIndex("percentage")));
                    }else {
                        jsonObject.put("percentage",0);
                    }
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
}