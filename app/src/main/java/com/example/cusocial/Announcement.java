package com.example.cusocial;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Announcement extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArrayList<announcement_model> announcement_modelArrayList = new ArrayList<>();
    announcement_adapter adapter ;
    FloatingActionButton add_announcement;
    RecyclerView Announcement_recycler;
    EditText title, body;
    String TITLE,BODY,USER, name,uid,role = null;
    ProgressDialog progressDialog;
    ProgressDialog loader;
    LocalDateTime myDateObj;
    String currentDateandTime;
    DateTimeFormatter sdf = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loader = new ProgressDialog(this);
        loader.setCancelable(false);
        loader.setMessage("Loading Announcements...");
        loader.show();

        SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
//        role = sharedPreferences.getString("user_role",null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        add_announcement = findViewById(R.id.add_announcement);

        add_announcement.setVisibility(GONE);


        DatabaseReference reference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("fullname").getValue().toString();
                uid = snapshot.child("uid").getValue().toString();
                role = snapshot.child("role").getValue().toString();
                Log.e("err",role);
                if (role.equals("Faculty"))
                    add_announcement.setVisibility(VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Announcement...");
        progressDialog.setCancelable(false);

        Announcement_recycler = findViewById(R.id.announcement_recycler);

        adapter = new announcement_adapter(announcement_modelArrayList, Announcement.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        Announcement_recycler.setLayoutManager(layoutManager);
        Announcement_recycler.setAdapter(adapter);


        DatabaseReference db = firebaseDatabase.getReference().child("Announcements");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcement_modelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    announcement_model adapter1 = dataSnapshot.getValue(announcement_model.class);
                    announcement_modelArrayList.add(adapter1);
                }
                adapter.notifyDataSetChanged();
                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
            sdf = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
            currentDateandTime = myDateObj.format(sdf);
            Log.e("Date",currentDateandTime);
        }



        add_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(Announcement.this);
                dialog.setContentView(R.layout.add_announcement);
                dialog.setCancelable(false);

                title = dialog.findViewById(R.id.add_announcement_title);
                body =dialog.findViewById(R.id.add_announcement_body);

                Button submit = dialog.findViewById(R.id.add_btn_submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((title.getText().toString().isEmpty()) || ( body.getText().toString().isEmpty())) {
                            title.setError(null);
                            body.setError(null);
                            Toast.makeText(Announcement.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.show();
                        TITLE = title.getText().toString();
                        BODY = body.getText().toString();
                        USER = "~By "+name+", "+uid;
                        announcement_model announcement_model = new announcement_model(TITLE,BODY,USER,currentDateandTime);
                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Announcements").push();
                        databaseReference.setValue(announcement_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Announcement.this, "Announcement created successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });

                Button cancel =dialog.findViewById(R.id.add_btn_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });




    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Announcement.this,MainActivity.class));
        finish();
        super.onBackPressed();
    }
}