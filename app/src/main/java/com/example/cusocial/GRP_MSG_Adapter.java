package com.example.cusocial;

import static com.example.cusocial.Chat_Window_Act.ReceiverIMG;
import static com.example.cusocial.Chat_Window_Act.ReceiverRoom;
import static com.example.cusocial.Chat_Window_Act.SenderIMG;
import static com.example.cusocial.Chat_Window_Act.SenderRoom;
import static com.example.cusocial.Group_Chat_Windo.GROUP_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GRP_MSG_Adapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<GRP_MSG_Model> ChatArrayList;
    int SENDER_VIEW_TYPE = 1, RECEIVER_VIEW_TYPE = 2;
    public GRP_MSG_Adapter(Context context, ArrayList<GRP_MSG_Model> chatArrayList) {
        this.context = context;
        ChatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)
            return new GRP_MSG_Adapter.SenderViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_grp_layout,parent,false));
        else
            return new GRP_MSG_Adapter.ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_grp_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GRP_MSG_Model msgModel = ChatArrayList.get(position);
        if (holder.getClass() == GRP_MSG_Adapter.SenderViewHolder.class)
        {
            GRP_MSG_Adapter.SenderViewHolder senderViewHolder = (GRP_MSG_Adapter.SenderViewHolder) holder;
            senderViewHolder.sender_message.setText(msgModel.getMESSAGE());
            senderViewHolder.sender_message_timeStamp.setText(msgModel.getTimeStamp());
            senderViewHolder.sender_name.setText(msgModel.getSENDER_USERNAME());
            Picasso.get().load(msgModel.getPhoto()).into(senderViewHolder.sender_image);
        }
        else
        {
            GRP_MSG_Adapter.ReceiverViewHolder receiverViewHolder = (GRP_MSG_Adapter.ReceiverViewHolder) holder;
            receiverViewHolder.receiver_message.setText(msgModel.getMESSAGE());
            receiverViewHolder.receiver_message_timeStamp.setText(msgModel.getTimeStamp());
            receiverViewHolder.receiver_name.setText(msgModel.getSENDER_USERNAME());
            Picasso.get().load(msgModel.getPhoto()).into(receiverViewHolder.receiver_image);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (holder.getClass() == GRP_MSG_Adapter.SenderViewHolder.class) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference sender_db = FirebaseDatabase.getInstance().getReference().child("Groups").child(GROUP_NAME).child("Group Messages");
                                    sender_db.child(msgModel.getMessage_id()).removeValue();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        GRP_MSG_Model msgModel = ChatArrayList.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(msgModel.getSENDER_UID())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView sender_message, sender_message_timeStamp,sender_name;
        CircleImageView sender_image;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sender_name = itemView.findViewById(R.id.group_sender_username);
            sender_message = itemView.findViewById(R.id.group_sender_txt_msg);
            sender_image = itemView.findViewById(R.id.group_sender_circular_img);
            sender_message_timeStamp = itemView.findViewById(R.id.group_sender_txt_timeStamp);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiver_message, receiver_message_timeStamp,receiver_name;
        CircleImageView receiver_image;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver_name = itemView.findViewById(R.id.group_receiver_username);
            receiver_message = itemView.findViewById(R.id.group_receiver_txt_msg);
            receiver_image = itemView.findViewById(R.id.group_receiver_circular_img);
            receiver_message_timeStamp = itemView.findViewById(R.id.group_receiver_txt_timeStamp);
        }
    }
}
