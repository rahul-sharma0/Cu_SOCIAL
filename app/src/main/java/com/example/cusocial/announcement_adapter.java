package com.example.cusocial;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class announcement_adapter extends RecyclerView.Adapter<announcement_adapter.ViewHolder> {
    ArrayList<announcement_model> announcement_ArrayList = new ArrayList<>();
    Announcement announcement;

    public announcement_adapter(ArrayList<announcement_model> announcement_ArrayList, Announcement announcement) {
        this.announcement_ArrayList = announcement_ArrayList;
        this.announcement = announcement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(announcement.getApplicationContext()).inflate(R.layout.announcement_layout, parent, false);
        return new announcement_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        announcement_model model = announcement_ArrayList.get(position);
        holder.title.setText(model.title);
        holder.body.setText(model.body);
        holder.user.setText(model.sendername);
        holder.timeStamp.setText(model.currentDateandTime);
    }


    @Override
    public int getItemCount() {
        return announcement_ArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,body,user,timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announcement_title);
            body = itemView.findViewById(R.id.announcement_body);
            user= itemView.findViewById(R.id.announcement_user);
            timeStamp = itemView.findViewById(R.id.announcement_timeStamp);
        }
    }
}
