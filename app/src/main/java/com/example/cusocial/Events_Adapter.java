package com.example.cusocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Events_Adapter extends RecyclerView.Adapter<Events_Adapter.ViewHolder> {
    ArrayList<Events_Model> Events_arrayList= new ArrayList<>();
    Events events;

    public Events_Adapter(ArrayList<Events_Model> events_arrayList, Events events) {
        Events_arrayList = events_arrayList;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(events.getApplicationContext()).inflate(R.layout.announcement_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Events_Adapter.ViewHolder holder, int position) {
        Events_Model events_model = Events_arrayList.get(position);
        holder.event_title.setText(events_model.getTitle());
        holder.event_body.setText(events_model.getBody()+"\nFrom: "+events_model.getEvent_till()+"\t Till:"+events_model.getEvent_till());
        holder.user_name.setText(events_model.getSendername());
        holder.timeStamp.setText(events_model.getCurrentDateandTime());
    }


    @Override
    public int getItemCount() {
        return Events_arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView event_title, event_body, user_name, timeStamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_title = itemView.findViewById(R.id.announcement_title);
            event_body = itemView.findViewById(R.id.announcement_body);
            user_name = itemView.findViewById(R.id.announcement_user);
            timeStamp = itemView.findViewById(R.id.announcement_timeStamp);
        }
    }
}
