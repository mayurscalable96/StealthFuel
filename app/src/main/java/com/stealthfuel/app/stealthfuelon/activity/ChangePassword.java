package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stealthfuel.app.stealthfuelon.R;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    EditText old_pwd,new_pwd;
    String old_pwdText,new_pwdText;
    Button change_pwd_submit;
    View view;
    private ProgressDialog pDialog;
    SharedPrefrenceData sharedPrefrenceData;
    TextView toolbartextView;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        view = findViewById(android.R.id.content);

        sharedPrefrenceData = new SharedPrefrenceData(this);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Change Password");

        old_pwd = findViewById(R.id.old_pwd);
        new_pwd = findViewById(R.id.new_pwd);
        change_pwd_submit = findViewById(R.id.change_pwd_submit);

        change_pwd_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChangePwd();
            }
        });
    }

    public void checkChangePwd(){

        old_pwdText = old_pwd.getText().toString();
        new_pwdText = new_pwd.getText().toString();

        if(old_pwdText.length()<=4){
            Snackbar.make(view, "Password must be greater than 4 character", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(new_pwdText.length()<=4){
            Snackbar.make(view, "Password must be greater than 4 character", Snackbar.LENGTH_LONG).show();
            return;
        }
        else{
            // Snackbar.make(view, "Tommoroew volley", Snackbar.LENGTH_LONG).show();
            pDialog = new ProgressDialog(ChangePassword.this, R.style.AppCompatAlertDialogStyle);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            changePwdVolley();
        }
    }

    private void changePwdVolley() {

        String url = UrlConstants.CHANGEPASSWORD+"?CustId="+sharedPrefrenceData.getCustomerId()+"&EmailID="
                +sharedPrefrenceData.getCustomerEmail()+"&OldPassword="+old_pwdText+"&NewPassword="+new_pwdText;
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            if(Status.equalsIgnoreCase("Success")){
                                /*String Message = jsonObject.getString("Message");
                                String customer_name = jsonObject.getString("FullName");
                                String customer_email = jsonObject.getString("EmailID");
                                String customer_mob = jsonObject.getString("MobileNo");
                                sharedPrefrenceData.setCustomerName(customer_name);
                                sharedPrefrenceData.setCustomerEmail(customer_email);
                                sharedPrefrenceData.setCustomerMob(customer_mob);*/
                                sharedPrefrenceData.logout();

                                pDialog.dismiss();
                                //Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangePassword.this,Login.class);
                                startActivity(intent);


                            }
                            else{
                                pDialog.dismiss();
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
                            }





                        } catch (JSONException e) {
                            pDialog.dismiss();
                            Log.e("JSONException", String.valueOf(e) );
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.e("Error: ", error.getMessage());
                pDialog.dismiss();
                String message = null;
                Log.e("volleyError", String.valueOf(volleyError));

                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(req);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
