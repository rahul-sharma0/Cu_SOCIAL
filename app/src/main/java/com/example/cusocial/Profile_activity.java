package com.example.cusocial;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_activity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Uri Image_uri = null;
    String Role, Uid, Name, Email, Password, Contact,Status,Class,Batch,Profile_pic= null;
    TextView profile_delete_btn;
    ImageView profile_circular_image;
    TextView NAME, EMAIL,UID,ROLE;
    TextInputEditText STATUS,PASSWORD,CONTACT;
    Button profile_update_btn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Profile...");
        dialog.show();



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile");
        progressDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        profile_circular_image = findViewById(R.id.profile_circular_image);
        NAME = findViewById(R.id.profile_txt_fullname);
        STATUS = findViewById(R.id.profile_txt_status);
        EMAIL = findViewById(R.id.profile_txt_email);
        PASSWORD = findViewById(R.id.profile_txt_password);
        CONTACT = findViewById(R.id.profile_txt_contact);
        UID = findViewById(R.id.profile_txt_uid);

        ROLE = findViewById(R.id.profile_txt_role);

        profile_delete_btn = findViewById(R.id.profile_delete_btn);
        profile_update_btn = findViewById(R.id.profile_update_btn);

        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference().child("Uploads").child(firebaseAuth.getUid());






        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Name = String.valueOf(snapshot.child("fullname").getValue());
                Email = String.valueOf(snapshot.child("emailid").getValue());
                Password = String.valueOf(snapshot.child("password").getValue());
                Contact = String.valueOf(snapshot.child("contactnum").getValue());
                Uid = String.valueOf(snapshot.child("uid").getValue());
                Role = String.valueOf(snapshot.child("role").getValue());
                Status = String.valueOf(snapshot.child("status").getValue());
                Profile_pic = String.valueOf(snapshot.child("image").getValue());



                NAME.setText(Name);
                EMAIL.setText(Email);
                PASSWORD.setText(Password);
                CONTACT.setText(Contact);
                UID.setText(Uid);
                ROLE.setText(Role);
                STATUS.setText(Status);
                Picasso.get().load(Profile_pic).into(profile_circular_image);

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_uri =null;
                profile_circular_image.setImageURI(Image_uri);
                Picasso.get().load(Image_uri).into(profile_circular_image);
                profile_circular_image.setBackgroundResource(R.drawable.add_camera_photo);
            }
        });

        profile_circular_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),10);

            }
        });

        profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                Name = NAME.getText().toString();
                Email = EMAIL.getText().toString();
                Password = PASSWORD.getText().toString();
                Contact = CONTACT.getText().toString();
                Role = ROLE.getText().toString();
                Status = STATUS.getText().toString();

                Uid = UID.getText().toString();

                if (Image_uri!=null)
                {
                    storageReference.putFile(Image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Profile_pic = uri.toString();
                                    Users_Adapter users_adapter = new Users_Adapter(firebaseAuth.getUid(),Email,Uid,Name,Role,Contact,Password,Status,Profile_pic);
                                    reference.setValue(users_adapter).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(Profile_activity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Profile_activity.this, MainActivity.class));
                                                finish();
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(Profile_activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    Profile_pic = "https://firebasestorage.googleapis.com/v0/b/cusocial-db.firebasestorage.app/o/user%20(2).png?alt=media&token=1a38847c-6b01-424f-8df4-2c26f8168194";
                    Users_Adapter users_adapter = new Users_Adapter(firebaseAuth.getUid(),Email,Uid,Name,Role,Contact,Password,Status,Profile_pic);
                    reference.setValue(users_adapter).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Log.e("phone",Contact);
                                Toast.makeText(Profile_activity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Profile_activity.this, MainActivity.class));
                                finish();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Profile_activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Image_uri = data.getData();
            profile_circular_image.setBackgroundResource(0);
            profile_circular_image.setImageURI(Image_uri);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile_activity.this,MainActivity.class));
        finish();
        super.onBackPressed();

    }
}