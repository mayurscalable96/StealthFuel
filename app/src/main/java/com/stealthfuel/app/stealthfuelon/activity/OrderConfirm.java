package com.stealthfuel.app.stealthfuelon.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderConfirm extends AppCompatActivity {

    TextView order_address,order_vehicle,gastype,gasprice,selected_date,selected_timeslot;
    EditText comment;
    String vehical_yearText,vehical_makeText,vehical_modelText,vehical_colorText,vehical_plateText,vehical_stateText
            ,vehical_gastypeText,vehical_gastypeIdText,vehical_priceText,vehical_addressText
            ,commentText;
    String vehical_id,selected_dateText;
    String ExtraAmountText,TimeSlotText,TimeSlotAmountText;
    double latitude,longitude;
    Intent intent;

    Button submit_order;

    SharedPrefrenceData sharedPrefrenceData;
    String access_tokene,c_id;
    private ProgressDialog pDialog;
    TextView toolbartextView;
    androidx.appcompat.widget.Toolbar toolbar;
    View view;
    TextView delivery_charge;
    String DeliveryCharge;

    private String clientToken;
    private static final int BRAINTREE_REQUEST_CODE = 4949;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Order Confirm");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        view = findViewById(android.R.id.content);

        sharedPrefrenceData = new SharedPrefrenceData(this);
        access_tokene = sharedPrefrenceData.getCustomerAccessToken();
        c_id = sharedPrefrenceData.getCustomerId();

        intent = getIntent();
        vehical_id = intent.getStringExtra("vehical_id");
        vehical_yearText = intent.getStringExtra("vehical_year");
        vehical_makeText = intent.getStringExtra("vehical_make");
        vehical_modelText = intent.getStringExtra("vehical_model");
        vehical_colorText = intent.getStringExtra("vehical_color");
        vehical_plateText = intent.getStringExtra("vehical_plate");
        vehical_stateText = intent.getStringExtra("vehical_state");
        vehical_gastypeText = intent.getStringExtra("vehical_gasetype");
        vehical_gastypeIdText = intent.getStringExtra("vehical_gasetypeid");
        vehical_priceText = intent.getStringExtra("price");
        vehical_addressText = intent.getStringExtra("address");
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        DeliveryCharge = intent.getStringExtra("delivery_charge");

        ExtraAmountText = intent.getStringExtra("extra_amount");
        TimeSlotText = intent.getStringExtra("time_slot");
        TimeSlotAmountText = intent.getStringExtra("time_slotamount");
        selected_dateText = intent.getStringExtra("selected_date");

        comment = findViewById(R.id.comment);


        order_address = findViewById(R.id.order_address);
        order_vehicle = findViewById(R.id.order_vehicle);
        gastype = findViewById(R.id.gastype);
        gasprice = findViewById(R.id.gasprice);
        selected_date = findViewById(R.id.selected_date);
        selected_timeslot = findViewById(R.id.selected_timeslot);
        delivery_charge = findViewById(R.id.delivery_charge);

        order_address.setText(vehical_addressText);
        order_vehicle.setText(vehical_makeText+" "+vehical_modelText+" "+vehical_colorText+" "+vehical_plateText);
        gastype.setText(vehical_gastypeText);
        gasprice.setText("$"+vehical_priceText);
        selected_timeslot.setText(TimeSlotAmountText);
        selected_date.setText(selected_dateText);

        delivery_charge.setText("First we will only deduct delivery charges that is $"+DeliveryCharge+
                ".After delivery is complete,amount of gas will be deducted from your card.");

        pDialog = new ProgressDialog(OrderConfirm.this, R.style.AppCompatAlertDialogStyle);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        getToken();
        //fixAmount();

        submit_order = findViewById(R.id.submit_order);
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentText = comment.getText().toString();
                    // Snackbar.make(view, "Select ", Snackbar.LENGTH_LONG).show();

                   /* pDialog = new ProgressDialog(OrderConfirm.this, R.style.AppCompatAlertDialogStyle);
                    // Showing progress dialog before making http request
                    pDialog.setMessage("Loading...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                    addOrderVolley();*/
                onBraintreeSubmit(view);

            }
        });
    }

    private void addOrderVolley(String paymentNonce) {

        String url = UrlConstants.ADDORDER+"?VehicleID="+vehical_id+"&Latitude="
                +latitude+"&longitude="+longitude+"&GasTypeID="+vehical_gastypeIdText+"&Price="+vehical_priceText
                +"&Date="+selected_dateText+"&TimeSlot="+TimeSlotText+"&ExtraAmount="+ExtraAmountText+"&AccessTokan="+access_tokene+"&CustId="+c_id
                +"&Address="+vehical_addressText+"&Comment="+commentText+"&PaymentMethodNonce="+paymentNonce+"&DeliveryCharge="+DeliveryCharge;
        url = url.replaceAll(" ", "+");
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("responce", String.valueOf(response));
                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            if(Status.equalsIgnoreCase("Success")){
                                //` String Message = jsonObject.getString("Message");
                                pDialog.dismiss();
                                //Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(OrderConfirm.this,OrderList.class);
                                startActivity(intent);

                            }
                            else{
                                pDialog.dismiss();
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                //Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
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
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add your Requests to the RequestQueue to execute */
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(req);
    }

    public void onBraintreeSubmit(View view){
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
       // dropInRequest.collectDeviceData(true);
        dropInRequest.disablePayPal();
        //dropInRequest.vaultManager(true);
        Log.e("dropInRequest", String.valueOf(dropInRequest));
        startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == BRAINTREE_REQUEST_CODE) {
                if (RESULT_OK == resultCode) {
                    DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                    Log.e("result", String.valueOf(result));

                    String paymentNonce = result.getPaymentMethodNonce().getNonce();
                    Log.e("paymentNonce", paymentNonce);
                    pDialog = new ProgressDialog(OrderConfirm.this, R.style.AppCompatAlertDialogStyle);
                    // Showing progress dialog before making http request
                    pDialog.setMessage("Loading...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    addOrderVolley(paymentNonce);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //Log.e("cancel", "User cancelled payment");
                    Snackbar.make(view, "Payment Canceled", Snackbar.LENGTH_LONG).show();
                } else {
                    Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                    //Log.e("error", " error exception"+error);
                    Snackbar.make(view, (CharSequence) error, Snackbar.LENGTH_LONG).show();
                }
            }

        }catch(Exception e){
            Log.e("exc", String.valueOf(e));
        }


    }

    private void paymentCheckout(String paymentNonce) {
        String url ="https://fuelwebservice.biddrip.a2hosted.com/webservice.asmx/PaymentCheckout?Price=1000&PaymentMethodNonce="+paymentNonce+"&OrderID="+"1855989817";
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
                                String paymentStatus = jsonObject.getString("paymentStatus");
                               // String Token = jsonObject.getString("Token");
                                //clientToken = jsonObject.getString("clientToken");
                                Log.e("paymentStatus",paymentStatus);
                                //  Log.e("Token",Token);
                            }
                            else{
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                Log.e("ErrorMessage",ErrorMessage);
                                Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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

    private void getToken() {
        String url = UrlConstants.GENERATEPAYMENTTOKEN+"?CustomerID="+c_id;
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("responce volley", String.valueOf(response));
                        try {
                            String Status = response.getString("Status");
                            JSONObject jsonObject = response.getJSONObject("Response");
                            if(Status.equalsIgnoreCase("Success")){
                                // String Message = jsonObject.getString("Message");
                                clientToken = jsonObject.getString("clientToken");
                                Log.e("c",clientToken);
                             pDialog.dismiss();
                            }
                            else{
                                pDialog.dismiss();
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.e("Error: ", error.getMessage());
                String message = null;
                pDialog.dismiss();
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


    private void fixAmount() {

        String url = UrlConstants.DELIVERYAMOUNT+"?AccessTokan="+access_tokene;
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

                                pDialog.dismiss();
                                // Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();

                                JSONArray jsonArray = jsonObject.getJSONArray("DeliveryAmount");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                DeliveryCharge =  jsonObject1.getString("DeliveryCharge");
                                delivery_charge.setText("First we will only deduct delivery charges that is $"+DeliveryCharge+
                                        ".After the delivery is complete,the delivery amount will be deducted from your card.");

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
