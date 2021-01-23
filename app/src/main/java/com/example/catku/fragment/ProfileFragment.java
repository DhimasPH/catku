package com.example.catku.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catku.EditProfileActivity;
import com.example.catku.ListCategoryExpenseActivity;
import com.example.catku.ListCategoryIncomeActivity;
import com.example.catku.ListScheduleActivity;
import com.example.catku.Login;
import com.example.catku.R;
import com.example.catku.viewmodel.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    View rootView;
    private LinearLayout linSchedule;
    private LinearLayout linExpense;
    private LinearLayout linIncome;
    private LinearLayout linLogout;
    private TextView tvName;
    private TextView tvUsername;
    private ImageView imgBtnProfile;
    private ProgressDialog progressDialog = null;
    private LoginViewModel loginViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        linSchedule = rootView.findViewById(R.id.btn_lin_schedule);
        linExpense = rootView.findViewById(R.id.btn_lin_expense);
        linIncome = rootView.findViewById(R.id.btn_lin_income);
        linLogout = rootView.findViewById(R.id.btn_lin_logout);
        tvName = rootView.findViewById(R.id.tv_name);
        tvUsername = rootView.findViewById(R.id.tv_username);
        imgBtnProfile = rootView.findViewById(R.id.btn_img_edit_profile);

        imgBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        linExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListCategoryExpenseActivity.class);
                intent.putExtra("type", "expense");
                intent.putExtra("Edit", "false");
                startActivity(intent);
                getActivity().finish();
            }
        });

        linIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListCategoryIncomeActivity.class);
                intent.putExtra("type", "income");
                intent.putExtra("Edit", "false");
                startActivity(intent);
                getActivity().finish();
            }
        });

        linSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListScheduleActivity.class);
                intent.putExtra("Edit", "false");
                startActivity(intent);
                getActivity().finish();
            }
        });

        linLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLogout();
            }
        });
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
                resultLogout(response);
            }
        });
        loginViewModel.checkSession(getActivity());
        return rootView;
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
                String username;
                String name;
                int id_user;
                for (int i = 0; i < jsa.length(); i++){
                    JSONObject jso = jsa.getJSONObject(i);
                    id_user = jso.getInt("id_user");
                    username = jso.getString("username");
                    name = jso.getString("name");
                    loginViewModel.getUser(id_user,getActivity());
                }
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
                    tvName.setText(name);
                    tvUsername.setText(username);
                }
            }else {
                loginViewModel.logOut(getActivity());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void logOut(){
        progressDialogShowing();
        loginViewModel.logOut(getActivity());
    }

    public void resultLogout(String Response){
        try {
            String status = "";
            String message = "";
            JSONArray jresArray = new JSONArray(Response);
            JSONObject jsoRes = jresArray.getJSONObject(0);
            status = jsoRes.getString("status");
            message = jsoRes.getString("message");

            if (status.equals("Success")){
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle(status);
                builder.setMessage(message);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.show();
            }else {
                progressDialogDismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle(status);
                builder.setMessage(message);
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

    public void dialogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Exit");
        builder.setMessage("Are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                logOut();
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
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
    }

    public void progressDialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}