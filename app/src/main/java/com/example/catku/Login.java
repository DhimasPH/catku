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

import com.example.catku.viewmodel.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private Button button2;
    private Button btnLogin;
    private EditText username;
    private EditText password;
    private ProgressDialog progressDialog = null;
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button2 = findViewById(R.id.button_to_register);
        btnLogin = findViewById(R.id.button_to_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.loginStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                System.out.println("onChanged: "+response);
                Result(response);
            }
        });
    }

    public void openRegister(){
        Intent intent= new Intent(this, Register.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }

    private void login(){

        String user = username.getText().toString();
        String pass = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            username.setError("Invalid Email Format");
            focusView = username;
            cancel = true;
        }

        if (TextUtils.isEmpty(user) || user == "") {
            username.setError("This field is required!");
            focusView = username;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(pass) || !isPasswordValid(pass)) {
            password.setError("This password is to short!");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialogShowing();
            loginViewModel.doLogin(user, pass, this);
        }
    }

    public void Result(String Response){
        try {
            String Status = "";
            String Message = "";
            String Code = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
                Code = jso.getString("code");
            }

            if (Code.equals("0")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.up_to_bottom_out);
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