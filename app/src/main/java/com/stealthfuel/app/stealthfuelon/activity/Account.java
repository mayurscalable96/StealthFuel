package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;

public class Account extends NavigationDrawerActivity {//implements View.OnClickListener{

    TextView cust_name,cust_mob,cust_email;
    SharedPrefrenceData sharedPrefrenceData;
    LinearLayout customer_detail_layout,change_pwd_layout,vehicles_layout,logout_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_account);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_account, null, false);
        drawer.addView(contentView, 0);
        toolbartextView.setText("Account");

        sharedPrefrenceData = new SharedPrefrenceData(this);

        cust_name = findViewById(R.id.cust_name);
        cust_mob = findViewById(R.id.cust_mob);
        cust_email = findViewById(R.id.cust_email);

        cust_name.setText(sharedPrefrenceData.getCustomerName());
        cust_mob.setText(sharedPrefrenceData.getCustomerMob());
        cust_email.setText(sharedPrefrenceData.getCustomerEmail());

        customer_detail_layout = findViewById(R.id.customer_detail_layout);
       // customer_detail_layout.setOnClickListener(this);
        customer_detail_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Account.this, EditAccount.class);
                startActivity(intent);
            }
        });
        change_pwd_layout = findViewById(R.id.change_pwd_layout);
       // change_pwd_layout.setOnClickListener(this);
        change_pwd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Account.this, ChangePassword.class);
                startActivity(intent);
            }
        });
        vehicles_layout = findViewById(R.id.vehicles_layout);
       // vehicles_layout.setOnClickListener(this);
        vehicles_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Account.this,VehiclesList.class);
                startActivity(intent);
            }
        });

        logout_layout = findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefrenceData.logout();
                intent = new Intent(Account.this,Login.class);
                startActivity(intent);
            }
        });

    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customer_detail_layout: {
                intent = new Intent(this, EditAccount.class);
                startActivity(intent);
                break;
            }
            case R.id.change_pwd_layout:{
                intent = new Intent(this, ChangePassword.class);
                startActivity(intent);
                break;
            }
            case R.id.vehicles_layout:{
                intent = new Intent(this,VehiclesList.class);
                startActivity(intent);
                break;
            }
           // default:
            //break;
        }

    }*/
}
