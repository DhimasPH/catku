package com.example.catku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.catku.viewmodel.RegisterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private Button button;
    private Button btnRegister;
    private EditText nameRegis;
    private EditText usernameRegis;
    private EditText passwordRegis;
    private ProgressDialog progressDialog = null;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = findViewById(R.id.button_to_login);
        btnRegister = findViewById(R.id.button_to_register);
        nameRegis = findViewById(R.id.et_your_name);
        usernameRegis = findViewById(R.id.et_regis_username);
        passwordRegis = findViewById(R.id.et_password_regis);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerResult();
            }
        });

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.registerStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(Register.this, Login.class);
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


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    private void registerResult(){
        String name = nameRegis.getText().toString();
        String username = usernameRegis.getText().toString();
        String password = passwordRegis.getText().toString();


        boolean cancel = false;
        View focusView = null;

        Pattern p = Pattern.compile("^[a-zA-Z' ]*$");
        Matcher m1;
        m1 = p.matcher(name);

        if(!m1.find()) {
            nameRegis.setError("This field is required!");
            focusView = nameRegis;
            cancel = true;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usernameRegis.setError("Invalid Email Format");
            focusView = usernameRegis;
            cancel = true;
        }

        if (TextUtils.isEmpty(username) || username == "") {
            usernameRegis.setError("This field is required!");
            focusView = usernameRegis;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordRegis.setError("This password is to short!");
            focusView = passwordRegis;
            cancel = true;
        }

        if (TextUtils.isEmpty(name) || name == "") {
            nameRegis.setError("This field is required!");
            focusView = nameRegis;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialogShowing();
            registerViewModel.addUserData(name,username,password,this);
        }
    }

    public void openLogin(){
        Intent intent= new Intent(this, Login.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }

    public void exitApp(){
        this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.up_to_bottom_out);
    }

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                exitApp();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
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

}