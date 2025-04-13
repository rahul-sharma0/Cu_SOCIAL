package com.example.cusocial;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Layout layout;
    ImageView userImage;
    String Name, Uid,Email,Role;
    TextView Username,User_id;
    String User_Img, User_ID,User_Email,User_Name;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    int recent_id = R.id.opt_chats;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // need a activityContext.
//        PermissionX.init(MainActivity.this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
//                .onExplainRequestReason(new ExplainReasonCallback() {
//                    @Override
//                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
//                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
//                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
//                    }
//                }).request(new RequestCallback() {
//                    @Override
//                    public void onResult(boolean allGranted, @NonNull List<String> grantedList,
//                                         @NonNull List<String> deniedList) {
//                    }
//                });



        firebaseAuth = FirebaseAuth.getInstance();

//        Log.e("info","Iam running: "+firebaseAuth.getCurrentUser().getUid());

//        User_ID = firebaseAuth.getCurrentUser().getUid();





        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this,Login_activity.class));
            finish();
        }



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        load_fragment(new messenger_fragment(), false);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (recent_id == id) {
                    Toast.makeText(MainActivity.this, "Already Opened", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.opt_profile) {
                    startActivity(new Intent(MainActivity.this,Profile_activity.class));
                    recent_id = id;
                } else if (id == R.id.opt_about) {
                    startActivity(new Intent(MainActivity.this,Aboutus.class));
                    recent_id=id;
                } else if (id == R.id.opt_chats) {
                    load_fragment(new messenger_fragment(), true);
                    recent_id = id;
                    Log.e("val", "val" + recent_id);
                } else if (id == R.id.opt_Cuims) {
                    startActivity(new Intent(MainActivity.this,Browser_cu.class));
                    recent_id = id;
                } else if (id == R.id.opt_Announcement) {
                   startActivity(new Intent(MainActivity.this,Announcement.class));
                   recent_id = id;

                }else if (id == R.id.opt_Event) {
                    startActivity(new Intent(MainActivity.this,Events.class));
                    recent_id =id;

                }
                else {
                    firebaseAuth.signOut();
                    Intent logout_intent = new Intent(MainActivity.this, Login_activity.class);
                    startActivity(logout_intent);
                    finish();
                }


                drawerLayout.closeDrawer(GravityCompat.START);


                return true;
            }
        });
        View Header_view = navigationView.getHeaderView(0);
        ImageView header_image = Header_view.findViewById(R.id.drawer_user_image);
        TextView header_name = Header_view.findViewById(R.id.drawer_user_name);
        TextView header_id = Header_view.findViewById(R.id.drawer_user_uid);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference =firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 User_Img = snapshot.child("image").getValue().toString();
                  Name = snapshot.child("fullname").getValue().toString();
                  Uid = snapshot.child("emailid").getValue().toString();
                  Role = snapshot.child("role").getValue().toString();

                Picasso.get().load(User_Img).into(header_image);
                header_name.setText(Name);
                header_id.setText(Uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString("user_name",Name);
       editor.putString("user_id",Uid);
       editor.putString("user_role",Role);
       editor.apply();
//
////        Picasso.get().load(User_Img).into(userImage);
//        Username.setText(Name);
//        User_id.setText(Uid);

//        if (User_ID.equals(firebaseAuth.getCurrentUser().getUid()))
//        {
//            readData(User_ID);
//        }











    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.search_menu,menu);
//        MenuItem item = menu.findItem(R.id.search_opt);
//
//        SearchView searchView = (SearchView) item.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                search_User(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                search_User(newText);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

//    private void search_User(String query) {
//
//            reference.child("Users").equalTo("uid",query.toLowerCase()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    modelList.clear();
//                    for (DataSnapshot dataSnapshot: task.getResult().getChildren())
//                    {
//                        Users_Adapter Users_adapter = dataSnapshot.getValue(Users_Adapter.class);
//                        modelList.add(Users_adapter);
//                    }
//                    User_recycler_adapter adapter = new Users_Adapter(MainActivity.this,modelList);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//
//                }
//            });
//    }
//
    private void readData(String userId) {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().exists())
                    {
//                        Users_Adapter user = task.getResult().getValue(Users_Adapter.class);
//                        Username.setText(user.getFULLNAME());
//                        User_id.setText(user.getEMAILID());
                        DataSnapshot dataSnapshot = task.getResult();
                         User_Name =String.valueOf( dataSnapshot.child("fullname").getValue());
                         User_ID = String.valueOf(dataSnapshot.child("emailid").getValue()) ;
                         User_Img = String.valueOf(dataSnapshot.child("image").getValue()) ;

                         Log.e("info","Iam running: "+User_Name+" "+User_ID+" "+User_Img);
//                        Picasso.get().load(User_Img).into(userImage);
//                        Username.setText(User_Name);
//                        User_id.setText(User_Email);
                    }

                }
            }
        });
    }

    public void load_fragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (flag)
            fragmentTransaction.replace(R.id.parent_layout, fragment);

        else {
            fragmentTransaction.add(R.id.parent_layout, fragment);
        }
//        fragmentTransaction.addToBackStack();
        fragmentTransaction.commit();

    }

    boolean F;

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }




//            firebaseAuth.signOut();
//            Intent logout_intent = new Intent(MainActivity.this, Login_activity.class);
//            startActivity(logout_intent);
        else
            super.onBackPressed();

    }
}