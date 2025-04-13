package com.example.cusocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.alterac.blurkit.BlurLayout;

public class cu_splash extends AppCompatActivity {
    Animation alpha,scale;
    ImageView cu_logo;

    BlurLayout blurLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cu_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        blurLayout = findViewById(R.id.blur);
        cu_logo = findViewById(R.id.cu_logo);

        alpha = AnimationUtils.loadAnimation(this,R.anim.alpha);
        scale = AnimationUtils.loadAnimation(this,R.anim.scale);

        SharedPreferences sharedPreferences =getSharedPreferences("Login",MODE_PRIVATE);
        boolean login_check = sharedPreferences.getBoolean("Is_Logged_In",false);
        Intent iLogin;
        if (login_check)
        {
            iLogin = new Intent(cu_splash.this, MainActivity.class);

        }
        else
        {
            iLogin = new Intent(cu_splash.this, Login_activity.class);
        }




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blurLayout.setVisibility(View.VISIBLE);
                blurLayout.startBlur();
                cu_logo.startAnimation(alpha);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(iLogin);
                        finish();
                    }
                },2000);



            }
        },1500);




//                blurLayout.startAnimation(alpha);




    }


    @Override
    protected void onStop() {
        blurLayout.pauseBlur();
        super.onStop();
    }
}