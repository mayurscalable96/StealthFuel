package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;

public class LoginOrRegister extends AppCompatActivity implements View.OnClickListener {
    Button createnewacc;
    TextView alreadyaccount;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        createnewacc = findViewById(R.id.createnewacc);
        createnewacc.setOnClickListener(this);
        alreadyaccount = findViewById(R.id.alreadyaccount);
        alreadyaccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.createnewacc:{
                 intent = new Intent(this,Registration.class);
                 startActivity(intent);
                 break;
             }
             case R.id.alreadyaccount:{
                 intent = new Intent(this,Login.class);
                 startActivity(intent);
                 break;
             }
             default:{
                 break;
             }
         }
    }
}
