package com.example.cusocial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Group_Chat_Windo extends AppCompatActivity {

    DateTimeFormatter sdf = null;
    LocalDateTime myDateObj;
    String currentDateandTime =null;
    public static String GROUP_NAME;
    String SenderIMG,SenderUID, SenderUsername;
    Button back;
    ImageView send_btn;
    TextView group_name;
    RecyclerView recyclerView;
    EditText message_edit_text;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ArrayList<GRP_MSG_Model> ChatArrayList = new ArrayList<>();
    GRP_MSG_Adapter msgAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_chat_windo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        SenderIMG = firebaseAuth.getCurrentUser

        GROUP_NAME = getIntent().getStringExtra("GROUP_NAME");

        back=findViewById(R.id.group_btn_back);
        send_btn = findViewById(R.id.btn_send_group);
        message_edit_text = findViewById(R.id.txt_message_group);
        group_name = findViewById(R.id.group_header_name);
        recyclerView = findViewById(R.id.group_chat_recycler);
        group_name.setText(GROUP_NAME);

        LinearLayoutManager layout= new LinearLayoutManager(this);
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);

        msgAdapter = new GRP_MSG_Adapter(Group_Chat_Windo.this,ChatArrayList);
        recyclerView.setAdapter(msgAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference reference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SenderIMG = snapshot.child("image").getValue().toString();
                SenderUID = firebaseAuth.getUid();
                SenderUsername = snapshot.child("uid").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Groups").child(GROUP_NAME).child("Group Messages");;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    GRP_MSG_Model msgModel = dataSnapshot.getValue(GRP_MSG_Model.class);
                    ChatArrayList.add(msgModel);
                }
                msgAdapter.notifyDataSetChanged();
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
                    Toast.makeText(Group_Chat_Windo.this, "Enter the message first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                message_edit_text.setText(null);
                Date Cur_date = new Date();
                String key = firebaseDatabase.getReference().child("Groups").child(GROUP_NAME).child("Group Messages").push().getKey();
                String Message_id = key;
                GRP_MSG_Model msgModel = new GRP_MSG_Model(CHAT_MESSAGE,SenderUID,SenderIMG,currentDateandTime,SenderUsername,Message_id);
//                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("Groups").child(GROUP_NAME).child("Group Messages").child(key)
                        .setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }

                        });

            }
        });





    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(Group_Chat_Windo.this,MainActivity.class));
        finish();

        super.onBackPressed();
    }
}