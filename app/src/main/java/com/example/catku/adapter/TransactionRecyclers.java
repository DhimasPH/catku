package com.example.catku.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catku.R;
import com.example.catku.object.Schedule;
import com.example.catku.object.Transaction;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class TransactionRecyclers extends RecyclerView.Adapter<TransactionRecyclers.MyViewHolder> {
    List<Transaction> item;
    Context context;
    public TransactionRecyclers(Context context, List<Transaction> item){
        this.context = context;
        this.item = item;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R
                .layout.list_transaction, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Transaction itemTrip = item.get(position);
        Log.d("CekIdTransaction",String.valueOf(itemTrip.getId_transaction()));
        Log.d("CekIdUser",String.valueOf(itemTrip.getId_user()));
        Log.d("CekIdCategory",String.valueOf(itemTrip.getId_category()));

        SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE/dd/MMM/yyyy");
        Date date = null;
        try {
            date = parser.parse(itemTrip.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = formatter.format(date);
        String datFor[] = formattedDate.split("/");
        holder.tvDay.setText(datFor[0]);
        holder.tvDate.setText(datFor[1]);
        holder.tvMonthYear.setText(datFor[2]+" "+datFor[3]);

        String totalAmount="";
        try {
            double dprice = Double.parseDouble(String.valueOf(itemTrip.getAmount()));
            DecimalFormat formatter2 = new DecimalFormat("#,###");
            String AB = String.valueOf(formatter2.format(dprice));
            if (AB.startsWith(".")) AB = "0" + AB;
            totalAmount = AB;
        }catch(Exception e){
            totalAmount = String.valueOf(itemTrip.getAmount());
        }

        Log.d("CekAMount",totalAmount);
        if (itemTrip.getType().equals("income")){
            holder.tvAmount.setText("+Rp. "+totalAmount);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.tvAmount.setText("-Rp. "+totalAmount);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.darkred));
        }
        holder.tvName.setText(itemTrip.getDescription());
        holder.tvCategory.setText(itemTrip.getCategory());
        if (!itemTrip.getImage().isEmpty()){
            holder.imgTransaction.setImageURI(Uri.parse(itemTrip.getImage()));
        }
        holder.tvTime.setText(itemTrip.getTime());

        holder.linParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(itemTrip);
            }
        });

    }

    public abstract void onClickItem(Transaction itemTrip);



    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvDay;
        TextView tvMonthYear;
        TextView tvAmount;
        TextView tvName;
        TextView tvCategory;
        TextView tvTime;
        ImageView imgTransaction;
        LinearLayout linParent;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvMonthYear = itemView.findViewById(R.id.tv_month_year);
            tvAmount = itemView.findViewById(R.id.tv_amount_transaction);
            tvName = itemView.findViewById(R.id.tv_name_transaction);
            tvCategory = itemView.findViewById(R.id.tv_category_transaction);
            tvTime = itemView.findViewById(R.id.tv_time_transaction);
            imgTransaction = itemView.findViewById(R.id.img_transaction);
            linParent = itemView.findViewById(R.id.lin_parent_item_transaction);
        }
    }
}
