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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.catku.object.Category;
import com.example.catku.viewmodel.CategoryViewModel;
import com.example.catku.viewmodel.ScheduleViewModel;
import com.example.catku.viewmodel.TransactionViewModel;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {
    private Spinner spinType;
    private Spinner spinSetEvery;
    private LinearLayout linDateTime;
    private EditText etDate;
    private EditText etTime;
    private EditText etAmount;
    private EditText etDescription;
    private CardView btnSaveSchedule;
    private Spinner spinCategory;
    public String Edit = "false";
    public String id_schedule = "";
    public String set_every = "";
    public String id_category = "";
    public String time = "";
    public String date = "";
    public String description = "";
    public String amount = "";
    public String type = "";
    String title = "";
    List<String> setEvery;
    CategoryViewModel categoryViewModel;
    ScheduleViewModel scheduleViewModel;
    List<String> category;
    List<Integer> categoryID;
    private List<String> typeList;
    private ProgressDialog progressDialog = null;
    SimpleDateFormat dateFormatter;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Edit = getIntent().getStringExtra("Edit");
        if (Edit.equals("true")){
            title = "Edit Schedule";
            id_schedule = getIntent().getStringExtra("id_schedule");
            set_every = getIntent().getStringExtra("set_every");
            id_category = getIntent().getStringExtra("id_category");
            time = getIntent().getStringExtra("time");
            date = getIntent().getStringExtra("date");
            description = getIntent().getStringExtra("description");
            amount = getIntent().getStringExtra("amount");
            type = getIntent().getStringExtra("type");
        }else {
            title = "Add Schedule";
        }
        getSupportActionBar().setTitle(title);
        setEvery = new ArrayList<>();
        categoryID = new ArrayList<>();
        category = new ArrayList<>();
        typeList = new ArrayList<>();
        typeList.add("income");
        typeList.add("expense");
        setEvery.add("Select");
        setEvery.add("Date & Time");

        spinSetEvery = findViewById(R.id.spin_set_every);
        linDateTime = findViewById(R.id.lin_date_time_schedule);
        etDate = findViewById(R.id.et_date_schedule);
        etTime = findViewById(R.id.et_time_schedule);
        etAmount = findViewById(R.id.et_amount_schedule);
        etDescription = findViewById(R.id.et_desc_schedule);
        spinCategory = findViewById(R.id.spin_category_schedule);
        btnSaveSchedule = findViewById(R.id.card_btn_save_schedule);
        spinType = findViewById(R.id.spin_type);

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, setEvery);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinSetEvery.setAdapter(spinnerArrayAdapter);

        spinSetEvery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    linDateTime.setVisibility(View.VISIBLE);
                }else {
                    linDateTime.setVisibility(View.GONE);
                }
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, typeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinType.setAdapter(arrayAdapter);
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category.clear();
                categoryID.clear();
                categoryViewModel.getCategory(spinType.getSelectedItem().toString(),AddScheduleActivity.this);
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
        scheduleViewModel.add.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });
        scheduleViewModel.update.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edit.equals("true")){
                    updateSchedule(Integer.parseInt(id_schedule));
                }else {
                    saveSchedule();
                }
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
                mDatePicker = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                timePickerDialog = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        DateFormat.is24HourFormat(AddScheduleActivity.this));
                timePickerDialog.show();
            }
        });
    }

    public void saveSchedule(){
        String typeCategory = spinType.getSelectedItem().toString();
        String setEvery = spinSetEvery.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String amount = etAmount.getText().toString();
        String desc = etDescription.getText().toString();
        int idCategory = 0;
        try {
            idCategory = categoryID.get(spinCategory.getSelectedItemPosition());
        }catch (Exception e){ }
        int amoun = Integer.parseInt(amount);
        if (spinSetEvery.getSelectedItemPosition() > 0){
            if (!date.isEmpty()){
                if (!time.isEmpty()){
                    if (!amount.isEmpty()){
                        if (!desc.isEmpty()){
                            if (category.size() > 0){
                                progressDialogShowing();
                                scheduleViewModel.add(setEvery,idCategory,time,date,desc,amoun,typeCategory,this);
                            }else {
                             dialog("Warning","Please Create Category First");
                            }
                        }else {
                            etDescription.setError("Can't empty");
                        }
                    }else {
                        etAmount.setError("Can't empty");
                    }
                }else {
                    etTime.setError("Can't empty");
                }
            }else {
                etDate.setError("Can't empty");
            }
        }else {
            dialog("Warning", "Please Select Set Every");
        }
    }

    public void updateSchedule(int id_schedule){
        String typeCategory = spinType.getSelectedItem().toString();
        String setEvery = spinSetEvery.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String amount = etAmount.getText().toString();
        String desc = etDescription.getText().toString();
        int idCategory = 0;
        try {
            idCategory = categoryID.get(spinCategory.getSelectedItemPosition());
        }catch (Exception e){ }
        int amoun = Integer.parseInt(amount);
        if (spinSetEvery.getSelectedItemPosition() > 0){
            if (!date.isEmpty()){
                if (!time.isEmpty()){
                    if (!amount.isEmpty()){
                        if (!desc.isEmpty()){
                            if (category.size() > 0){
                                progressDialogShowing();
                                scheduleViewModel.updateCategory(id_schedule,setEvery,idCategory,time,date,desc,amoun,typeCategory,this);
                            }else {
                                dialog("Warning", "Please Select Category");
                            }
                        }else {
                            etDescription.setError("Can't empty");
                        }
                    }else {
                        etAmount.setError("Can't empty");
                    }
                }else {
                    etTime.setError("Can't empty");
                }
            }else {
                etDate.setError("Can't empty");
            }
        }else {
            dialog("Warning", "Please Select Set Every");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(AddScheduleActivity.this, ListScheduleActivity.class);
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
                    category.add(name);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, category);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinCategory.setAdapter(spinnerArrayAdapter);
            }

            if (Edit.equals("true")){
                spinType.setSelection(typeList.indexOf(type));
                spinSetEvery.setSelection(setEvery.indexOf(set_every));
                if (set_every.equals("Date & Time")){
                    linDateTime.setVisibility(View.VISIBLE);
                    etTime.setText(time);
                    etDate.setText(date);
                }
                etAmount.setText(amount);
                etDescription.setText(description);
                Log.d("CekIndex",String.valueOf(categoryID.indexOf(Integer.parseInt(id_category))));
                spinCategory.setSelection(categoryID.indexOf(Integer.parseInt(id_category)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
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
                    intent = new Intent(AddScheduleActivity.this, ListScheduleActivity.class);
                }else {
                    intent = new Intent(AddScheduleActivity.this, ListScheduleActivity.class);
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