package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;

public class InviteFriends extends NavigationDrawerActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    Button share;
    TextView refcode;
    SharedPrefrenceData sharedPrefrenceData;
    String refcodeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_invite_friends);

      /*  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbartextView = findViewById(R.id.toolbartextView);*/
        String text = "Invite Friends";
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_invite_friends, null, false);
        drawer.addView(contentView, 0);
        toolbartextView.setText(text);

        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.example.admin.stealthfuel.activity \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
        });

        refcode = findViewById(R.id.refcode);
        sharedPrefrenceData = new SharedPrefrenceData(this);
        refcodeText = sharedPrefrenceData.getCustomerRefralCode();
        refcode.setText("YOUR CODE : "+refcodeText);
    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/
}
