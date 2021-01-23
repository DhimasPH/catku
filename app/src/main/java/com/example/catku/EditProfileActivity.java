package com.example.catku;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.catku.viewmodel.LoginViewModel;
import com.example.catku.viewmodel.RegisterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private TextView tvNameProfile;
    private TextView tvUsernameProfile;
    private EditText etNameProfile;
    private EditText etUsernameProfile;
    private EditText etPasswordProfile;
    private CardView btnCardSave;
    LoginViewModel loginViewModel;
    RegisterViewModel registerViewModel;
    private ProgressDialog progressDialog = null;
    ActionBar actionBar;
    int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit Profile");

        tvNameProfile = findViewById(R.id.tv_name_profile);
        tvUsernameProfile = findViewById(R.id.tv_username_profile);
        etNameProfile = findViewById(R.id.et_current_name_profile);
        etUsernameProfile = findViewById(R.id.et_current_username_profile);
        etPasswordProfile = findViewById(R.id.et_new_password_profile);
        btnCardSave = findViewById(R.id.btn_card_edit_profile);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.user.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                getDataUser(response);
            }
        });
        loginViewModel.sessionStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                getSession(response);
            }
        });

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.updateUser.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

        btnCardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        loginViewModel.checkSession(this);
    }

    public void Result(String Response){
        try {
            String Status = "";
            String Message = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        intent.putExtra("index","3");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSession(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");


            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                String username;
                String name;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    userID = jso.getInt("id_user");
                    username = jso.getString("username");
                    name = jso.getString("name");
                    loginViewModel.getUser(userID,this);
                }
            }else {
                loginViewModel.logOut(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataUser(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");


            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                int id_user = 0;
                String username;
                String name;
                int balance = 0;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    id_user = jso.getInt("id_user");
                    username = jso.getString("username");
                    name = jso.getString("name");
                    balance = jso.getInt("balance");
                    tvNameProfile.setText(name);
                    tvUsernameProfile.setText(username);

                    etNameProfile.setText(name);
                    etUsernameProfile.setText(username);
                }
            }else {
                loginViewModel.logOut(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void editProfile(){
        String name = etNameProfile.getText().toString();
        String username = etUsernameProfile.getText().toString();
        String password = etPasswordProfile.getText().toString();

        boolean cancel = false;
        View focusView = null;

        Pattern p = Pattern.compile("^[a-zA-Z' ]*$");
        Matcher m1;
        m1 = p.matcher(name);

        if(!m1.find()) {
            etNameProfile.setError("This field is required!");
            focusView = etNameProfile;
            cancel = true;
        }
        if (TextUtils.isEmpty(name) || name == "") {
            etNameProfile.setError("This field is required!");
            focusView = etNameProfile;
            cancel = true;
        }

//        if (TextUtils.isEmpty(password) || password == "") {
//            etPasswordProfile.setError("This field is required!");
//            focusView = etPasswordProfile;
//            cancel = true;
//        }

            if (cancel) {
                focusView.requestFocus();
            } else {
                progressDialogShowing();
                registerViewModel.updateUserData(userID,name,username,password,EditProfileActivity.this);
            }
    }

    public void progressDialogShowing() {
        progressDialogDismiss();
        progressDialog = null;
        progressDialog = ProgressDialog.show(this, "", "Loading...");
    }

    public void progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra("index","3");
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }
}