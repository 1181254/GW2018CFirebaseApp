package com.auribises.myfirebaseapp.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.auribises.myfirebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        TextView txt = findViewById(R.id.textView2);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        txt.startAnimation(animation);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(user!=null){
            handler.sendEmptyMessageDelayed(201,3000);
        }else{
            handler.sendEmptyMessageDelayed(101,3000);
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101){
                Intent intent = new Intent(SplashActivity.this,RegisterUserActivity.class);
                startActivity(intent);
                finish();
            }

            if(msg.what == 201){
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
