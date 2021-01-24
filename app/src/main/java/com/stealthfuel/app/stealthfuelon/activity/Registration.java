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
import com.android.volley.DefaultRetryPolicy;
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


public class Registration extends AppCompatActivity implements View.OnClickListener{
    TextView toolbartextView,alreadyaccount;
    EditText fullname,mob,email,pwd,repwd,refcode;
    Intent intent;
    String fullnameText,mobText,emailText,pwdText,repwdText,refcodeText;
    Button register;
    View view;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog pDialog;
    SharedPrefrenceData sharedPrefrenceData;

    private androidx.appcompat.widget.Toolbar toolbar;
    String device_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Registration");

        alreadyaccount = findViewById(R.id.alreadyaccount);
        alreadyaccount.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);


        fullname = findViewById(R.id.fullname);
        mob = findViewById(R.id.mob);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        repwd = findViewById(R.id.repwd);
        refcode = findViewById(R.id.refcode);
        view = findViewById(android.R.id.content);

        sharedPrefrenceData = new SharedPrefrenceData(this);
        device_id = sharedPrefrenceData.getDeviceToken();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alreadyaccount:
                intent = new Intent(this,Login.class);
                startActivity(intent);
                break;
            case R.id.register:{
                checkRegistrationEditText();
            }
            default:
                break;
        }
    }

    public void checkRegistrationEditText(){

        fullnameText = fullname.getText().toString();
        mobText = mob.getText().toString();
        emailText = email.getText().toString();
        pwdText = pwd.getText().toString();
        repwdText = repwd.getText().toString();
        refcodeText  = refcode.getText().toString();

        if(fullnameText.length()==0){
            Snackbar.make(view, "Enter Full Name", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(mobText.length()< 10){
            Snackbar.make(view, "Enter Valid Mobile Number", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(!emailText.matches(emailPattern)){
            Snackbar.make(view, "Enter Correct Email", Snackbar.LENGTH_LONG).show();
            return;
        }
//        if(pwdText.length()<=4){
//            Snackbar.make(view, "Password must be greater than 4 character", Snackbar.LENGTH_LONG).show();
//            return;
//        }
//        if(!pwdText.equals(repwdText)){
//            Snackbar.make(view, "Password must be same", Snackbar.LENGTH_LONG).show();
//            return;
//        }
        else{
           // Snackbar.make(view, "Tommoroew volley", Snackbar.LENGTH_LONG).show();
            pDialog = new ProgressDialog(Registration.this, R.style.AppCompatAlertDialogStyle);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
//            registration();
            sendOtp();

         }

    }



    private void sendOtp() {

        String url = UrlConstants.SEND_OTP+"?MobileNo="+mobText+"&Type=Reg";
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
                                String Message = jsonObject.getString("Message");
                                pDialog.dismiss();
                                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(Registration.this,OtpVerification.class);
                                intent.putExtra("otp",jsonObject.getString("SendOTP"));
                                intent.putExtra("activity","registration");
                                intent.putExtra("mobile_no",mobText);
                                intent.putExtra("full_name",fullnameText);
                                intent.putExtra("email",emailText);
                                intent.putExtra("refrel_code",refcodeText);
                                intent.putExtra("device_id",device_id);
                                startActivity(intent);

                            }
                            else{
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                pDialog.dismiss();
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add your Requests to the RequestQueue to execute */
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(req);
    }


    private void registration() {

        String url = UrlConstants.REGISTRATION+"?FullName="+fullnameText+"&EmailID="
                +emailText+"&MobileNo="+mobText+"&Password="+pwdText+"&ReferalCode="+refcodeText+"&DeviceID="+device_id;
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
                                String Message = jsonObject.getString("Message");
                                String customer_id = jsonObject.getString("CustId");
                                String customer_name = jsonObject.getString("FullName");
                                String customer_email = jsonObject.getString("EmailID");
                                String customer_mob = jsonObject.getString("MobileNo");
                                String customer_access_token = jsonObject.getString("AccessToken");
                                String customer_refralcode = jsonObject.getString("ReferalCode");
                                sharedPrefrenceData.setCustomerId(customer_id);
                                sharedPrefrenceData.setCustomerName(customer_name);
                                sharedPrefrenceData.setCustomerEmail(customer_email);
                                sharedPrefrenceData.setCustomerMob(customer_mob);
                                sharedPrefrenceData.setCustomerAccessToken(customer_access_token);
                                sharedPrefrenceData.setCustomerRefralCode(customer_refralcode);

                                pDialog.dismiss();
                                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(Registration.this,Home.class);
                                startActivity(intent);


                            }
                            else{
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                pDialog.dismiss();
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
