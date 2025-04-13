package com.example.cusocial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class create_group extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView member_count;
    RecyclerView recyclerView;
    ArrayList<Users_Adapter> users_ArrayList, Active_User_Adapter,filteredList;
    member_recycler_adapter adapter;
    EditText Group_Name;
    Button group_btn;
    ImageView search_btn;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        member_count = findViewById(R.id.member_count);
        group_btn = findViewById(R.id.group_btn);
        Group_Name = findViewById(R.id.group_name_txt);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        users_ArrayList = new ArrayList<>();
        Active_User_Adapter = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users_Adapter users_Adapter = dataSnapshot.getValue(Users_Adapter.class);
                    if (users_Adapter.getUser_id().equals(firebaseAuth.getUid())) {
                        Active_User_Adapter.add(users_Adapter);
                    } else {
                        users_ArrayList.add(users_Adapter);
                    }
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.group_members_recycler);


//        recyclerView.setLayoutManager(new LinearLayoutManager(this_context));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new member_recycler_adapter(create_group.this, users_ArrayList, member_count, group_btn, Group_Name, Active_User_Adapter);
        recyclerView.setAdapter(adapter);


        search_btn= findViewById(R.id.search_icon);
        searchView = findViewById(R.id.create_group_searchView);
        search_btn.setBackgroundResource(R.drawable.search_alt_2_svgrepo_com);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getVisibility() == View.VISIBLE) {
                    searchView.setVisibility(View.GONE);
                    search_btn.setBackgroundResource(R.drawable.search_alt_2_svgrepo_com);
                } else {
                    searchView.setVisibility(View.VISIBLE);
                    search_btn.setBackgroundResource(R.drawable.baseline_clear_24);
                }
            }
        });

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });


    }
    private void search(String s) {
        ArrayList<Users_Adapter> filteredList = new ArrayList<>();
        for (Users_Adapter item : users_ArrayList) {
            if (item.getUID().toLowerCase().contains(s.toLowerCase()))
                filteredList.add(item);

        }
        adapter.filterList(filteredList);
    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(create_group.this, MainActivity.class));
        finish();

        super.onBackPressed();

    }
}