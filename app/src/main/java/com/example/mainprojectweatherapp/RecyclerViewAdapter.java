package com.example.mainprojectweatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ForecastModel> details;


    public RecyclerViewAdapter(Context context, ArrayList<ForecastModel> details){
        this.context = context;
        this.details=details;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dayTv, timeTv, tempTv;
        ImageView IconIv;
        public  ViewHolder(@NonNull View itemView){
            super(itemView);
            dayTv = itemView.findViewById(R.id.dayTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            tempTv = itemView.findViewById(R.id.tempTv);
            IconIv = itemView.findViewById(R.id.IconIv);

        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.forecase_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void  onBindViewHolder(@NonNull ViewHolder holder, int position){
        ForecastModel item = details.get(position);
        holder.dayTv.setText(details.get(position).getDay());
        holder.timeTv.setText(details.get(position).getTime());
        holder.tempTv.setText(details.get(position).getTemperature());



//        String icon = details.get(position).getImageUrl();
            Picasso.get().load(item.getImageUrl()).into(holder.IconIv);

    }

    @Override
    public int getItemCount() {
        return details.size();
    }
    public void setDetails(ArrayList<ForecastModel> details){

        this.details =details;
        notifyDataSetChanged();
    }
}
