package com.stealthfuel.app.stealthfuelon.activity;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.stealthfuel.app.stealthfuelon.adapter.PromotionAdapter;
import com.stealthfuel.app.stealthfuelon.geterseter.PromotionsItem;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Promotions extends NavigationDrawerActivity implements RecyclerView.OnScrollChangeListener{

    RecyclerView recycle_promotionhistory;
    PromotionAdapter adapter;
    List<PromotionsItem> promotionsItemList;
    SharedPrefrenceData sharedPrefrenceData;
    String cust_id,access_token;
    View view ;
    private int requestCount = 0;
    ProgressBar progress_bar;
    TextView promotion_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_promotions);

         LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate your activity layout here!
         View contentView = inflater.inflate(R.layout.activity_promotions, null, false);
         drawer.addView(contentView, 0);
         toolbartextView.setText("Promotions");

        sharedPrefrenceData = new SharedPrefrenceData(this);
        cust_id = sharedPrefrenceData.getCustomerId();
        access_token = sharedPrefrenceData.getCustomerAccessToken();

        promotion_total = findViewById(R.id.promotion_total);
        promotion_total.setVisibility(View.GONE);
        view = findViewById(android.R.id.content);
        progress_bar = findViewById(R.id.progress_bar);
        setRewardRecycleView();
    }

    private void setRewardRecycleView(){
        recycle_promotionhistory = findViewById(R.id.recycle_promotionhistory);
        promotionsItemList = new ArrayList<>();
        adapter = new PromotionAdapter(this,promotionsItemList);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        recycle_promotionhistory.setLayoutManager(layoutManager);
        recycle_promotionhistory.setAdapter(adapter);
        recycle_promotionhistory.setOnScrollChangeListener(this);
        getRewardListVolley(cust_id,access_token,requestCount);
    }

    private void getRewardListVolley(String c_id, String c_accesstoken, final int req_count) {
        String url = UrlConstants.REWARDHISTORY+"?CustID="+c_id+"&AccessToken="
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
                                String Total_amount = response.getString("CustomerTotalBalance");
                                promotion_total.setVisibility(View.VISIBLE);
                                promotion_total.setText("Your total reward is $"+Total_amount+".");
                                JSONArray jsonArray = jsonObject.getJSONArray("ReferalList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    PromotionsItem promotionsItem = new PromotionsItem();
                                    String order_id = jsonObject1.getString("OrderID");
                                    String refral_amount = jsonObject1.getString("ReferalAmount");
                                    String refral_date = jsonObject1.getString("RefDate");


                                    promotionsItem.setHistory("You got $" +refral_amount + " on this "+refral_date
                                            + " for #"+order_id);

                                    promotionsItemList.add(promotionsItem);
                                }

                            }
                            else{
                                progress_bar.setVisibility(View.GONE);
                                // pDialog.dismiss();
                                // Snackbar.make(view, Status, Snackbar.LENGTH_LONG).show();
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
        if (isLastItemDisplaying(recycle_promotionhistory)) {
            // Log.e("yes ","yes");
            //Calling the method getdata again
            getRewardListVolley(cust_id,access_token,requestCount);
        }
    }
}
