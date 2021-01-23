package com.example.catku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.catku.object.Category;
import com.example.catku.viewmodel.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {
    private Button button;
    private Button button2;
    LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        button = findViewById(R.id.button_to_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        button2 = findViewById(R.id.button_to_register);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.sessionStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                checkLogin(response);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginViewModel.checkSession(SplashScreenActivity.this);
            }
        }, 2000);
    }

    public void openLogin(){
        Intent intent= new Intent(this, Login.class);
        startActivity(intent);
        this.finish();
    }

    public void openRegister(){
        Intent intent= new Intent(this, Register.class);
        startActivity(intent);
        this.finish();
    }

    public void checkLogin(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");


            if (status.equals("Success")){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}