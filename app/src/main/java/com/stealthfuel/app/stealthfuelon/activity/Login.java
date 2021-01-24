package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

public class Login extends AppCompatActivity implements View.OnClickListener{

    TextView newaccount,forgotpwd,toolbartextView;
    EditText login_email,login_pwd,mob;
    Button login;
    Intent intent;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String login_emailText,login_pwdtext,mobText;
    View view;
    private ProgressDialog pDialog;
    SharedPrefrenceData sharedPrefrenceData;

    private androidx.appcompat.widget.Toolbar toolbar;
    String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Login");

        view = findViewById(android.R.id.content);

//        login_email = findViewById(R.id.login_email);
//        login_pwd = findViewById(R.id.login_pwd);
        mob = findViewById(R.id.mob);

        newaccount = findViewById(R.id.newaccount);
        newaccount.setOnClickListener(this);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        forgotpwd = findViewById(R.id.forgotpwd);
        forgotpwd.setOnClickListener(this);

        sharedPrefrenceData = new SharedPrefrenceData(this);
        device_id = sharedPrefrenceData.getDeviceToken();
        //Log.e("device token",sharedPrefrenceData.getDeviceToken());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newaccount:
                intent = new Intent(this,Registration.class);
                startActivity(intent);
                break;
            case R.id.login:
                checkEditext();
                break;
            case R.id.forgotpwd:{
                intent = new Intent(this,ForgotPassword.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    public void checkEditext(){
//        login_emailText = login_email.getText().toString();
//        login_pwdtext = login_pwd.getText().toString();
        mobText = mob.getText().toString();

//comment code may be possible client revert the
//        if(!login_emailText.matches(emailPattern)){
//            Snackbar.make(view, "Enter Correct Email", Snackbar.LENGTH_LONG).show();
//            return;
//        }
//        if(login_pwdtext.length()<=4){
//            Snackbar.make(view, "Password must be greater than 4 character", Snackbar.LENGTH_LONG).show();
//            return;
//        }
        if(mobText.length() < 10){
            Snackbar.make(view, "Enter Valid Mobile Number", Snackbar.LENGTH_LONG).show();
            return;
        }
        else{
            pDialog = new ProgressDialog(Login.this, R.style.AppCompatAlertDialogStyle);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
//            login();
            sendOtp();
        }

    }


    private void sendOtp() {

        String url = UrlConstants.SEND_OTP+"?MobileNo="+mobText+"&Type=Login";
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            Log.e("response otp", String.valueOf(response));
                            if(Status.equalsIgnoreCase("Success")){
                                String Message = jsonObject.getString("Message");
                                pDialog.dismiss();
                                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                intent = new Intent(Login.this,OtpVerification.class);
                                intent.putExtra("otp",jsonObject.getString("SendOTP"));
                                intent.putExtra("activity","login");
                                intent.putExtra("mobile_no",mobText);
                                intent.putExtra("device_id",device_id);
                                startActivity(intent);
                            }
                            else {
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
        String url = UrlConstants.LOGIN+"?EmailId="+login_emailText+"&Password="+login_pwdtext+"&DeviceID="+device_id;
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
                               // Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this,Home.class);
                                startActivity(intent);

                            }
                            else{
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_HOME);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
   /* @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
