package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stealthfuel.app.stealthfuelon.R;

public class NavigationDrawerActivity extends AppCompatActivity implements View.OnClickListener{
        //implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    TextView toolbartextView;
    Intent intent;
    RelativeLayout layout_request_gas,layout_account,layout_orders,layout_invite
            ,layout_promotions,layout_faq,layout_contact,layout_about;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbartextView = findViewById(R.id.toolbartextView);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);

        layout_request_gas = findViewById(R.id.layout_request_gas);
        layout_request_gas.setOnClickListener(this);
        layout_account = findViewById(R.id.layout_account);
        layout_account.setOnClickListener(this);
        layout_orders = findViewById(R.id.layout_orders);
        layout_orders.setOnClickListener(this);
        layout_invite = findViewById(R.id.layout_invite);
        layout_invite.setOnClickListener(this);
        layout_promotions = findViewById(R.id.layout_promotions);
        layout_promotions.setOnClickListener(this);
        layout_faq = findViewById(R.id.layout_faq);
        layout_faq.setOnClickListener(this);
        layout_contact = findViewById(R.id.layout_contact);
        layout_contact.setOnClickListener(this);
        layout_about = findViewById(R.id.layout_about);
        layout_about.setOnClickListener(this);
        layout_promotions.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.layout_request_gas: {
                intent = new Intent(this,Home.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_account:{
                intent = new Intent(this,Account.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_invite:{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.stealthfuel.app.stealthfuelon";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));

                //intent = new Intent(this,InviteFriends.class);
                //startActivity(intent);
                break;
            }
            case R.id.layout_faq :{
                intent = new Intent(this,Faq.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_contact:{
                intent = new Intent(this,ContactUs.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_about:{
                intent = new Intent(this,AboutUs.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_orders:{
                intent = new Intent(this,OrderList.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_promotions:{
                intent = new Intent(this,Promotions.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        /*int id = v.getId();

        if (id == R.id.layout_request_gas) {
            intent = new Intent(this,Home.class);
            startActivity(intent);
        } else if (id == R.id.layout_account) {
            intent = new Intent(this,Account.class);
            startActivity(intent);

        } else if(id == R.id.layout_invite) {
            intent = new Intent(this,InviteFriends.class);
            startActivity(intent);
        }*/
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            intent = new Intent(this,Notification.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.account) {
            intent = new Intent(this,Account.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
