package com.example.catku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.catku.viewmodel.CategoryViewModel;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCategoryActivity extends AppCompatActivity {
    private EditText etCategoryName;
    private CardView btnCardSave;
    private Spinner spinType;
    private CategoryViewModel categoryViewModel;
    private ProgressDialog progressDialog = null;
    private String idCategory = "";
    public String Edit = "false";
    public String name = "";
    public String type = "";
    private List<String> typeList;
    String title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Edit = getIntent().getStringExtra("Edit");
        type = getIntent().getStringExtra("type");
        if (Edit.equals("true")){
            title = "Edit Category";
        }else {
            title = "Add Category";
        }
        getSupportActionBar().setTitle(title);
        typeList = new ArrayList<>();
        typeList.add("income");
        typeList.add("expense");

        etCategoryName = findViewById(R.id.et_category_name);
        btnCardSave = findViewById(R.id.btn_card_save_category);
        spinType = findViewById(R.id.spin_type);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, typeList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinType.setAdapter(spinnerArrayAdapter);

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinType.setSelection(typeList.indexOf(type));
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.addCategory.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });
        categoryViewModel.updateCategory.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Result(response);
            }
        });

        btnCardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edit.equals("true")){
                    editCategory();
                }else {
                    addCategory();
                }
            }
        });

        if (Edit.equals("true")){
            idCategory = getIntent().getStringExtra("id_category");
            name = getIntent().getStringExtra("name");
            title = "Edit Category";
            etCategoryName.setText(name);
        }

    }

    private void addCategory(){
        String name = etCategoryName.getText().toString();
        String typeCategory = spinType.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        Pattern p = Pattern.compile("^[a-zA-Z' ]*$");
        Matcher m1;
        m1 = p.matcher(name);

        if(!m1.find()) {
            etCategoryName.setError("This field is required!");
            focusView = etCategoryName;
            cancel = true;
        }
        if (TextUtils.isEmpty(name) || name == "") {
            etCategoryName.setError("This field is required!");
            focusView = etCategoryName;
            cancel = true;
        }

            if (cancel) {
                focusView.requestFocus();
            } else {
                progressDialogShowing();
                categoryViewModel.addCategory(name,typeCategory,AddCategoryActivity.this);
            }
    }

    private void editCategory(){
        String name = etCategoryName.getText().toString();
        String typeCategory = spinType.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name) || name == "") {
            etCategoryName.setError("This field is required!");
            focusView = etCategoryName;
            cancel = true;
        }
            if (cancel) {
                focusView.requestFocus();
            } else {
                progressDialogShowing();
                categoryViewModel.updateCategory(Integer.parseInt(idCategory),name,typeCategory,AddCategoryActivity.this);
            }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCategoryActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent;
                        if (type.equals("income")){
                            intent = new Intent(AddCategoryActivity.this, ListCategoryIncomeActivity.class);
                        }else {
                            intent = new Intent(AddCategoryActivity.this, ListCategoryExpenseActivity.class);
                        }
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

    public void dialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCategoryActivity.this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
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

    public void dialogExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent;
                if (type.equals("income")){
                    intent = new Intent(AddCategoryActivity.this, ListCategoryIncomeActivity.class);
                }else {
                    intent = new Intent(AddCategoryActivity.this, ListCategoryExpenseActivity.class);
                }
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