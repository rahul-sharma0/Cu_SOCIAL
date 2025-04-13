package com.example.cusocial;

import static com.example.cusocial.Chat_Window_Act.ReceiverIMG;
import static com.example.cusocial.Chat_Window_Act.ReceiverRoom;
import static com.example.cusocial.Chat_Window_Act.SenderIMG;
import static com.example.cusocial.Chat_Window_Act.SenderRoom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MSG_Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MSG_Model> ChatArrayList;
    int SENDER_VIEW_TYPE = 1, RECEIVER_VIEW_TYPE = 2;

    public MSG_Adapter(Context context, ArrayList<MSG_Model> chatArrayList) {
        this.context = context;
        ChatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE)
            return new SenderViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_msg_layout,parent,false));
        else
            return new ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_msg_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MSG_Model msgModel = ChatArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class)
        {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.sender_message.setText(msgModel.getMESSAGE());
            senderViewHolder.sender_message_timeStamp.setText(msgModel.getTimeStamp());
            Picasso.get().load(SenderIMG).into(senderViewHolder.sender_image);
        }
        else
        {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.receiver_message.setText(msgModel.getMESSAGE());
            receiverViewHolder.receiver_message_timeStamp.setText(msgModel.getTimeStamp());
            Picasso.get().load(ReceiverIMG).into(receiverViewHolder.receiver_image);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (holder.getClass() == SenderViewHolder.class) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference sender_db = FirebaseDatabase.getInstance().getReference().child("Chats").child(SenderRoom).child("Messages");
                                    sender_db.child(msgModel.getMESSAGE_ID()).removeValue();
                                    DatabaseReference receiver_db = FirebaseDatabase.getInstance().getReference().child("Chats").child(ReceiverRoom).child("Messages");
                                    receiver_db.child(msgModel.getMESSAGE_ID()).removeValue();
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
        MSG_Model msgModel = ChatArrayList.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(msgModel.getSENDER_UID())) {
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView sender_message, sender_message_timeStamp;
        CircleImageView sender_image;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sender_message = itemView.findViewById(R.id.sender_txt_msg);
            sender_image = itemView.findViewById(R.id.sender_circular_img);
            sender_message_timeStamp = itemView.findViewById(R.id.sender_txt_timeStamp);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiver_message, receiver_message_timeStamp;
        CircleImageView receiver_image;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver_message = itemView.findViewById(R.id.receiver_txt_msg);
            receiver_image = itemView.findViewById(R.id.receiver_circular_img);
            receiver_message_timeStamp = itemView.findViewById(R.id.receiver_txt_timeStamp);
        }
    }

}
