package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ContactUs extends NavigationDrawerActivity {

    View view;
    SharedPrefrenceData sharedPrefrenceData;
    EditText contact_description;
    String contactText;
    Button submit_contact;
    private ProgressDialog pDialog;
    String cust_id,access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_contact_us);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_contact_us, null, false);
        drawer.addView(contentView, 0);
        toolbartextView.setText("Contact Us");

        view = findViewById(android.R.id.content);
        sharedPrefrenceData = new SharedPrefrenceData(this);
        cust_id = sharedPrefrenceData.getCustomerId();
        access_token = sharedPrefrenceData.getCustomerAccessToken();

        contact_description = findViewById(R.id.contact_description);
        submit_contact = findViewById(R.id.submit_contact);
        submit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactText = contact_description.getText().toString();

                if(contactText.equals("")){
                    Snackbar.make(view, "Please enter text", Snackbar.LENGTH_LONG).show();
                } else {
                    pDialog = new ProgressDialog(ContactUs.this, R.style.AppCompatAlertDialogStyle);
                    // Showing progress dialog before making http request
                    pDialog.setMessage("Loading...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                    contactUsVolley();
                }

            }
        });
    }

    private void contactUsVolley() {

        String url = UrlConstants.CONTACTUS+"?CustID="+cust_id+"&AccessTokan="+access_token+"&Description="+contactText;
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
                                contact_description.setText("");
                            }
                            else{
                                pDialog.dismiss();
                                Snackbar.make(view, Status, Snackbar.LENGTH_LONG).show();
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

        /* Add your Requests to the RequestQueue to execute */
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(req);
    }
}
