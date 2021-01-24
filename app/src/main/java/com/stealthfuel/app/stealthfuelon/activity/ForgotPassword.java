package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
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
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    TextView toolbartextView;
    EditText forgot_email;
    Button forgot_submit;
    String forgot_emailText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    View view;
    private ProgressDialog pDialog;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Forgot Password");

        view = findViewById(android.R.id.content);

        forgot_email = findViewById(R.id.forgot_email);
        forgot_submit = findViewById(R.id.forgot_submit);
        forgot_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_emailText = forgot_email.getText().toString();
                if(!forgot_emailText.matches(emailPattern)){
                    Snackbar.make(view, "Enter Correct Email", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else{
                    pDialog = new ProgressDialog(ForgotPassword.this, R.style.AppCompatAlertDialogStyle);
                    // Showing progress dialog before making http request
                    pDialog.setMessage("Loading...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                    forgottPasswordVolley();
                }

            }
        });
    }

    private void forgottPasswordVolley() {


        String url = UrlConstants.FORGOT_PASSWORD+"?EmailId="+forgot_emailText;
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
