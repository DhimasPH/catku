package com.example.catku.viewmodel;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catku.model.CategoryModel;
import com.example.catku.model.RegisterModel;
import com.example.catku.object.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {
    public MutableLiveData<String> addCategory = new MutableLiveData<String>();
    public MutableLiveData<String> selectCategory = new MutableLiveData<String>();
    public MutableLiveData<String> updateCategory = new MutableLiveData<String>();
    public MutableLiveData<String> deleteCategory = new MutableLiveData<String>();

    public void addCategory(String name, String type, Context context){
        CategoryModel categoryModel = new CategoryModel(name,type,context);
        final int code = categoryModel.addCategory();

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
                        reqpart.put("message", "Add Category Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Add Category Failed, Your category have been exists.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Add Category Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                addCategory.postValue(Response);
            }
        }, 2000);
    }

    public void updateCategory(int id_category,String name, String type, Context context){
        CategoryModel categoryModel = new CategoryModel(name,type,context);
        final int code = categoryModel.updateCategory(id_category);

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
                        reqpart.put("message", "Update Category Successfully");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (code == -1){
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Category Failed, Your category have been exist.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        reqpart.put("status", "Failed");
                        reqpart.put("message", "Update Category Failed, Something went wrong.");
                        arrayData.put(reqpart);
                        msg = arrayData + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Response = msg;

                System.out.println("@Register: "+Response);

                updateCategory.postValue(Response);
            }
        }, 2000);
    }

    public void getCategory(String type,Context context){
        CategoryModel categoryModel = new CategoryModel("","", context);
        final String dataCategory = categoryModel.getDataCategory(type);
        System.out.println("@CheckSession "+dataCategory);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String message = "";
        String Response;

        int Code = 0;
        try {
            JSONArray jsa = new JSONArray(dataCategory);
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
        selectCategory.postValue(Response);
    }

    public void deleteCategory(Context context, int id_category){
        CategoryModel delCategory = new CategoryModel("","",context);
        final String delete = delCategory.deleteCategory(id_category);
        System.out.println("@DeleteTemp "+delete);

        JSONArray arrayData = new JSONArray();
        JSONObject reqpart = new JSONObject();
        String msg = "";
        String Response;

        String message = "";
        int Code = 0;

        try {
            JSONArray jsa = new JSONArray(delete);
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
        deleteCategory.postValue(Response);
    }

}
