package com.example.catku;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.catku.viewmodel.CategoryViewModel;
import com.example.catku.viewmodel.RegisterViewModel;
import com.example.catku.viewmodel.TransactionViewModel;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {
    private Spinner spinType;
    private EditText etDate;
    private EditText etTime;
    private EditText etAmount;
    private EditText etMemo;
    private EditText etDescription;
    private CardView btnSaveSchedule;
    private CardView cardImage;
    private ImageView imageView;
    private ImageView btnImage;
    private Spinner spinCategory;
    private List<String> typeList;
    List<String> categoryList;
    List<Integer> categoryID;
    CategoryViewModel categoryViewModel;
    TransactionViewModel transactionViewModel;
    RegisterViewModel registerViewModel;
    private ProgressDialog progressDialog = null;
    SimpleDateFormat dateFormatter;
    private TimePickerDialog timePickerDialog;

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
    String title = "";
    String PATHIMAGES = "";
    String typeCategory = "";
    String Status = "";
    String Message = "";
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Edit = getIntent().getStringExtra("Edit");
        id_user = getIntent().getStringExtra("id_user");
        balance = getIntent().getStringExtra("balance");
        if (Edit.equals("true")){
            title = "Edit Transaction";
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
        }else {
            title = "Add Transaction";
        }
        getSupportActionBar().setTitle(title);

        categoryID = new ArrayList<>();
        categoryList = new ArrayList<>();
        typeList = new ArrayList<>();
        typeList.add("income");
        typeList.add("expense");

        etDate = findViewById(R.id.et_date_transaction);
        etTime = findViewById(R.id.et_time_transaction);
        etAmount = findViewById(R.id.et_amount_transaction);
        etDescription = findViewById(R.id.et_desc_transaction);
        etMemo = findViewById(R.id.et_memo_transaction);
        spinCategory = findViewById(R.id.spin_category_transaction);
        btnSaveSchedule = findViewById(R.id.card_btn_save_transaction);
        cardImage = findViewById(R.id.card_img);
        btnImage = findViewById(R.id.btn_img_transaction);
        imageView = findViewById(R.id.img_transaction);
        spinType = findViewById(R.id.spin_type);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, typeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinType.setAdapter(arrayAdapter);

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryList.clear();
                categoryID.clear();
                categoryViewModel.getCategory(spinType.getSelectedItem().toString(),AddTransactionActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        categoryViewModel.selectCategory.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultCategory(s);
            }
        });

        transactionViewModel.add.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });
        transactionViewModel.update.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultUpdate(s);
            }
        });

        registerViewModel.updateUserBalance.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultBalance(s);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDate.setText(dateFormatter.format(newDate.getTime()));

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(AddTransactionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String min = String.valueOf(minute);
                        String jam = String.valueOf(hourOfDay);
                        if (min.length() == 1) min = "0"+min;
                        if (jam.length() == 1) jam = "0"+jam;
                        etTime.setText(jam+":"+min);
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(AddTransactionActivity.this));
                timePickerDialog.show();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddTransactionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(AddTransactionActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(AddTransactionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2);
                }else {
                    CropImage.startPickImageActivity(AddTransactionActivity.this);
                }
            }
        });

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edit.equals("true")){
                    editTransaction();
                }else {
                    saveTransaction();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddTransactionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(AddTransactionActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(AddTransactionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2);
                }else {
                    CropImage.startPickImageActivity(AddTransactionActivity.this);
                }
            }
        });

    }

    public void saveTransaction(){
        typeCategory = spinType.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String amount = etAmount.getText().toString();
        String desc = etDescription.getText().toString();
        String memo = etMemo.getText().toString();
        int idCategory = 0;
        try {
            idCategory = categoryID.get(spinCategory.getSelectedItemPosition());
        }catch (Exception e){ }
        int amoun = Integer.parseInt(amount);
        int idUser = Integer.parseInt(id_user);
            if (!date.isEmpty()){
                if (!time.isEmpty()){
                    if (!amount.isEmpty()){
                        if (!desc.isEmpty()){
                                if (categoryList.size() > 0){
                                    progressDialogShowing();
                                    transactionViewModel.add(idUser,idCategory,typeCategory,PATHIMAGES,amoun,desc,memo,date,time,this);
                                }else {
                                    dialog("Warning", "Please Create Category First");
                                }
                        }else {
                            etDescription.setError("Can't empty");
                            etDescription.setFocusable(true);
                        }
                    }else {
                        etAmount.setError("Can't empty");
                        etAmount.setFocusable(true);
                    }
                }else {
                    etTime.setError("Can't empty");
                    etTime.setFocusable(true);
                }
            }else {
                etDate.setError("Can't empty");
                etDate.setFocusable(true);
            }
    }

    public void editTransaction(){
        typeCategory = spinType.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String amountText = etAmount.getText().toString();
        String desc = etDescription.getText().toString();
        String memo = etMemo.getText().toString();
        int idCategory = 0;
        try {
            idCategory = categoryID.get(spinCategory.getSelectedItemPosition());
        }catch (Exception e){ }
        int amoun = Integer.parseInt(amountText);
        int idUser = Integer.parseInt(id_user);
        int idTransaction = Integer.parseInt(id_transaction);
        if (PATHIMAGES.isEmpty()){
            PATHIMAGES = image;
        }
            if (!date.isEmpty()){
                if (!time.isEmpty()){
                    if (!amount.isEmpty()){
                        if (!desc.isEmpty()){
                                if (categoryList.size() > 0){
                                    progressDialogShowing();
                                    transactionViewModel.update(idTransaction,idUser,idCategory,typeCategory,PATHIMAGES,amoun,desc,memo,date,time,this);
                                }else {
                                    dialog("Warning", "Please Select Category");
                                }
                        }else {
                            etDescription.setError("Can't empty");
                            etDescription.setFocusable(true);
                        }
                    }else {
                        etAmount.setError("Can't empty");
                        etAmount.setFocusable(true);
                    }
                }else {
                    etTime.setError("Can't empty");
                    etTime.setFocusable(true);
                }
            }else {
                etDate.setError("Can't empty");
                etDate.setFocusable(true);
            }
    }

    public void Result(String Response){
        try {
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                int userID = Integer.parseInt(id_user);
                int bal = 0;
                if (typeCategory.equals("income")){
                    bal = Integer.parseInt(balance) + Integer.parseInt(etAmount.getText().toString());
                }else {
                    bal = Integer.parseInt(balance) - Integer.parseInt(etAmount.getText().toString());
                }
                registerViewModel.updateUserBalance(userID,"","","",bal,this);
                Log.d("CekRespon", Response);
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

    public void ResultUpdate(String Response){
        Log.d("CekResponBalance", Response);
        try {
            JSONArray jsa = new JSONArray(Response);
            for (int i = 0; i < jsa.length(); i++){
                JSONObject jso = jsa.getJSONObject(i);
                Status = jso.getString("status");
                Message = jso.getString("message");
            }
            if (Status.equals("Success")){
                int userID = Integer.parseInt(id_user);
                int amoun = Integer.parseInt(etAmount.getText().toString());
                int am = Integer.parseInt(amount);
                int total = 0;
                int bal = 0;
                if (typeCategory.equals("income")){
                    if (amoun > am){
                        total = amoun - am;
                        bal = Integer.parseInt(balance) + total;
                    }else {
                        total = am - amoun;
                        bal = Integer.parseInt(balance) - total;
                    }
                }else {
                    if (amoun > am){
                        total = amoun - am;
                        bal = Integer.parseInt(balance) - total;
                    }else {
                        total = am - amoun;
                        bal = Integer.parseInt(balance) + total;
                    }
                }
                registerViewModel.updateUserBalance(userID,"","","",bal,this);
                Log.d("CekRespon", Response);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTransactionActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
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

    public void ResultCategory(String Response){
        Log.d("CekResponCategory", Response);
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);

                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    int id_category;
                    String name;

                    id_category = jso.getInt("id_category");
                    name = jso.getString("name");
                    Log.d("CekID",jso.getString("id_category"));
                    categoryID.add(id_category);
                    categoryList.add(name);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddTransactionActivity.this,   android.R.layout.simple_spinner_item, categoryList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinCategory.setAdapter(spinnerArrayAdapter);
            }else {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddTransactionActivity.this,   android.R.layout.simple_spinner_item, categoryList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinCategory.setAdapter(spinnerArrayAdapter);
            }

            if (Edit.equals("true")){
                spinType.setSelection(typeList.indexOf(type));
                cardImage.setVisibility(View.VISIBLE);
                etTime.setText(time);
                etDate.setText(date);
                etAmount.setText(amount);
//                etAmount.setEnabled(false);
                etDescription.setText(description);
                etMemo.setText(memo);
                imageView.setImageURI(Uri.parse(image));
                Log.d("CekIdCategory", id_category);
                Log.d("CekIndex",String.valueOf(categoryID.indexOf(Integer.parseInt(id_category))));
                spinCategory.setSelection(categoryID.indexOf(Integer.parseInt(id_category)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTransactionActivity.this);
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
                Intent intent = new Intent(AddTransactionActivity.this, MainActivity.class);
                intent.putExtra("index","0");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            try{
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("result", result.getUri().toString());
                if (resultCode == RESULT_OK) {
                    cardImage.setVisibility(View.VISIBLE);
                    imageView.setImageURI(result.getUri());
                    PATHIMAGES = result.getUri().getPath();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Cropping failed: " + result.toString(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Log.d("ErrorCropImage", e.toString());
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

        }else if (requestCode == 2){
            CropImage.startPickImageActivity(this);
        }else{
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(16,9)
                .setMaxCropResultSize(3072,2304)
                .start(this);
    }

}