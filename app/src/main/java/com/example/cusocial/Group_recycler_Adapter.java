package com.example.cusocial;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Group_recycler_Adapter extends RecyclerView.Adapter<Group_recycler_Adapter.ViewHolder> {
    Group_fragment groupFragment;
    ArrayList<Group_Adapter> groupArrayList;
    public Group_recycler_Adapter(Group_fragment groupFragment, ArrayList<Group_Adapter> groupArrayList) {
        this.groupFragment = groupFragment;
        this.groupArrayList = groupArrayList;
    }

    @NonNull
    @Override
    public Group_recycler_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(groupFragment.getContext()).inflate(R.layout.group_chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Group_recycler_Adapter.ViewHolder holder, int position) {
        Group_Adapter groupAdapter = groupArrayList.get(position);
        holder.group_name.setText(groupAdapter.Group_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iChatWin = new Intent(groupFragment.getContext(), Group_Chat_Windo.class);
                iChatWin.putExtra("GROUP_NAME", groupAdapter.Group_name);
                groupFragment.startActivity(iChatWin);
                groupFragment.getActivity().overridePendingTransition(LayoutTransition.APPEARING, LayoutTransition.DISAPPEARING);
//            R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }
    public void filterList(ArrayList<Group_Adapter> filteredList) {
        groupArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView group_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group_name = itemView.findViewById(R.id.group_name_final);
        }
    }
}
