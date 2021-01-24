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

public class EditAccount extends AppCompatActivity {

    TextView toolbartextView;
    EditText edit_name,edit_mob,edit_email;
    Button edit_submit;
    SharedPrefrenceData sharedPrefrenceData;
    String edit_nameText,edit_mobText,edit_emailText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    View view;
    private ProgressDialog pDialog;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPrefrenceData = new SharedPrefrenceData(this);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Edit");

        view = findViewById(android.R.id.content);

        edit_name = findViewById(R.id.edit_name);
        edit_mob = findViewById(R.id.edit_mob);
        edit_email = findViewById(R.id.edit_email);
        edit_name.setText(sharedPrefrenceData.getCustomerName());
        edit_mob.setText(sharedPrefrenceData.getCustomerMob());
        edit_email.setText(sharedPrefrenceData.getCustomerEmail());
        edit_email.setEnabled(false);
        edit_submit = findViewById(R.id.edit_submit);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditText();
            }
        });

    }

    public void checkEditText(){
        edit_nameText = edit_name.getText().toString();
        edit_mobText = edit_mob.getText().toString();
        edit_emailText = edit_email.getText().toString();

        if(edit_nameText.length()==0){
            Snackbar.make(view, "Enter Full Name", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(edit_mobText.length()==0){
            Snackbar.make(view, "Enter Mobile Number", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(!edit_emailText.matches(emailPattern)){
            Snackbar.make(view, "Enter Correct Email", Snackbar.LENGTH_LONG).show();
            return;
        }
        else{

            pDialog = new ProgressDialog(EditAccount.this, R.style.AppCompatAlertDialogStyle);
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            editCustomerVolley();

        }
    }

    private void editCustomerVolley() {

        String url = UrlConstants.EDITCUSTOMERDETAIL+"?FullName="+edit_nameText+"&EmailID="
                +edit_emailText+"&MobileNo="+edit_mobText+"&CustId="+sharedPrefrenceData.getCustomerId();
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
                                String customer_name = jsonObject.getString("FullName");
                                String customer_email = jsonObject.getString("EmailID");
                                String customer_mob = jsonObject.getString("MobileNo");
                                String access_token = jsonObject.getString("AccessToken");
                                sharedPrefrenceData.setCustomerName(customer_name);
                                sharedPrefrenceData.setCustomerEmail(customer_email);
                                sharedPrefrenceData.setCustomerMob(customer_mob);
                                sharedPrefrenceData.setCustomerAccessToken(access_token);

                                pDialog.dismiss();
                                //Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(EditAccount.this,Account.class);
                                startActivity(intent);


                            }
                            else{
                                pDialog.dismiss();
                                Snackbar.make(view, Status, Snackbar.LENGTH_LONG).show();
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
