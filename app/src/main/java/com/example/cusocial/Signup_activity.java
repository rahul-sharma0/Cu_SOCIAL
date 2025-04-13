package com.example.cusocial;

import static android.view.View.GONE;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup_activity extends AppCompatActivity {

    private int current_progress = 0;
//    private static final String Batch_Pattern = "\\w{3}\\d{5}";
//    private static final String Batch_Pattern = "";
    private ProgressBar progressBar;
//    AutoCompleteTextView txt_class;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    LinearLayout Signup_layout1,Signup_layout2;
    TextView txt_already_account;
    String Role, Uid, Name, Email, Password, Contact,Status,Class="22BCA 1-A",Batch="2022";
    TextInputEditText txt_email_id,txt_password,txt_confirm_password,txt_name,txt_phone_number,txt_status;
    CircleImageView profile_image;
    Uri imageUri = null;
    String image_uri;
    Button btn_Register,btn_Next,btn_Previous;
    private static final String Student_pattern = "[0-9]{2}[a-z|A-Z]{3}[0-9]{5}@[cuchd|CUCHD]{5}.[in|IN]{2}";
    private static final String Contact_pattern = "[6789]{1}\\d{9}$";
    private static final String Faculty_pattern = "[e|E][0-9]{5}@[cuchd|CUCHD]{5}.[in|IN]{2}";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User");
        progressDialog.setCancelable(false);



        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();



//        txt_class = findViewById(R.id.txt_class_section);


        txt_already_account = findViewById(R.id.txt_already_account);
        btn_Register = findViewById(R.id.btn_register);
        txt_phone_number = findViewById(R.id.txt_contact);
        txt_email_id = findViewById(R.id.txt_emailId);
        txt_name = findViewById(R.id.txt_fullname);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_reenter_password);
        txt_status = findViewById(R.id.txt_status);
        profile_image = findViewById(R.id.circular_image);
        btn_Next = findViewById(R.id.btn_next);
//        btn_Previous = findViewById(R.id.btn_previous);

        Signup_layout1 = findViewById(R.id.Signup_layout1);
        Signup_layout2 = findViewById(R.id.Signup_layout2);

        Editable data = txt_email_id.getText();

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
//
//                Log.e("data"," : "+Name+Email+Password+Contact+Role+Uid);
//                Log.e("data"," : "+Status);

                if (TextUtils.isEmpty(txt_status.getText().toString()))
                {
                    Status = "Hey there! Iam using Cu Social";
                }
                else {
                    Status = txt_status.getText().toString();
                }
                firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            String user_id = task.getResult().getUser().getUid();

                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user_id);
                            StorageReference storageReference = firebaseStorage.getReference().child("Uploads").child(user_id);

                            if (imageUri!=null)
                            {
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful())
                                        {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                         image_uri = uri.toString();
                                                         Users_Adapter usersAdapter = new Users_Adapter(user_id,Email,Uid,Name,Role,Contact,Password,Status,image_uri);
                                                         databaseReference.setValue(usersAdapter).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful())
                                                                    {
//                                                                        progressBar.setProgress(current_progress+50,true);
                                                                        progressDialog.dismiss();
//
                                                                        Toast.makeText(Signup_activity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                                        Intent login_intent = new Intent(Signup_activity.this, Login_activity.class);
                                                                        startActivity(login_intent);
                                                                        finish();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(Signup_activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                                                    }
                                                             }
                                                         });

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else {
//                                imageUri = Uri.parse("android.resource://"+getPackageName()+"/drawable/users");
//                                image_uri = imageUri.toString();
                                image_uri = "https://firebasestorage.googleapis.com/v0/b/cusocial-db.firebasestorage.app/o/user%20(2).png?alt=media&token=1a38847c-6b01-424f-8df4-2c26f8168194";

                                Users_Adapter usersAdapter = new Users_Adapter(user_id,Email,Uid,Name,Role,Contact,Password,Status,image_uri);
                                databaseReference.setValue(usersAdapter).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
//                                            progressBar.setProgress(current_progress+50,true);
//
                                            progressDialog.dismiss();
                                            Toast.makeText(Signup_activity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                            Intent login_intent = new Intent(Signup_activity.this, Login_activity.class);
                                            startActivity(login_intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(Signup_activity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            Log.e("err",task.getException().getMessage());
                            Toast.makeText(Signup_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TextUtils.isEmpty(txt_email_id.getText().toString())) || (TextUtils.isEmpty(txt_name.getText().toString())) || (TextUtils.isEmpty(txt_phone_number.getText().toString())) || (TextUtils.isEmpty(txt_password.getText().toString())) || (TextUtils.isEmpty(txt_confirm_password.getText().toString())))
                {
//                    progressBar.setProgress(current_progress+50,true);
                    txt_email_id.setError("Please enter email id");
                    txt_name.setError("Please enter name");
                    txt_phone_number.setError("Please enter phone number");
                    txt_password.setError("Please enter password");
                    txt_confirm_password.setError("Please enter confirm password");
//                    txt_class.setError("Please enter class and section");

                    Toast.makeText(Signup_activity.this, "All fields are required", Toast.LENGTH_LONG).show();
                }
                else if (!(txt_email_id.getText().toString().matches(Student_pattern)|| txt_email_id.getText().toString().matches(Faculty_pattern))) {
                    txt_email_id.setError("Invalid email id");
                }
                 else if ((txt_confirm_password.length()<=8 || txt_password.length()<=8)) {
                    Toast.makeText(Signup_activity.this, "Password length should be greater than 8 characters", Toast.LENGTH_LONG).show();
                } else if (!(txt_confirm_password.getText().toString().equals(txt_password.getText().toString()))) {
                    txt_confirm_password.setError("Password does not match");
                } else if (!(txt_phone_number.getText().toString().matches(Contact_pattern))) {
                    txt_phone_number.setError("Ex: +91-9876543210");
                    Toast.makeText(Signup_activity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                } else
                {
                    Name = txt_name.getText().toString();
                    Email = txt_email_id.getText().toString();
                    Password = txt_password.getText().toString();
                    Contact = txt_phone_number.getText().toString();
                    Role = getRole();
                    Uid = getUid(data);

                    onNext();
                }


            }
        });







//        defaultImage();
//        btn_Next.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Uid = getUid(data);
//                                            getBatch();
//                                        }
//                                    });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });






        txt_already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(Signup_activity.this, Login_activity.class);
                startActivity(login_intent);
                finish();
        }

    });
    }


    public String getRole(){
        String role = null;
        if (txt_email_id.getText().toString().matches(Student_pattern))
        {
            role ="Student";

//            Log.e("role"," :"+txt_email_id.getText().toString().matches(Student_pattern));

        } else if (txt_email_id.getText().toString().matches(Faculty_pattern)) {

            role= "Faculty";

        } else {
            Toast.makeText(Signup_activity.this, "Invalid email id", Toast.LENGTH_LONG).show();
            txt_email_id.setText(null);
        }
        return role;
    }
    public String getUid(Editable data){
        String[] uid = data.toString().split("@");
        return uid[0].toUpperCase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Signup_layout1.getVisibility() == View.GONE && Signup_layout2.getVisibility() == View.VISIBLE)
            {
                Signup_layout2.setVisibility(GONE);
                Signup_layout1.setVisibility(View.VISIBLE);
                txt_name.setText(Name);
                txt_email_id.setText(Email);
                txt_password.setText(Password);
                txt_confirm_password.setText(Password);
                txt_phone_number.setText(Contact);
            }
        else
            {
                super.onBackPressed();
            }
        }
        public void onNext() {
            Signup_layout2.setVisibility(View.VISIBLE);
            Signup_layout1.setVisibility(View.GONE);
            return;
        }

    }

