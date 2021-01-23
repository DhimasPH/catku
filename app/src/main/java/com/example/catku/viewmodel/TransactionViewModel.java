package com.example.catku.viewmodel;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catku.model.CategoryModel;
import com.example.catku.model.ScheduleModel;
import com.example.catku.model.TransactionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionViewModel extends ViewModel {
    public MutableLiveData<String> add = new MutableLiveData<String>();
    public MutableLiveData<String> select = new MutableLiveData<String>();
    public MutableLiveData<String> update = new MutableLiveData<String>();
    public MutableLiveData<String> delete = new MutableLiveData<String>();

    public void add(int id_user, int id_category, String type, String image, int amount, String description, String memo, String date, String time, Context context){
        TransactionModel model = new TransactionModel(context);
        final int code = model.add(id_user, id_category,type,image,amount,description,memo,date,time );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONArray arrayData = new JSONArray();
                JSONObject reqpart = new JSONObject();
                String msg = "";
                String Response;

                if(code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", "Add Transaction Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Add Transaction Failed, Your category have been exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Add Transaction Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                add.postValue(Response);
            }
        }, 2000);
    }

    public void update(int id_transaction,int id_user, int id_category, String type, String image, int amount, String description, String memo, String date, String time, Context context){
        TransactionModel model = new TransactionModel(context);
        final int code = model.update(id_transaction,id_user, id_category,type,image,amount,description,memo,date,time );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONArray arrayData = new JSONArray();
                JSONObject reqpart = new JSONObject();
                String msg = "";
                String Response;

                if(code == 0) {
                    try {
                        reqpart.put("status", "Success");
                        reqpart.put("message", "Update Transaction Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Transaction Failed, Your category have been exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Transaction Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                update.postValue(Response);
            }
        }, 2000);
    }

    public void getData(int id_user, Context context){
        TransactionModel model = new TransactionModel(context);
        final String data = model.getData(id_user);
        System.out.println("@CheckSession "+data);

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
        System.out.println("@CheckSession: "+Response);
        select.postValue(Response);
    }

    public void deleteData(Context context, int id_transaction, int id_user){
        TransactionModel model = new TransactionModel(context);
        final String data = model.delete(id_transaction,id_user);
        System.out.println("@DeleteTemp "+data);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(data);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Code = Integer.parseInt(jso.getString("status"));
                message = jso.getString("message");
                if(Code == 0) {
                    reqpart.put("status", "Success");
                    reqpart.put("message", message);
                    arrayData.put(reqpart);
                    msg = arrayData + "";
                }else {
                    reqpart.put("status", "Failed");
                    reqpart.put("message", message);
                    arrayData.put(reqpart);
                    msg = arrayData + "";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                reqpart.put("status", "Failed");
                reqpart.put("message", "Something went wrong");
                arrayData.put(reqpart);
                msg = arrayData + "";
            }catch (Exception i){

            }
        }
        Response = msg;
        System.out.println("@DeleteTemp: "+Response);
        delete.postValue(Response);
    }
}
