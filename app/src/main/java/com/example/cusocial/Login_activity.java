package com.example.cusocial;

import static com.google.firebase.sessions.api.FirebaseSessionsDependencies.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.strictmode.FragmentStrictMode;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.alterac.blurkit.BlurLayout;

public class Login_activity extends AppCompatActivity {

    TextView btn_register;
    private static final String Student_pattern = "[0-9]{2}[a-z|A-Z]{3}[0-9]{5}@[cuchd|CUCHD]{5}.[in|IN]{2}";
    private static final String Faculty_pattern = "[e|E][0-9]{5}@[cuchd|CUCHD]{5}.[in|IN]{2}";
    TextInputEditText EMAIL,PASSWORD;
    FirebaseAuth firebaseAuth;
//    AuthCredential uid,password;
;
    Button btn_login;
    ProgressDialog progressDialog;

    BlurLayout blurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        getActionBar().hide();
//        credential = null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging You In");
        progressDialog.setCancelable(false);

        firebaseAuth=FirebaseAuth.getInstance();

        EMAIL = findViewById(R.id.txt_login_uid);
        PASSWORD = findViewById(R.id.txt_login_password);
        btn_login = findViewById(R.id.btn_login);





        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String uid = EMAIL.getText().toString();
                 String password = PASSWORD.getText().toString();

                if (TextUtils.isEmpty(uid))
                {
                    Toast.makeText(Login_activity.this, "Email-id is required", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login_activity.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (!(uid.matches(Student_pattern)||uid.matches(Faculty_pattern))) {
                    Toast.makeText(Login_activity.this,"Email-id is not valid",Toast.LENGTH_LONG).show();
                } else if (password.length()<8) {
//                    password.setError("Password must be of 8 characters");
                    Toast.makeText(Login_activity.this,"Password must be of 8 characters",Toast.LENGTH_LONG).show();
                }
                else {

                    progressDialog.show();

//                    startActivity(new Intent(Login_activity.this,MainActivity.class));
//                    finish();


                    firebaseAuth.signInWithEmailAndPassword(uid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    progressDialog.dismiss();

                                    SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
//                    editor.putString("uid",uid);
//                    editor.putString("password",password);
                                    editor.putBoolean("Is_Logged_In",true);
                                    editor.apply();

                                    Intent main_intent = new Intent(Login_activity.this, MainActivity.class);
                                    startActivity(main_intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(Login_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


        btn_register = findViewById(R.id.txt_register_btn);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_activity.this,Signup_activity.class));
            }
        });

        blurLayout = findViewById(R.id.blurView);



    }

    @Override
    protected void onStart() {
        super.onStart();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}