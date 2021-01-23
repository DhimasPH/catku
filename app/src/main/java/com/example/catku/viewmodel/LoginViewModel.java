package com.example.catku.viewmodel;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.catku.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> loginStatus = new MutableLiveData<String>();
    public MutableLiveData<String> sessionStatus = new MutableLiveData<String>();
    public MutableLiveData<String> user = new MutableLiveData<String>();
    public MutableLiveData<String> sessionStatus1 = new MutableLiveData<String>();
    public MutableLiveData<String> user1 = new MutableLiveData<String>();
    public MutableLiveData<String> logOutStatus = new MutableLiveData<String>();


    public void doLogin(String userName, String password, Context context) {
        LoginModel loginModel = new LoginModel(userName, password, context);
        final int code = loginModel.checkUserValidity(userName,password);
        System.out.println("@Code "+code);

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
                        reqpart.put("message", "Login Successfully");
                        reqpart.put("code", "0");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Email or Password wrong.");
                        reqpart.put("code", "-1");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (code == -2){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Your email have not be exist.");
                        reqpart.put("code", "-2");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Login Failed, Something went wrong.");
                        reqpart.put("code", "-3");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;
                System.out.println("@LOGIN: "+Response);

                loginStatus.postValue(Response);
            }
        }, 2000);
    }

    public void checkSession(Context context){
        LoginModel loginModel = new LoginModel("", "", context);
        final String data = loginModel.checkSession();
        System.out.println("@Code "+data);

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
        sessionStatus.postValue(Response);
    }

    public void checkSession1(Context context){
        LoginModel loginModel = new LoginModel("", "", context);
        final String data = loginModel.checkSession();
        System.out.println("@Code "+data);

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
        System.out.println("@CheckSession1: "+Response);
        sessionStatus1.postValue(Response);
    }

    public void getUser(int id_user, Context context){
        LoginModel loginModel = new LoginModel("", "", context);
        final String data = loginModel.getDataUser(id_user);
        System.out.println("@Code "+data);

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
        user.postValue(Response);
    }

    public void getUser1(int id_user, Context context){
        LoginModel loginModel = new LoginModel("", "", context);
        final String data = loginModel.getDataUser(id_user);
        System.out.println("@Code "+data);

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
        user1.postValue(Response);
    }

    public void logOut(Context context){
        LoginModel logOutModel = new LoginModel("","",context);
        final String logOut = logOutModel.logOut();
        System.out.println("@Logout "+logOut);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(logOut);
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
        System.out.println("@Logout: "+Response);
        logOutStatus.postValue(Response);
    }
}
