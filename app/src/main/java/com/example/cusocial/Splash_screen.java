package com.example.cusocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Splash_screen extends AppCompatActivity {
    LocalDateTime myDateObj;
        TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DateTimeFormatter sdf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
            sdf = DateTimeFormatter.ofPattern("E, MMM dd yyyy\tHH:mm:ss");
            String currentDateandTime = myDateObj.format(sdf);
            Log.e("Date",currentDateandTime);
        }



//        Log.e("Date",currentDateandTime);


        textView = findViewById(R.id.splash_txt);
//        scale();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);
    }
    public  void scale()
    {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.spash_anim);
        textView.startAnimation(animation);
        return;
    }
}