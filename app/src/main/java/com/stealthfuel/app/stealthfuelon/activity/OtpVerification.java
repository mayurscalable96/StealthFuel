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

public class OtpVerification extends AppCompatActivity implements View.OnClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;
    TextView resend_otp,toolbartextView;
    EditText enter_otp;
    Button otpVerify;
    private ProgressDialog pDialog;
    SharedPrefrenceData sharedPrefrenceData;
    String fullnameText,mobText,emailText,pwdText,repwdText,refcodeText;
    String device_id;
    View view;
    String otpText;
    Intent intent;
    String activtyText,getOtpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("OTP Verification");
        resend_otp = findViewById(R.id.resend_otp);
        resend_otp.setOnClickListener(this);
        enter_otp = findViewById(R.id.enter_otp);
        otpVerify = findViewById(R.id.otpVerify);
        otpVerify.setOnClickListener(this);
        view = findViewById(android.R.id.content);
        sharedPrefrenceData = new SharedPrefrenceData(this);

        activtyText = getIntent().getStringExtra("activity");
        mobText = getIntent().getStringExtra("mobile_no");
        getOtpText = getIntent().getStringExtra("otp");
        Log.e("getOtpText",getOtpText);
        device_id =  getIntent().getStringExtra("device_id");
        if(activtyText.equals("registration")){
            fullnameText =  getIntent().getStringExtra("full_name");
            emailText =  getIntent().getStringExtra("email");
            refcodeText =  getIntent().getStringExtra("refrel_code");
        }
    }
    public void verifyOtp(){
        otpText = enter_otp.getText().toString();
       Log.e("otp",getOtpText);
        if(otpText.equals(getOtpText)){
            pDialog = new ProgressDialog(OtpVerification.this, R.style.AppCompatAlertDialogStyle);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            if(activtyText.equals("registration")){
                registration();
            }else{
                login();
            }
        }else{
            Snackbar.make(view, "Please Enter Correct OTP.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resend_otp:
                pDialog = new ProgressDialog(OtpVerification.this, R.style.AppCompatAlertDialogStyle);
                // Showing progress dialog before making http request
                pDialog.setMessage("Loading...");
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                sendOtp();
                break;
            case R.id.otpVerify:{
                verifyOtp();
            }
            default:
                break;
        }
    }

    private void sendOtp() {
        String activityName = "";
        if(activtyText.equals("registration")){
            activityName = "Reg";
        }else{
            activityName = "Login";
        }
        String url = UrlConstants.SEND_OTP+"?MobileNo="+mobText+"&Type="+activityName;        Log.e("url ",url);
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
                                Log.e("OTP",jsonObject.getString("SendOTP"));
                                getOtpText = jsonObject.getString("SendOTP");
                                pDialog.dismiss();
                                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
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

    private void login() {
        String url = UrlConstants.LOGIN+"?MobileNo="+mobText+"&DeviceID="+device_id;
        Log.e("Loginurl ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            Log.e("jsonObjectLogin", String.valueOf(response));
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
                                // Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(OtpVerification.this,Home.class);
                                startActivity(intent);

                            }
                            else{
                                String ErrorMessage = jsonObject.getString("Message");
                                pDialog.dismiss();
                                Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                //VolleyLog.e("Error: ", error.getMessage());
                String message = null;
                Snackbar snackbar;
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
        Log.e("Registrationurl ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            Log.e("jsonObjectRegistration", String.valueOf(response));

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
                                Intent intent = new Intent(OtpVerification.this,Home.class);
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