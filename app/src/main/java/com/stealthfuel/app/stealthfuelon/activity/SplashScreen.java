package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;

public class SplashScreen extends AppCompatActivity {

    Intent intent;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SharedPrefrenceData sharedPrefrenceData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPrefrenceData = new SharedPrefrenceData(this);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(sharedPrefrenceData.getCustomerId()!=null){
                    intent = new Intent(SplashScreen.this,Home.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(SplashScreen.this,LoginOrRegister.class);
                    startActivity(intent);
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
