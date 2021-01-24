package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class AboutUs extends NavigationDrawerActivity {

    TextView about_description;
    View view;
    SharedPrefrenceData sharedPrefrenceData;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about_us);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_about_us, null, false);
        drawer.addView(contentView, 0);
        toolbartextView.setText("About Us");

        view = findViewById(android.R.id.content);
        sharedPrefrenceData = new SharedPrefrenceData(this);
        about_description = findViewById(R.id.about_description);
        pDialog = new ProgressDialog(AboutUs.this, R.style.AppCompatAlertDialogStyle);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        aboutUsVolley();
    }

    private void aboutUsVolley() {

        String url = UrlConstants.ABOUTUS+"?AccessTokan="+sharedPrefrenceData.getCustomerAccessToken();
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Status = response.getString("Status");

                            if(Status.equalsIgnoreCase("Success")){
                                JSONObject jsonObject = response.getJSONObject("Response");
                                String Description = jsonObject.getString("Description");

                                about_description.setText(Description);
                                pDialog.dismiss();

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

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /* Add your Requests to the RequestQueue to execute */
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(req);
    }
}
