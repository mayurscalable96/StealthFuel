package com.stealthfuel.app.stealthfuelon.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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
import com.stealthfuel.app.stealthfuelon.adapter.OrderListAdapter;
import com.stealthfuel.app.stealthfuelon.geterseter.OrderListItem;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OrderList extends AppCompatActivity  implements RecyclerView.OnScrollChangeListener{

    RecyclerView recyclerview_orderlist;
    OrderListAdapter adapter;
    List<OrderListItem> orderListItem;
    SharedPrefrenceData sharedPrefrenceData;
    String cust_id,access_token;
    View view ;
    private int requestCount = 0;
    ProgressBar progress_bar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

       // LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // inflate your activity layout here!
      //  View contentView = inflater.inflate(R.layout.activity_order_list, null, false);
      //  drawer.addView(contentView, 0);
      //  toolbartextView.setText("Orders");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Orders");


        sharedPrefrenceData = new SharedPrefrenceData(this);
        cust_id = sharedPrefrenceData.getCustomerId();
        access_token = sharedPrefrenceData.getCustomerAccessToken();

        view = findViewById(android.R.id.content);
        progress_bar = findViewById(R.id.progress_bar);
        setOrderRecycleView();

       /* recyclerview_orderlist.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerview_orderlist, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OrderListItem orderListItem1 = orderListItem.get(position);
                Intent intent = new Intent(OrderList.this,OrderParticular.class);
                intent.putExtra("invoice_id",orderListItem1.getInvoice_id());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/


    }






    private void setOrderRecycleView(){
        recyclerview_orderlist = findViewById(R.id.recyclerview_orderlist);
        orderListItem = new ArrayList<>();
        adapter = new OrderListAdapter(this,orderListItem);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        recyclerview_orderlist.setLayoutManager(layoutManager);
        recyclerview_orderlist.setAdapter(adapter);
        recyclerview_orderlist.setOnScrollChangeListener(this);
        getOrderListVolley(cust_id,access_token,requestCount);
    }



    private void getOrderListVolley(String c_id, String c_accesstoken, final int req_count) {

        String url = UrlConstants.ORDERLIST+"?CustID="+c_id+"&AccessTokan="
                +c_accesstoken+"&indexNo="+req_count;
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

                                JSONArray jsonArray = jsonObject.getJSONArray("OrderList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    OrderListItem orderListItemList = new OrderListItem();
                                    String invoice_no = jsonObject1.getString("InvoiceNo");
                                    String order_id = jsonObject1.getString("OrderID");
                                    String order_date = jsonObject1.getString("Date");
                                    String order_time = jsonObject1.getString("Time");
                                    String order_address = jsonObject1.getString("Address");
                                    String order_comment = jsonObject1.getString("Comment");
                                    String order_status = jsonObject1.getString("orderstatus");
                                    String payment_status = jsonObject1.getString("PaymentStatus");

                                    String v_id = jsonObject1.getString("VID");
                                    String v_year = jsonObject1.getString("Year");
                                    String v_make = jsonObject1.getString("Make");
                                    String v_model = jsonObject1.getString("Model");
                                    String v_color = jsonObject1.getString("Color");
                                    String v_plateno = jsonObject1.getString("Tag");
                                    String v_state = jsonObject1.getString("TagState");
                                    String gasType = jsonObject1.getString("GasType");
                                    String gasprice = jsonObject1.getString("Price");
                                    String time_slot = jsonObject1.getString("TimeSlot");
                                    String extra_amount = jsonObject1.getString("ExtraAmount");


                                    orderListItemList.setInvoice_id(invoice_no);
                                    orderListItemList.setOrder_id(order_id);
                                    orderListItemList.setOrder_date(order_date);
                                    orderListItemList.setOrder_time(order_time);
                                    orderListItemList.setOrder_address(order_address);
                                    orderListItemList.setOrder_comment(order_comment);
                                    orderListItemList.setVehicle_year(v_year);
                                    orderListItemList.setOrder_status(order_status);
                                    orderListItemList.setOrder_paymentstatus(payment_status);
                                    orderListItemList.setVehicle_year(v_year);
                                    orderListItemList.setVehicle_make(v_make);
                                    orderListItemList.setVehicle_model(v_model);
                                    orderListItemList.setVehicle_color(v_color);
                                    orderListItemList.setVehicle_plateno(v_plateno);
                                    orderListItemList.setVehicle_state(v_state);
                                    orderListItemList.setGas_type(gasType);
                                    orderListItemList.setGas_price(gasprice);
                                    orderListItemList.setTime_slot(time_slot);
                                    orderListItemList.setExtra_amount(extra_amount);
                                    orderListItem.add(orderListItemList);
                                }

                            }
                            else{
                                progress_bar.setVisibility(View.GONE);
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                if(req_count==0){
                                    Snackbar.make(view, ErrorMessage, Snackbar.LENGTH_LONG).show();
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
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
        return true;
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,Home.class);
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
        if (isLastItemDisplaying(recyclerview_orderlist)) {
            // Log.e("yes ","yes");
            //Calling the method getdata again
            getOrderListVolley(cust_id,access_token,requestCount);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
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
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recycleClick(OrderListItem orderListItem1){
        Intent intent = new Intent(OrderList.this,OrderParticular.class);
        intent.putExtra("invoice_id",orderListItem1.getInvoice_id());

        startActivity(intent);
    }
}
