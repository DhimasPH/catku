package com.example.catku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catku.object.Schedule;
import com.example.catku.viewmodel.RegisterViewModel;
import com.example.catku.viewmodel.ScheduleViewModel;
import com.example.catku.viewmodel.TransactionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class TransactionDetailActivity extends AppCompatActivity {
    private TextView tvDesc;
    private TextView tvCategory;
    private TextView tvAmount;
    private TextView tvDateTime;
    private TextView tvType;
    private TextView tvMemo;
    private ImageView imageView;

    public String Edit = "false";
    public String id_transaction = "";
    public String id_user = "";
    public String id_category = "";
    public String category = "";
    public String time = "";
    public String date = "";
    public String description = "";
    public String memo = "";
    public String image = "";
    public String amount = "";
    public String type = "";
    public String balance = "";
    String Status = "";
    String Message = "";

    TransactionViewModel transactionViewModel;
    RegisterViewModel registerViewModel;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Transaction Detail");
        Edit = getIntent().getStringExtra("Edit");
        id_user = getIntent().getStringExtra("id_user");
        id_transaction = getIntent().getStringExtra("id_transaction");
        id_category = getIntent().getStringExtra("id_category");
        category = getIntent().getStringExtra("category");
        image = getIntent().getStringExtra("image");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");
        memo = getIntent().getStringExtra("memo");
        amount = getIntent().getStringExtra("amount");
        type = getIntent().getStringExtra("type");
        balance = getIntent().getStringExtra("balance");
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        tvDesc = findViewById(R.id.tv_desc_detail);
        tvCategory = findViewById(R.id.tv_category_detail);
        tvAmount = findViewById(R.id.tv_amount_detail);
        tvDateTime = findViewById(R.id.tv_date_time_detail);
        tvType = findViewById(R.id.tv_type_detail);
        tvMemo = findViewById(R.id.tv_memo_detail);
        imageView = findViewById(R.id.img_detail);

        tvDesc.setText(description);
        tvCategory.setText(category);

        String totalAmount="";
        try {
            double dprice = Double.parseDouble(amount);
            DecimalFormat formatter2 = new DecimalFormat("#,###");
            String AB = String.valueOf(formatter2.format(dprice));
            if (AB.startsWith(".")) AB = "0" + AB;
            totalAmount = AB;
        }catch(Exception e){
            totalAmount = String.valueOf(amount);
        }
        if (type.equals("income")){
            tvAmount.setText("+Rp "+totalAmount);
            tvAmount.setTextColor(this.getResources().getColor(R.color.blue));
        }else {
            tvAmount.setText("-Rp "+totalAmount);
            tvAmount.setTextColor(this.getResources().getColor(R.color.darkred));
        }
        tvDateTime.setText(date +" "+time);
        tvType.setText(type);
        tvMemo.setText(memo);
        imageView.setImageURI(Uri.parse(image));

        transactionViewModel.delete.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultDelete(s);
            }
        });

        registerViewModel.updateUserBalance.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultBalance(s);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_detail_transaction,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_edit_transaction){
            Intent intent = new Intent(this, AddTransactionActivity.class);
            intent.putExtra("id_transaction", id_transaction);
            intent.putExtra("id_user", id_user);
            intent.putExtra("id_category", id_category);
            intent.putExtra("category", category);
            intent.putExtra("type",type);
            intent.putExtra("image", image);
            intent.putExtra("amount", amount);
            intent.putExtra("description", description);
            intent.putExtra("memo", memo);
            intent.putExtra("time", time);
            intent.putExtra("date", date);
            intent.putExtra("balance", balance);
            intent.putExtra("Edit", "true");
            intent.putExtra("from_schedule", "false");
            startActivity(intent);
            finish();
        }else if (id == R.id.action_delete_transaction){
            int transactionID = Integer.parseInt(id_transaction);
            int userID = Integer.parseInt(id_user);
            dialogDelete(transactionID,userID);
        }

        return super.onOptionsItemSelected(item);
    }

    public void ResultDelete(String Response){
        try {
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                int bal = 0;
                int userID = Integer.parseInt(id_user);
                int amoun = Integer.parseInt(amount);
                if (type.equals("income")){
                    bal = Integer.parseInt(balance) - amoun;
                }else {
                    bal = Integer.parseInt(balance) + amoun;
                }
                registerViewModel.updateUserBalance(userID,"","","",bal,this);
//                progressDialogDismiss();
//                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetailActivity.this);
//                builder.setCancelable(false);
//                builder.setTitle(Status);
//                builder.setMessage(Message);
//                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
//                        intent.putExtra("index","0");
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetailActivity.this);
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

    public void ResultBalance(String Response){
        Log.d("CekResponBalance", Response);
        try {
            String Stat = "";
            String Mess = "";
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Stat = jso.getString("status");
                Mess = jso.getString("message");
            }
            if (Stat.equals("Success")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetailActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
                        intent.putExtra("index","0");
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

    public void dialogDelete(final int id_transaction, final int id_user){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure want to delete this transaction?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                progressDialogShowing();
                transactionViewModel.deleteData(TransactionDetailActivity.this,id_transaction,id_user);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
        intent.putExtra("index","0");
        startActivity(intent);
        finish();
    }
}