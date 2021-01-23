package com.example.catku.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catku.AddScheduleActivity;
import com.example.catku.AddTransactionActivity;
import com.example.catku.ListScheduleActivity;
import com.example.catku.Login;
import com.example.catku.MainActivity;
import com.example.catku.R;
import com.example.catku.SplashScreenActivity;
import com.example.catku.TransactionDetailActivity;
import com.example.catku.adapter.TransactionRecyclers;
import com.example.catku.object.Schedule;
import com.example.catku.object.Transaction;
import com.example.catku.viewmodel.LoginViewModel;
import com.example.catku.viewmodel.ReportViewModel;
import com.example.catku.viewmodel.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    View rootView;
    LoginViewModel loginViewModel;
    TransactionViewModel transactionViewModel;
    ReportViewModel reportViewModel;
    private TextView tvNameHome;
    private TextView tvBalanceHome;
    private LinearLayout linNoDataTranscation;
    private RecyclerView recyclerView;
    private TransactionRecyclers transactionRecyclers;
    private int balanceHome = 0;
    private int userID;
    List<Transaction> transactionList = new ArrayList<Transaction>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        tvNameHome = rootView.findViewById(R.id.tv_name_home);
        tvBalanceHome = rootView.findViewById(R.id.tv_balance);
        recyclerView = rootView.findViewById(R.id.recycler_transaction);
        linNoDataTranscation = rootView.findViewById(R.id.lin_no_data_transaction);

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

        loginViewModel.logOutStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Intent intent= new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        reportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
        reportViewModel.total.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultTotal(s);
            }
        });

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.select.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Result(s);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        transactionRecyclers = new TransactionRecyclers(getActivity(),transactionList) {
            @Override
            public void onClickItem(Transaction itemTrip) {
                Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                String idTransaction = String.valueOf(itemTrip.getId_transaction());
                String idUser = String.valueOf(itemTrip.getId_user());
                String idCategory = String.valueOf(itemTrip.getId_category());
                String amount = String.valueOf(itemTrip.getAmount());
                String balance = String.valueOf(balanceHome);
                intent.putExtra("id_transaction", idTransaction);
                intent.putExtra("id_user", idUser);
                intent.putExtra("id_category", idCategory);
                intent.putExtra("category", itemTrip.getCategory());
                intent.putExtra("type", itemTrip.getType());
                intent.putExtra("image", itemTrip.getImage());
                intent.putExtra("amount", amount);
                intent.putExtra("description", itemTrip.getDescription());
                intent.putExtra("memo", itemTrip.getMemo());
                intent.putExtra("time", itemTrip.getTime());
                intent.putExtra("date", itemTrip.getDate());
                intent.putExtra("balance", balance);
                intent.putExtra("Edit", "true");
                startActivity(intent);
                getActivity().finish();
            }
        };
        recyclerView.setAdapter(transactionRecyclers);

        FloatingActionButton btnAdd = rootView.findViewById(R.id.float_add_transaction);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddTransaction();
            }
        });
        loginViewModel.checkSession(getActivity());
        return rootView;
    }

    public void toAddTransaction(){
        String balance = String.valueOf(balanceHome);
        String idUser = String.valueOf(userID);
        Intent intent= new Intent(getActivity(), AddTransactionActivity.class);
        intent.putExtra("id_user", idUser);
        intent.putExtra("balance", balance);
        intent.putExtra("Edit", "false");
        intent.putExtra("from_schedule", "false");
        startActivity(intent);
        getActivity().finish();
    }

    public void Result(String Response){
        Log.d("CekResponTransaction", Response);
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");


            if (status.equals("Success")){
                transactionList.clear();
                JSONArray jsa = new JSONArray(message);
                int id_transaction;
                int id_user;
                int id_category;
                int amount;
                String category;
                String time;
                String date;
                String description;
                String type;
                String image;
                String memo;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);

                    id_transaction = jso.getInt("id_transaction");
                    id_user = jso.getInt("id_user");
                    id_category = jso.getInt("id_category");
                    category = jso.getString("name");
                    amount = jso.getInt("amount");
                    time = jso.getString("time");
                    date = jso.getString("date");
                    memo = jso.getString("memo");
                    description = jso.getString("description");
                    image = jso.getString("image");
                    type = jso.getString("type");

                    Log.d("CekDataTransaction", jso.toString());
                    Transaction transaction = new Transaction(id_transaction,id_user,id_category,category,type,image,amount,description,memo,date,time);
                    transactionList.add(transaction);
                    Log.d("CekDataIdCategory", String.valueOf(id_category));
                    transactionRecyclers.notifyDataSetChanged();
                }
                linNoDataTranscation.setVisibility(View.GONE);
            }else {
                linNoDataTranscation.setVisibility(View.VISIBLE);
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
                int id_user = 0;
                int balance;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    id_user = jso.getInt("id_user");

                }
                loginViewModel.getUser(id_user,getActivity());
            }else {
                loginViewModel.logOut(getActivity());
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
                    userID = id_user;
                    tvNameHome.setText(name);
                    reportViewModel.getTotalReport(id_user,"",getActivity());
                }
                Log.d("CekGetData","true");
                transactionViewModel.getData(id_user,getActivity());
            }else {
                loginViewModel.logOut(getActivity());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ResultTotal(String Response){
        Log.d("CekResponTotal", Response);
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");


            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                int total_income = 0;
                int total_expense = 0;
                int Total = 0;
                int Balance = 0;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    total_income = jso.getInt("total_income");
                    total_expense = jso.getInt("total_expense");
                }
                Total = total_income - total_expense;
                Balance = Total;
                balanceHome = Balance;
                tvBalanceHome.setText("Balance : Rp. "+FormatNumber(balanceHome));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String FormatNumber(int amount)  {
        String totalAmount="";
        try {
            double dprice = Double.parseDouble(String.valueOf(amount));
            DecimalFormat formatter2 = new DecimalFormat("#,###");
            String AB = String.valueOf(formatter2.format(dprice));
            if (AB.startsWith(".")) AB = "0" + AB;
            totalAmount = AB;
        }catch(Exception e){
            totalAmount = String.valueOf(amount);
        }
        return totalAmount;
    }
}