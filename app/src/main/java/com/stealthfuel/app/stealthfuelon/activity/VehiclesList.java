package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.stealthfuel.app.stealthfuelon.adapter.VehicalListAdapter;
import com.stealthfuel.app.stealthfuelon.geterseter.VehicalListItem;
import com.stealthfuel.app.stealthfuelon.other.RecyclerTouchListener;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class VehiclesList extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{

    TextView toolbartextView;
    androidx.appcompat.widget.Toolbar toolbar;
    RecyclerView recycle_vehical_list;
    VehicalListAdapter adapter;
    List<VehicalListItem>  vehicalListItemList;
    SharedPrefrenceData sharedPrefrenceData;
    String cust_id,access_token;
    View view ;
    private int requestCount = 0;
    ProgressBar progress_bar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_list);

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Vehicles");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedPrefrenceData = new SharedPrefrenceData(this);
        cust_id = sharedPrefrenceData.getCustomerId();
        access_token = sharedPrefrenceData.getCustomerAccessToken();

        view = findViewById(android.R.id.content);
        progress_bar = findViewById(R.id.progress_bar);
        setVehicalListRecycle();

        recycle_vehical_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycle_vehical_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                VehicalListItem vehicalListItem = vehicalListItemList.get(position);
                intent = new Intent(VehiclesList.this,EditVehicles.class);
                intent.putExtra("vehical_id",vehicalListItem.getVehical_id());
                intent.putExtra("vehical_year",vehicalListItem.getVehical_year());
                intent.putExtra("vehical_make",vehicalListItem.getVehical_make());
                intent.putExtra("vehical_model",vehicalListItem.getVehical_model());
                intent.putExtra("vehical_color",vehicalListItem.getVehical_color());
                intent.putExtra("vehical_plate",vehicalListItem.getVehical_plateno());
                intent.putExtra("vehical_state",vehicalListItem.getVehical_state());
                intent.putExtra("vehical_gasetype",vehicalListItem.getVehical_gastype());
               // Snackbar.make(view, vehicalListItem.getVehical_id(), Snackbar.LENGTH_LONG).show();
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    private void setVehicalListRecycle(){
        recycle_vehical_list = findViewById(R.id.recycle_vehical_list);
        vehicalListItemList = new ArrayList<>();
        adapter = new VehicalListAdapter(this,vehicalListItemList);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        recycle_vehical_list.setLayoutManager(layoutManager);
        recycle_vehical_list.setAdapter(adapter);
        recycle_vehical_list.setOnScrollChangeListener(this);
        getVehicalListVolley(cust_id,access_token,requestCount);
    }


    private void getVehicalListVolley(String c_id, String c_accesstoken, final int req_count) {

        String url = UrlConstants.GetAllListVehical+"?CustID="+c_id+"&AccessTokan="
                +c_accesstoken+"&indexNo="+req_count;
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
                                JSONArray jsonArray = jsonObject.getJSONArray("VehicleList");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        VehicalListItem vehicalListItem = new VehicalListItem();
                                        String v_id = jsonObject1.getString("VID");
                                        String v_year = jsonObject1.getString("Year");
                                        String v_make = jsonObject1.getString("Make");
                                        String v_model = jsonObject1.getString("Model");
                                        String v_color = jsonObject1.getString("Color");
                                        String v_plateno = jsonObject1.getString("Tag");
                                        String v_state = jsonObject1.getString("TagState");
                                        String v_gasType = jsonObject1.getString("GasType");

                                        Log.e("v_id", v_id);

                                        vehicalListItem.setVehical_id(v_id);
                                        vehicalListItem.setVehical_year(v_year);
                                        vehicalListItem.setVehical_make(v_make);
                                        vehicalListItem.setVehical_model(v_model);
                                        vehicalListItem.setVehical_color(v_color);
                                        vehicalListItem.setVehical_plateno(v_plateno);
                                        vehicalListItem.setVehical_state(v_state);
                                        vehicalListItem.setVehical_gastype(v_gasType);
                                        vehicalListItemList.add(vehicalListItem);
                                    }


                            }
                            else{
                                progress_bar.setVisibility(View.GONE);
                                if(req_count==0){
                                     //pDialog.dismiss();
                                     Snackbar.make(view, "You don't have any vehicle,please add new vehicle.", Snackbar.LENGTH_LONG).show();
                                }

                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                           // pDialog.dismiss();
                            Log.e("JSONException", String.valueOf(e) );
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.e("Error: ", error.getMessage());
               // pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);
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
        requestCount++;
    }
    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        intent = new Intent(this,Home.class);
        startActivity(intent);
        return true;
    }
    @Override
    public void onBackPressed()
    {
        intent = new Intent(this,Home.class);
        startActivity(intent);
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (isLastItemDisplaying(recycle_vehical_list)) {
            // Log.e("yes ","yes");
            //Calling the method getdata again
            getVehicalListVolley(cust_id,access_token,requestCount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_menu, menu);

        final MenuItem item = menu.findItem(R.id.menu_new);
        MenuItemCompat.setActionView(item, R.layout.menu_text_customlayout);
        LinearLayout ln =  (LinearLayout) MenuItemCompat.getActionView(item);
        TextView text = ln. findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_new) {
            intent = new Intent(this,AddVehical.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}

