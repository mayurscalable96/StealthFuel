package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class AddVehical extends AppCompatActivity {
  EditText vehical_year,vehical_make,vehical_model,vehical_color,vehical_plate,vehical_state;
  String vehical_yearText,vehical_makeText,vehical_modelText,vehical_colorText,vehical_plateText,vehical_stateText;
  TextView toolbartextView;

  Spinner spinner_gas_type;
  Button add_vehical_submit;
  private ProgressDialog pDialog;
  androidx.appcompat.widget.Toolbar toolbar;
  View view;
  SharedPrefrenceData sharedPrefrenceData;
  String access_tokene,custid;
  String spinneritem;

  List<String> listid = new ArrayList<String>();
  List<String> listgastype = new ArrayList<String>();
  String gastype_id,gastypeText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_vehical);
    sharedPrefrenceData = new SharedPrefrenceData(this);
    access_tokene = sharedPrefrenceData.getCustomerAccessToken();
    custid = sharedPrefrenceData.getCustomerId();

    toolbartextView = findViewById(R.id.toolbartextView);
    toolbartextView.setText("Add Vehicle");
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // enabling action bar app icon and behaving it as toggle button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    vehical_year = findViewById(R.id.vehical_year);
    vehical_make = findViewById(R.id.vehical_make);
    vehical_model = findViewById(R.id.vehical_model);
    vehical_color = findViewById(R.id.vehical_color);
    vehical_plate = findViewById(R.id.vehical_plate);
    vehical_state = findViewById(R.id.vehical_state);

    add_vehical_submit = findViewById(R.id.add_vehical_submit);
    add_vehical_submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAddVehicalEdittext();
      }
    });

    view = findViewById(android.R.id.content);

    addItemsOnSpinner();

    pDialog = new ProgressDialog(AddVehical.this, R.style.AppCompatAlertDialogStyle);
    // Showing progress dialog before making http request
    pDialog.setMessage("Loading...");
    pDialog.setCanceledOnTouchOutside(false);
    pDialog.show();
    getAllGasType();

    spinner_gas_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        gastype_id = listid.get(position);


      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }

    });

  }

  public void checkAddVehicalEdittext(){


    vehical_yearText = vehical_year.getText().toString();
    vehical_makeText = vehical_make.getText().toString();
    vehical_modelText = vehical_model.getText().toString();
    vehical_colorText = vehical_color.getText().toString();
    vehical_plateText = vehical_plate.getText().toString();
    vehical_stateText = vehical_state.getText().toString();
    spinneritem = spinner_gas_type.getSelectedItem().toString();

    if(vehical_yearText.equals("")||vehical_makeText.equals("")||vehical_modelText.equals("")
            ||vehical_colorText.equals("")||vehical_plateText.equals("")||vehical_stateText.equals("")
            || spinneritem.equals("Select Gas Type")){

      Snackbar.make(view,"All field are required.", Snackbar.LENGTH_LONG).show();

    }
    else{
      pDialog = new ProgressDialog(AddVehical.this, R.style.AppCompatAlertDialogStyle);
      // Showing progress dialog before making http request
      pDialog.setMessage("Loading...");
      pDialog.setCanceledOnTouchOutside(false);
      pDialog.show();
      addVehicalVolley();
      // Snackbar.make(view,"Done", Snackbar.LENGTH_LONG).show();
    }

  }

  // add items into spinner dynamically
  public void addItemsOnSpinner() {

    spinner_gas_type = (Spinner) findViewById(R.id.spinner_gas_type);

    listgastype.add("Select Gas Type");
    listid.add("24171");

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
            R.layout.spinner_textview, listgastype);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_gas_type.setAdapter(dataAdapter);
  }


  private void getAllGasType(){
    String url = UrlConstants.GASTYPE+"?AccessTokan="+access_tokene;
    Log.e("url ",url);
    JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, url,null,
            // The third parameter Listener overrides the method onResponse() and passes
            //JSONObject as a parameter
            new Response.Listener<JSONObject>() {
              // Takes the response from the JSON request
              @Override
              public void onResponse(JSONObject response) {
                try {
                  String Status = response.getString("Status");
                  if (Status.equalsIgnoreCase("Success")) {
                    JSONObject jsonObject = response.getJSONObject("Response");
                    JSONArray jsonArray = jsonObject.getJSONArray("GasType");
                    for (int i = 0; i < jsonArray.length(); i++) {
                      JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                      String GasType = jsonObject1.getString("GasType");
                      String id = jsonObject1.getString("Id");
                      listid.add(id);
                      listgastype.add(GasType);
                    }
                  }
                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                  pDialog.dismiss();
                  //If an error occurs, this prints the error to the log
                  Log.e("JSONException", String.valueOf(e));
                  e.printStackTrace();
                }


                pDialog.dismiss();
              }
            },
            // The final parameter overrides the method onErrorResponse() and passes VolleyError
            //as a parameter
            new Response.ErrorListener() {
              @Override
              // Handles errors that occur due to Volley
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
            }
    );
    obreq.setRetryPolicy(new DefaultRetryPolicy(
            5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add your Requests to the RequestQueue to execute */
    RequestQueue mRequestQueue = Volley.newRequestQueue(this);
    mRequestQueue.add(obreq);
  }

  private void addVehicalVolley() {

    String url = UrlConstants.ADDVEHICAL+"?Year="+vehical_yearText+"&Make="
            +vehical_makeText+"&Model="+vehical_modelText+"&Color="+vehical_colorText+"&Tag="+vehical_plateText
            +"&TagState="+vehical_stateText+"&GasType="+gastype_id+"&AccessTokan="+access_tokene+"&CustID="+custid;
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
                    Intent intent = new Intent(AddVehical.this,VehiclesList.class);
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
