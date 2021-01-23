package com.example.catku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.catku.R;
import com.example.catku.object.Category;
import com.example.catku.object.Schedule;

import java.util.List;


public abstract class ScheduleRecyclers extends RecyclerView.Adapter<ScheduleRecyclers.MyViewHolder> {
    List<Schedule> item;
    Context context;
    public ScheduleRecyclers(Context context, List<Schedule> item){
        this.context = context;
        this.item = item;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R
                .layout.list_item_setting_profile, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Schedule itemTrip = item.get(position);

        holder.tvNameCategory.setText(itemTrip.getDescription());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditItem(itemTrip);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteItem(itemTrip,position);
            }
        });


    }

    public abstract void onEditItem(Schedule itemTrip);
    public abstract void onDeleteItem(Schedule itemTrip, int pos);



    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameCategory;
        ImageView btnEdit;
        ImageView btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNameCategory = itemView.findViewById(R.id.tv_name_item);
            btnEdit = itemView.findViewById(R.id.btn_img_edit_item);
            btnDelete = itemView.findViewById(R.id.btn_img_delete_item);
        }
    }
}
