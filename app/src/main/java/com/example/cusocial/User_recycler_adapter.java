package com.example.cusocial;

import static androidx.core.content.ContextCompat.startActivity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_recycler_adapter extends  RecyclerView.Adapter<User_recycler_adapter.ViewHolder> {
    Chats_fragment chatsFragment;
    ArrayList<Users_Adapter> usersArrayList;
    public User_recycler_adapter(Chats_fragment chatsFragment, ArrayList<Users_Adapter> usersArrayList) {
        this.chatsFragment = chatsFragment;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public User_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(chatsFragment.getContext()).inflate(R.layout.user_chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_recycler_adapter.ViewHolder holder, int position) {
        Users_Adapter usersAdapter = usersArrayList.get(position);
        holder.User_name.setText(usersAdapter.UID);
        holder.User_status.setText(usersAdapter.FULLNAME);
        if (holder.circleImageView != null) {
//            holder.circleImageView.setImageResource(R.drawable.users); // Replace with your image resource
            Picasso.get().load(usersAdapter.IMAGE).into(holder.circleImageView);

        } else {
            // Handle the case where profileImageView is null (e.g., log an error, use a default view, etc.)
            Log.e("User_recycler_adapter", "profileImageView is null!");
//            holder.circleImageView.setImageResource(R.drawable.users);

//            Picasso.get().load(usersAdapter.IMAGE).into(holder.circleImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iChatWin = new Intent(chatsFragment.getContext(), Chat_Window_Act.class);
                iChatWin.putExtra("RECEIVER_NAME", usersAdapter.FULLNAME);
                iChatWin.putExtra("RECEIVER_UID", usersAdapter.user_id);
                iChatWin.putExtra("RECEIVER_IMAGE", usersAdapter.IMAGE);
                iChatWin.putExtra("USERNAME", usersAdapter.UID);
                chatsFragment.startActivity(iChatWin);
                chatsFragment.getActivity().overridePendingTransition(LayoutTransition.APPEARING, LayoutTransition.DISAPPEARING);
//            R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public void filterList(ArrayList<Users_Adapter> filteredList) {
        usersArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        val profileImageView: CircleImageView = itemView.findViewById(R.id.user_profile_image);

        CircleImageView circleImageView;
        TextView User_name, User_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_img_circular);
            User_name = itemView.findViewById(R.id.user_name);
            User_status = itemView.findViewById(R.id.user_last_message);
        }

    }
}
