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

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {

    ArrayList<Event> eventData = new ArrayList<Event>();

    public void setEventData(ArrayList<Event> eventData){
        this.eventData = eventData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvID.setText(String.valueOf(eventData.get(position).getEventID()));
        holder.tvName.setText(String.valueOf(eventData.get(position).getEventName()));
        holder.tvCategoryID.setText(String.valueOf(eventData.get(position).getCategoryID()));
        holder.tvEventCount.setText(String.valueOf(eventData.get(position).getEventCount()));
        holder.tvActive.setText(eventData.get(position).isActive()? "Active" : "Inactive");

        holder.itemView.setOnClickListener(v -> {

            final String eventNameSearch = eventData.get(position).getEventName();

            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
            intent.putExtra("eventNameSearch", eventNameSearch);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {

        if(this.eventData != null){
            return this.eventData.size();
        }

        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView tvID, tvName, tvCategoryID, tvEventCount, tvActive;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCategoryID = itemView.findViewById(R.id.tv_EventCount);
            tvEventCount = itemView.findViewById(R.id.tv_tickets);
            tvActive = itemView.findViewById(R.id.tv_active);
        }

    }

}
