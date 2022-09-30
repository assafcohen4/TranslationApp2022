package com.app.translation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.translation.Database.MyPref;
import com.app.translation.Model.User;
import com.google.gson.Gson;

public class SplashScreen extends AppCompatActivity {
    MyPref myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        myPref = new MyPref(this);

        new Handler().postDelayed(() -> {
            Intent intent;
            if(myPref.getUser().isEmpty()){
                intent = new Intent(SplashScreen.this, SignInScreen.class);
            }else{
                intent = new Intent(SplashScreen.this, TranslationScreen.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}