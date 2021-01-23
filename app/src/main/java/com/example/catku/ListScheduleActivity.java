package com.example.catku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.catku.adapter.CategoryRecyclers;
import com.example.catku.adapter.ScheduleRecyclers;
import com.example.catku.object.Category;
import com.example.catku.object.Schedule;
import com.example.catku.viewmodel.CategoryViewModel;
import com.example.catku.viewmodel.ScheduleViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListScheduleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout linNoSchedule;
    private ProgressDialog progressDialog = null;
    ScheduleRecyclers scheduleRecyclers;
    ScheduleViewModel scheduleViewModel;
    List<Schedule> scheduleList = new ArrayList<Schedule>();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("List Schedule");

        linNoSchedule = findViewById(R.id.lin_schedule_not_found);
        recyclerView = findViewById(R.id.recycler_schedule);

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        scheduleViewModel.select.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultCategory(s);
            }
        });

        scheduleViewModel.delete.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scheduleRecyclers = new ScheduleRecyclers(this, scheduleList) {
            @Override
            public void onEditItem(Schedule itemTrip) {
                Intent intent = new Intent(ListScheduleActivity.this, AddScheduleActivity.class);
                String idSchedule = String.valueOf(itemTrip.getId_schedule());
                String idCategory = String.valueOf(itemTrip.getId_category());
                String amount = String.valueOf(itemTrip.getAmount());
                intent.putExtra("id_schedule", idSchedule);
                intent.putExtra("set_every", itemTrip.getSet_every());
                intent.putExtra("id_category", idCategory);
                intent.putExtra("time", itemTrip.getTime());
                intent.putExtra("date", itemTrip.getDate());
                intent.putExtra("description", itemTrip.getDescription());
                intent.putExtra("amount", amount);
                intent.putExtra("type", itemTrip.getType());
                intent.putExtra("Edit", "true");
                startActivity(intent);
                finish();
            }

            @Override
            public void onDeleteItem(Schedule itemTrip, int pos) {
                dialogDelete(itemTrip,pos);
            }
        };
        recyclerView.setAdapter(scheduleRecyclers);
        getSchedule();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_add_schedule,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_schedule){
            Intent intent = new Intent(this, AddScheduleActivity.class);
            intent.putExtra("Edit", "false");
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    public void getSchedule(){
        progressDialogShowing();
        scheduleViewModel.getData(this);
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
                progressDialogDismiss();
                JSONArray jsa = new JSONArray(message);

                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    int id_schedule;
                    int id_category;
                    int amount;
                    String set_every;
                    String time;
                    String date;
                    String description;
                    String type;

                    id_schedule = jso.getInt("id_schedule");
                    id_category = jso.getInt("id_category");
                    amount = jso.getInt("amount");
                    set_every = jso.getString("set_every");
                    time = jso.getString("time");
                    date = jso.getString("date");
                    description = jso.getString("description");
                    type = jso.getString("type");


                    Schedule schedule = new Schedule(id_schedule,set_every,id_category, time,date,description,amount, type);
                    scheduleList.add(schedule);
                    scheduleRecyclers.notifyDataSetChanged();
                }
                linNoSchedule.setVisibility(View.GONE);
            }else {
                progressDialogDismiss();
                linNoSchedule.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ListScheduleActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        scheduleList.remove(position);
                        scheduleRecyclers.notifyDataSetChanged();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListScheduleActivity.this);
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

    public void dialogDelete(final Schedule itemTrip, final int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure want to delete this schedule?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                progressDialogShowing();
                position = i;
                scheduleViewModel.deleteData(ListScheduleActivity.this,itemTrip.getId_schedule());
                if (scheduleList.size() == 0){
                    linNoSchedule.setVisibility(View.VISIBLE);
                }

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
        Intent intent = new Intent(ListScheduleActivity.this, MainActivity.class);
        intent.putExtra("index","3");
        startActivity(intent);
        finish();
    }
}