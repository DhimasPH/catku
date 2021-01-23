package com.example.catku.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catku.AddTransactionActivity;
import com.example.catku.Login;
import com.example.catku.MainActivity;
import com.example.catku.R;
import com.example.catku.viewmodel.LoginViewModel;
import com.example.catku.viewmodel.ReportViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    View rootView;
    private EditText etDateFilter;
    private TextView tvBalanceReport;
    private TextView tvIncome;
    private TextView tvExpense;
    private TextView tvTotal;
    private TextView tvBtnClear;
    private ImageView imgBtnDate;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    ReportViewModel reportViewModel;
    LoginViewModel loginViewModel;
    int userID;

    private PieChart pieChart;
    private PieChart pieChartIncome;
    PieData pieData;
    PieData pieDataIncome;
    PieDataSet pieDataSet;
    PieDataSet pieDataSetIncome;
    ArrayList<PieEntry> pieEntries;
    ArrayList<PieEntry> pieEntriesIncome;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        etDateFilter = rootView.findViewById(R.id.et_date_filter);
        tvBalanceReport = rootView.findViewById(R.id.tv_balance_report);
        tvIncome = rootView.findViewById(R.id.tv_total_income_report);
        tvExpense = rootView.findViewById(R.id.tv_total_expense_report);
        tvTotal = rootView.findViewById(R.id.tv_grand_total_report);
        tvBtnClear = rootView.findViewById(R.id.tv_btn_clear);
        imgBtnDate = rootView.findViewById(R.id.btn_img_calendar);
        pieChart = rootView.findViewById(R.id.pieChartExpense);
        pieChartIncome = rootView.findViewById(R.id.pieChartIncome);
        pieEntries = new ArrayList<>();
        pieEntriesIncome = new ArrayList<>();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.sessionStatus1.observe(this, new Observer<String>() {
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
        reportViewModel.income.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultIncome(s);
            }
        });
        reportViewModel.expense.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ResultExpense(s);
            }
        });
        imgBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDateFilter.setText(dateFormatter.format(newDate.getTime()));
                        reportViewModel.getTotalReport(userID,etDateFilter.getText().toString(),getActivity());
                        reportViewModel.getIncomeReport(userID,etDateFilter.getText().toString(),getActivity());
                        reportViewModel.getExpenseReport(userID,etDateFilter.getText().toString(),getActivity());

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });
        etDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDateFilter.setText(dateFormatter.format(newDate.getTime()));
                        reportViewModel.getTotalReport(userID,etDateFilter.getText().toString(),getActivity());
                        reportViewModel.getIncomeReport(userID,etDateFilter.getText().toString(),getActivity());
                        reportViewModel.getExpenseReport(userID,etDateFilter.getText().toString(),getActivity());

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

        tvBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDateFilter.setText("");
                reportViewModel.getTotalReport(userID,etDateFilter.getText().toString(),getActivity());
                reportViewModel.getIncomeReport(userID,etDateFilter.getText().toString(),getActivity());
                reportViewModel.getExpenseReport(userID,etDateFilter.getText().toString(),getActivity());
            }
        });

        loginViewModel.checkSession1(getActivity());
        return rootView;
    }

    public void getSession(String Response){
        Log.d("CekResponeSession",Response);
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
                    userID = jso.getInt("id_user");
                }
                reportViewModel.getTotalReport(userID,etDateFilter.getText().toString(),getActivity());
                reportViewModel.getIncomeReport(userID,etDateFilter.getText().toString(),getActivity());
                reportViewModel.getExpenseReport(userID,etDateFilter.getText().toString(),getActivity());
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

                tvBalanceReport.setText("Balance : "+FormatNumber(Balance));
                tvIncome.setText(FormatNumber(total_income));
                tvExpense.setText(FormatNumber(total_expense));
                tvTotal.setText(FormatNumber(Total));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ResultIncome(String Response){
        Log.d("CekResponseIncome", Response);
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            pieEntriesIncome.clear();
            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                int id_category = 0;
                int percentage = 0;
                String nama_category;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    id_category = jso.getInt("id_category");
                    nama_category = jso.getString("name_category");
                    percentage = jso.getInt("percentage");
                    Log.d("CekIncome",nama_category);
                    Log.d("CekPersentase", String.valueOf(percentage));
                    pieEntriesIncome.add(new PieEntry(percentage, nama_category));
                }
                pieDataSetIncome = new PieDataSet(pieEntriesIncome, "");
                pieDataIncome = new PieData(pieDataSetIncome);
                pieChartIncome.setData(pieDataIncome);
                pieDataSetIncome.setColors(ColorTemplate.JOYFUL_COLORS);
                pieDataSetIncome.setSliceSpace(2f);
                pieDataSetIncome.setValueTextColor(Color.BLACK);
                pieDataSetIncome.setValueTextSize(10f);
                pieDataSetIncome.setSliceSpace(5f);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ResultExpense(String Response){
        Log.d("CekResponExpense", Response);
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");
            pieEntries.clear();

            if (status.equals("Success")){
                JSONArray jsa = new JSONArray(message);
                int id_category = 0;
                int percentage = 0;
                String nama_category;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    id_category = jso.getInt("id_category");
                    nama_category = jso.getString("name_category");
                    percentage = jso.getInt("percentage");
                    Log.d("CekExpense",nama_category);
                    pieEntries.add(new PieEntry(percentage, nama_category));
                }
                pieDataSet = new PieDataSet(pieEntries, "");
                pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                pieDataSet.setSliceSpace(2f);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(10f);
                pieDataSet.setSliceSpace(5f);
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
        return "Rp. "+totalAmount;
    }
}