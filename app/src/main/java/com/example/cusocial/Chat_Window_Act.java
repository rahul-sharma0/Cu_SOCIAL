package com.example.cusocial;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Window_Act extends AppCompatActivity {
    DateTimeFormatter sdf = null;
    LocalDateTime myDateObj;
    String currentDateandTime =null;

    String RECEIVER_NAME, RECEIVER_UID, SENDER_UID,RECEIVER_IMAGE,USERNAME,SenderUID,Message_id;
    CircleImageView profile_image;
    TextView receiver_name;
    EditText message_edit_text;
    ImageButton send_btn;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArrayList<MSG_Model> ChatArrayList = new ArrayList<>();
    public static String SenderIMG, ReceiverIMG;
    public static String SenderRoom, ReceiverRoom,Key;
    MSG_Adapter msgAdapter;
    ZegoSendCallInvitationButton call,video;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        back=findViewById(R.id.btn_back);

        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onBackPressed();
                                    }
                                });


        RECEIVER_IMAGE = getIntent().getStringExtra("RECEIVER_IMAGE");
        RECEIVER_NAME = getIntent().getStringExtra("RECEIVER_NAME");
        RECEIVER_UID = getIntent().getStringExtra("RECEIVER_UID");
        SENDER_UID = firebaseAuth.getUid();

        send_btn = findViewById(R.id.btn_send_chat);
        message_edit_text = findViewById(R.id.txt_message_chat);

        profile_image = findViewById(R.id.chat_header_image);
        receiver_name = findViewById(R.id.chat_header_name);

        Picasso.get().load(RECEIVER_IMAGE).into(profile_image);
        receiver_name.setText(RECEIVER_NAME);


        recyclerView =findViewById(R.id.chats_recycler_view);

        LinearLayoutManager layout= new LinearLayoutManager(this);
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);

        msgAdapter = new MSG_Adapter(Chat_Window_Act.this,ChatArrayList);
        recyclerView.setAdapter(msgAdapter);


        SenderRoom = SENDER_UID + RECEIVER_UID;
        ReceiverRoom = RECEIVER_UID + SENDER_UID;

        DatabaseReference reference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        DatabaseReference chat_reference = firebaseDatabase.getReference().child("Chats").child(SenderRoom).child("Messages");

        chat_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    MSG_Model msgModel = dataSnapshot.getValue(MSG_Model.class);
                    ChatArrayList.add(msgModel);
                }
                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SenderIMG = snapshot.child("image").getValue().toString();
                ReceiverIMG = RECEIVER_IMAGE;
                SenderUID = snapshot.child("uid").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
            sdf = DateTimeFormatter.ofPattern("E, MMM dd yyyy\tHH:mm:ss");
            currentDateandTime = myDateObj.format(sdf);
            Log.e("Date",currentDateandTime);
        }


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CHAT_MESSAGE = message_edit_text.getText().toString();
                if (CHAT_MESSAGE.isEmpty()) {
                    Toast.makeText(Chat_Window_Act.this, "Enter the message first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                    message_edit_text.setText(null);
                    Date Cur_date = new Date();
                    Key = firebaseDatabase.getReference().child("Chats").child(SenderRoom).child("Messages").push().getKey();
                    Message_id = Key;
                    MSG_Model msgModel = new MSG_Model(CHAT_MESSAGE,SENDER_UID,currentDateandTime,Message_id);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("Chats").child(SenderRoom).child("Messages").child(Key)
                            .setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference().child("Chats").child(ReceiverRoom).child("Messages").child(Key)
                                            .setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

            }
        });

        call = findViewById(R.id.chat_call_btn);
        video = findViewById(R.id.chat_video_btn);
        USERNAME = getIntent().getStringExtra("USERNAME");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                proceedService(USERNAME);
//                startActivity(new Intent(Chat_Window_Act.this,Calling_act.class).putExtra("username",USERNAME));

//
                String targetUserID = RECEIVER_UID; // The ID of the user you want to call.
                String targetUserName = RECEIVER_NAME; // The username of the user you want to call.
//                Context context = getApplicationContext() ; // Android context.

//                ZegoSendCallInvitationButton button = new ZegoSendCallInvitationButton(context);
                call.setIsVideoCall(false);
                call.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
                call.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));



            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetUserID = RECEIVER_UID; // The ID of the user you want to call.
                String targetUserName = RECEIVER_NAME; // The username of the user you want to call.
//                Context context = getApplicationContext(); // Android context.

//                ZegoSendCallInvitationButton button = new ZegoSendCallInvitationButton(context);
                video.setIsVideoCall(true);
                video.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
                video.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));


            }
        });

        Application application = getApplication(); // Android's application context
        long appID = 456883046;   // yourAppID
        String appSign ="9d704563532d464d257bc25217d07d5ce2e580a9a0b2fbb349c135344910bed0";  // yourAppSign
        String userID =firebaseAuth.getUid(); // yourUserID, userID should only contain numbers, English characters, and '_'.
//        String userName = firebaseAuth.getUid();   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userID,callInvitationConfig);

    }
    void proceedService(String User_ID)
    {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallService.unInit();
    }

    @Override
    public void onBackPressed() {
//     else {

         startActivity(new Intent(Chat_Window_Act.this,MainActivity.class));
         finish();
         Log.e("MSg","Iam running");
        super.onBackPressed();
//
//     }
    }
}