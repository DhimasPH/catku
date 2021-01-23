package com.example.catku.viewmodel;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catku.model.LoginModel;
import com.example.catku.model.ReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportViewModel extends ViewModel {
    public MutableLiveData<String> total = new MutableLiveData<String>();
    public MutableLiveData<String> income = new MutableLiveData<String>();
    public MutableLiveData<String> expense = new MutableLiveData<String>();

    public void getTotalReport(int id_user,String date, Context context){
        ReportModel reportModel = new ReportModel(context);
        final String data = reportModel.getTotalReport(id_user,date);
        System.out.println("@ReportTotal "+data);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String message = "";
        String Response;

        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(data);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (Code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
        Response = msg;
        System.out.println("@ReportTotal: "+Response);
        total.postValue(Response);
    }

    public void getIncomeReport(int id_user,String date, Context context){
        ReportModel reportModel = new ReportModel(context);
        final String data = reportModel.getIncomeReport(id_user,date);
        System.out.println("@ReportIncome "+data);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String message = "";
        String Response;

        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(data);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (Code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
        Response = msg;
        System.out.println("@ReportIncome: "+Response);
        income.postValue(Response);
    }

    public void getExpenseReport(int id_user,String date, Context context){
        ReportModel reportModel = new ReportModel(context);
        final String data = reportModel.getExpenseReport(id_user,date);
        System.out.println("@ReportExpense "+data);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String message = "";
        String Response;

        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(data);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (Code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", message);
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
        Response = msg;
        System.out.println("@ReportExpense: "+Response);
        expense.postValue(Response);
    }
}
