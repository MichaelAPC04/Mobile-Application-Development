package com.example.a3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerAdapterEventCategory extends RecyclerView.Adapter<MyRecyclerAdapterEventCategory.CustomViewHolder> {

    ArrayList<EventCategory> eventCategoryData = new ArrayList<EventCategory>();

    public void setData(ArrayList<EventCategory> eventCategoryData){
        this.eventCategoryData = eventCategoryData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_event_category, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvID.setText(String.valueOf(eventCategoryData.get(position).getCategoryID()));
        holder.tvName.setText(String.valueOf(eventCategoryData.get(position).getCategoryName()));
        holder.tvEventCount.setText(String.valueOf(eventCategoryData.get(position).getEventCount()));
        holder.tvIsActive.setText(eventCategoryData.get(position).isActive()? "Yes" : "No");

        holder.itemView.setOnClickListener(v -> {
            // CHECK
            final String selectedCountry = eventCategoryData.get(position).getEventLocation();
            final String categoryName = eventCategoryData.get(position).getCategoryName();

            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, GoogleMapsActivity.class);
            intent.putExtra("clickedCategoryCountry", selectedCountry);
            intent.putExtra("categoryName", categoryName);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        if(this.eventCategoryData != null){
            return this.eventCategoryData.size();
        }
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView tvID, tvName, tvEventCount, tvIsActive;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEventCount = itemView.findViewById(R.id.tv_EventCount);
            tvIsActive = itemView.findViewById(R.id.tv_active);
        }
    }

}
