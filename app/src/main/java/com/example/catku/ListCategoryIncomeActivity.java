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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.catku.adapter.CategoryRecyclers;
import com.example.catku.object.Category;
import com.example.catku.viewmodel.CategoryViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryIncomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout linNoCategory;
    private ProgressDialog progressDialog = null;
    CategoryViewModel categoryViewModel;
    CategoryRecyclers categoryRecyclers;
    List<Category> categoryList = new ArrayList<Category>();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category_income);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("List Category Income");

        linNoCategory = findViewById(R.id.lin_category_not_found);
        recyclerView = findViewById(R.id.recycler_view);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.selectCategory.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultCategory(s);
            }
        });

        categoryViewModel.deleteCategory.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryRecyclers = new CategoryRecyclers(this, categoryList) {
            @Override
            public void onEditItem(Category itemTrip) {
                Intent intent = new Intent(ListCategoryIncomeActivity.this, AddCategoryActivity.class);
                Log.d("CekDataList", itemTrip.getId_category()+" "+itemTrip.getName()+" "+itemTrip.getType());
                String id = String.valueOf(itemTrip.getId_category());
                intent.putExtra("id_category", id);
                intent.putExtra("name", itemTrip.getName());
                intent.putExtra("type", itemTrip.getType());
                intent.putExtra("Edit", "true");
                startActivity(intent);
                finish();
            }

            @Override
            public void onDeleteItem(Category itemTrip, int i) {
                dialogDelete(itemTrip,i);
            }
        };
        recyclerView.setAdapter(categoryRecyclers);

        getCategory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_add_category,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_category){
            Intent intent = new Intent(this, AddCategoryActivity.class);
            intent.putExtra("type", "income");
            intent.putExtra("Edit", "false");
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void dialogDelete(final Category itemTrip, final int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure want to delete this category?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                progressDialogShowing();
                position = i;
                categoryViewModel.deleteCategory(ListCategoryIncomeActivity.this,itemTrip.getId_category());
                if (categoryList.size() == 0){
                    linNoCategory.setVisibility(View.VISIBLE);
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


    public void getCategory(){
        progressDialogShowing();
        categoryViewModel.getCategory("income",this);
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

                    int id_category;
                    String name;
                    String type;

                    id_category = jso.getInt("id_category");
                    name = jso.getString("name");
                    type = jso.getString("type");


                    Category category = new Category(id_category, name, type);
                    categoryList.add(category);
                    categoryRecyclers.notifyDataSetChanged();
                }
                linNoCategory.setVisibility(View.GONE);
            }else {
                progressDialogDismiss();
                linNoCategory.setVisibility(View.VISIBLE);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ListCategoryIncomeActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Status);
                builder.setMessage(Message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        categoryList.remove(position);
                        categoryRecyclers.notifyDataSetChanged();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ListCategoryIncomeActivity.this);
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
        Intent intent = new Intent(ListCategoryIncomeActivity.this, MainActivity.class);
        intent.putExtra("index","3");
        startActivity(intent);
        finish();
    }
}