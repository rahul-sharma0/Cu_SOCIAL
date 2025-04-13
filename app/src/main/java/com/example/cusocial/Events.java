package com.example.cusocial;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Events extends AppCompatActivity {

    FloatingActionButton add_event;
    private final Calendar from_calendar = Calendar.getInstance();
    private final Calendar till_calendar = Calendar.getInstance();
    Button event_btn_cancel, event_btn_submit;
    TextInputEditText event_title, event_body, event_from, event_till;
    DateTimeFormatter sdf;
    LocalDateTime myDateObj;
    String TITLE,BODY,FROM,TILL,USERNAME,TIMESTAMP;
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Events_Adapter Adapter;
    RecyclerView recyclerView;
    ArrayList<Events_Model> Events_ModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        add_event = findViewById(R.id.add_event);
        add_event.setVisibility(GONE);



        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fullname").getValue().toString();
                String uid = snapshot.child("uid").getValue().toString();
                String role = snapshot.child("role").getValue().toString();

                if (role.equals("Faculty"))
                    add_event.setVisibility(VISIBLE);

                USERNAME = "~By "+name+", "+uid;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.event_recycler);
        Adapter = new Events_Adapter(Events_ModelArrayList,Events.this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(Adapter);

        DatabaseReference db = firebaseDatabase.getReference().child("Events");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Events_ModelArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Events_Model model = dataSnapshot.getValue(Events_Model.class);
                    Events_ModelArrayList.add(model);

                }
                Adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
            sdf = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
            TIMESTAMP = myDateObj.format(sdf);
            Log.e("Date",TIMESTAMP);
        }



        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Events.this);
                dialog.setContentView(R.layout.add_event);
                dialog.show();

                event_from = dialog.findViewById(R.id.add_event_from);
                event_till = dialog.findViewById(R.id.add_event_till);

                event_from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(Events.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from_calendar.set(Calendar.YEAR,year);
                                from_calendar.set(Calendar.MONTH,month);
                                from_calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                                String myFormat = "MMM dd, yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                event_from.setText(sdf.format(from_calendar.getTime()));
                            }
                        }, from_calendar.get(Calendar.YEAR), from_calendar.get(Calendar.MONTH), from_calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });


                event_till.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(Events.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                till_calendar.set(Calendar.YEAR,year);
                                till_calendar.set(Calendar.MONTH,month);
                                till_calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                                String myFormat = "MMM dd, yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                event_till.setText(sdf.format(till_calendar.getTime()));
                            }
                        }, till_calendar.get(Calendar.YEAR), till_calendar.get(Calendar.MONTH), till_calendar.get(Calendar.DAY_OF_MONTH)).show();
//                        Toast.makeText(Events.this, "Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                event_title = dialog.findViewById(R.id.event_title);
                event_body = dialog.findViewById(R.id.add_event_body);


                event_title.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length()>44)
                            event_title.setError("Title too long");

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                event_btn_cancel = dialog.findViewById(R.id.event_btn_cancel);

                event_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                event_btn_submit = dialog.findViewById(R.id.event_btn_submit);

                event_btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressDialog = new ProgressDialog(Events.this);
                        progressDialog.setMessage("Creating Event...");
                        progressDialog.setCancelable(false);

                        if ((event_title.getText().toString().isEmpty()) || (event_body.getText().toString().isEmpty()) || (event_from.getText().toString().isEmpty()) || (event_till.getText().toString().isEmpty()))
                        {
                            event_title.setError("Title is required");
                            event_body.setError("Body is required");
                            event_from.setError("Date is required");
                            event_till.setError("Date is required");
                            Toast.makeText(Events.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.show();

                        TITLE = event_title.getText().toString();
                        BODY = event_body.getText().toString();
                        FROM = event_from.getText().toString();
                        TILL = event_till.getText().toString();

                        Events_Model model = new Events_Model(TITLE,BODY,FROM,TILL,USERNAME,TIMESTAMP);

                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Events").push();
                        databaseReference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Events.this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                });

            }
        });





    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Events.this,MainActivity.class));
        finish();

        super.onBackPressed();
    }
}