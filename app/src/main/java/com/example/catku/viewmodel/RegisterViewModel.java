package com.example.catku.viewmodel;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.catku.model.RegisterModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> registerStatus = new MutableLiveData<String>();
    public MutableLiveData<String> updateUser = new MutableLiveData<String>();
    public MutableLiveData<String> updateUserBalance = new MutableLiveData<String>();

    public void addUserData(String name, String username, String password, Context context){
        RegisterModel registerModel = new RegisterModel(name,username,password,context);
        final int code = registerModel.addUserData(name,username,password);

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
                        reqpart.put("message", "Register Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Register Failed, Your email have been exists.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Register Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                registerStatus.postValue(Response);
            }
        }, 2000);
    }

    public void updateUserData(int id_user,String name, String username, String password, Context context){
        RegisterModel registerModel = new RegisterModel(name,username,password,context);
        final int code = registerModel.updateUser(id_user);

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
                        reqpart.put("message", "Update User Data Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update User Data Failed, Your email have been exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update User Data Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@UpdateUserData: "+Response);

                updateUser.postValue(Response);
            }
        }, 2000);
    }

    public void updateUserBalance(int id_user,String name, String username, String password,int balance, Context context){
        RegisterModel registerModel = new RegisterModel(name,username,password,context);
        final int code = registerModel.updateUserBalance(id_user,balance);

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
                        reqpart.put("message", "Update Balance Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Balance Failed, Your balance have been exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Balance Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Balance: "+Response);

                updateUserBalance.postValue(Response);
            }
        }, 2000);
    }
}
