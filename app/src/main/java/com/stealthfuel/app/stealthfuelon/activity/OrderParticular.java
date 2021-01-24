package com.stealthfuel.app.stealthfuelon.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
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

import java.text.DecimalFormat;

public class OrderParticular extends AppCompatActivity {

    Intent intent;
    String invoice_idText,order_idText,order_addressText,order_dateText,order_timeText,order_statusText;
    String vehicle_yearText,vehicle_makeText,vehicle_modelText,vehicle_plateText,vehicle_stateText,vehicle_colorText;
    String gastypeText,gaspriceText;
    String delivery_boynameText,delivery_boymobText;
    String gas_quantityText,gas_totalamountText;
    String order_delivereddateText;
    String commentText;
    String ratingText,ratingDescriptionText;
    String timeSlotText,extraAmountText;
    String deliveryChargesText;

    TextView  order_id,order_address,order_date,order_status;
    TextView order_vehicle;
    TextView gastype,gasprice;
    TextView delivery_boyname,delivery_boymob;
    TextView gas_quantity,gas_totalamount;
    TextView order_delivereddate;
    TextView comment;
    TextView order_timeslot;

    private ProgressDialog pDialog;
    androidx.appcompat.widget.Toolbar toolbar;
    TextView toolbartextView;
    View view;
    SharedPrefrenceData sharedPrefrenceData;
    String access_tokene,custid;
    Button cancel_order;

    LinearLayout order_layout,deliveryboy_layout,gasfilled_layout,comment_layout;
    LinearLayout customer_moblayout;
    LinearLayout ratinginsert_layout,ratingshow_layout;
    LinearLayout delivereddate_layout;
    LinearLayout charges_layout;
    LinearLayout orderparticular_mainlayout;
    RatingBar ratingBar_insert,ratingBar_show;
    EditText ratingdescription_insert;
    TextView ratingdescription_show;
    TextView extra_ammount,delivery_charge;
    TextView total_gasprice;
    //TextView e_ammount;
    TextView d_charge;
    Button submit_rating;
    String ratingBar_insertText,ratingdescription_insertText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_particular);


        intent = getIntent();
        invoice_idText = intent.getStringExtra("invoice_id");

       /* order_idText = intent.getStringExtra("order_id");
        order_addressText = intent.getStringExtra("order_address");
        order_dateText = intent.getStringExtra("order_date");
        order_timeText = intent.getStringExtra("order_time");
        order_statusText = intent.getStringExtra("order_status");

        vehicle_yearText = intent.getStringExtra("vehicle_year");
        vehicle_makeText = intent.getStringExtra("vehicle_make");
        vehicle_modelText = intent.getStringExtra("vehicle_model");
        vehicle_plateText = intent.getStringExtra("vehicle_plate");
        vehicle_colorText = intent.getStringExtra("vehicle_color");
        vehicle_stateText = intent.getStringExtra("vehicle_state");
        gastypeText = intent.getStringExtra("gasetype");
        gaspriceText = intent.getStringExtra("gaseprice");*/

        order_id = findViewById(R.id.order_id);
        order_address = findViewById(R.id.order_address);
        order_date = findViewById(R.id.order_date);
        order_status = findViewById(R.id.order_status);
        order_vehicle = findViewById(R.id.order_vehicle);
        gastype = findViewById(R.id.gastype);
        gasprice = findViewById(R.id.gasprice);
        delivery_boyname = findViewById(R.id.delivery_boyname);
        delivery_boymob = findViewById(R.id.delivery_boymob);
        gas_quantity = findViewById(R.id.gas_quantity);
        gas_totalamount = findViewById(R.id.gas_totalamount);
        order_delivereddate = findViewById(R.id.order_delivereddate);
        comment = findViewById(R.id.comment);
        order_timeslot = findViewById(R.id.order_timeslot);
        extra_ammount = findViewById(R.id.extra_ammount);
        delivery_charge = findViewById(R.id.delivery_charge);
        //e_ammount = findViewById(R.id.e_ammount);
        d_charge = findViewById(R.id.d_charge);

        /*order_id.setText(order_idText);
        order_address.setText(order_addressText);
        order_date.setText(order_dateText+" "+order_timeText);
        order_status.setText(order_statusText);
        order_vehicle.setText(vehicle_makeText+ " "+ vehicle_modelText+" "+vehicle_colorText+" "+vehicle_plateText);
        gastype.setText(gastypeText);
        gasprice.setText("$"+gaspriceText);*/

        sharedPrefrenceData = new SharedPrefrenceData(this);
        access_tokene = sharedPrefrenceData.getCustomerAccessToken();
        custid = sharedPrefrenceData.getCustomerId();

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Order Detail");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cancel_order = findViewById(R.id.cancel_order);

        order_layout = findViewById(R.id.order_layout);
        deliveryboy_layout = findViewById(R.id.deliveryboy_layout);
        gasfilled_layout = findViewById(R.id.gasfilled_layout);
        comment_layout = findViewById(R.id.comment_layout);
        ratinginsert_layout = findViewById(R.id.ratinginsert_layout);
        ratingshow_layout = findViewById(R.id.ratingshow_layout);
        delivereddate_layout = findViewById(R.id.delivereddate_layout);
        charges_layout = findViewById(R.id.charges_layout);
        total_gasprice = findViewById(R.id.total_gasprice);
        orderparticular_mainlayout = findViewById(R.id.orderparticular_mainlayout);

        cancel_order.setVisibility(View.GONE);
        order_layout.setVisibility(View.GONE);
        deliveryboy_layout.setVisibility(View.GONE);
        gasfilled_layout.setVisibility(View.GONE);
        comment_layout.setVisibility(View.GONE);
        ratinginsert_layout.setVisibility(View.GONE);
        ratingshow_layout.setVisibility(View.GONE);
        delivereddate_layout.setVisibility(View.GONE);
        charges_layout.setVisibility(View.GONE);

        ratingBar_insert = findViewById(R.id.ratingBar_insert);
        ratingBar_show = findViewById(R.id.ratingBar_show);

        ratingdescription_insert = findViewById(R.id.ratingdescription_insert);
        ratingdescription_show = findViewById(R.id.ratingdescription_show);

        submit_rating = findViewById(R.id.submit_rating);
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar_insertText  = String.valueOf(ratingBar_insert.getRating());
                ratingdescription_insertText = ratingdescription_insert.getText().toString();
                if(ratingBar_insertText.equalsIgnoreCase("0.0")){
                    Snackbar.make(view, "Please give rating", Snackbar.LENGTH_LONG).show();

                }
                else{
                    pDialog = new ProgressDialog(OrderParticular.this, R.style.AppCompatAlertDialogStyle);
                    // Showing progress dialog before making http request
                    pDialog.setMessage("Loading...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                   // Log.e("rateng",ratingBar_insertText);
                    insertRatingVolley();
                }

            }
        });


        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPopup();
            }
        });

        view = findViewById(android.R.id.content);
        customer_moblayout = findViewById(R.id.customer_moblayout);
        customer_moblayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+delivery_boymobText));
                startActivity(intent);
            }
        });

        pDialog = new ProgressDialog(OrderParticular.this, R.style.AppCompatAlertDialogStyle);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        orderDetailVolley();


    }

    private void orderDetailVolley() {

        String url = UrlConstants.ORDERDETAIL+"?AccessTokan="+access_tokene+"&InvoiceNo="+invoice_idText;

        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Status = response.getString("Status");
                            String OrderStatus = response.getString("OrderStatus");

                            if(Status.equalsIgnoreCase("Success")){
                                JSONObject jsonObject = response.getJSONObject("Response");
                                if(OrderStatus.equalsIgnoreCase("CancelOrder")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("CancelOrder");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                     order_idText = jsonObject1.getString("OrderID");
                                     order_addressText = jsonObject1.getString("Address");
                                     order_dateText = jsonObject1.getString("Date");
                                     order_timeText = jsonObject1.getString("Time");
                                     order_statusText = jsonObject1.getString("orderstatus");
                                     vehicle_makeText = jsonObject1.getString("Make");
                                     vehicle_modelText = jsonObject1.getString("Model");
                                     vehicle_colorText = jsonObject1.getString("Color");
                                     vehicle_plateText = jsonObject1.getString("Tag");
                                     gastypeText = jsonObject1.getString("GasType");
                                     gaspriceText = jsonObject1.getString("Price");
                                     timeSlotText = jsonObject1.getString("TimeSlot");
                                     extraAmountText = jsonObject1.getString("ExtraAmnt");
                                     deliveryChargesText = jsonObject1.getString("DeliveryCharge");

                                     delivery_charge.setText("$"+deliveryChargesText);
                                     extra_ammount.setText("$"+extraAmountText);
                                     gasprice.setText("$"+gaspriceText);

                                    if(extraAmountText.equals("0")){
                                        order_timeslot.setText(timeSlotText);
                                    }
                                    else{
                                        order_timeslot.setText(timeSlotText+"Extra amount is $"+extraAmountText);
                                    }

                                    commentText = jsonObject1.getString("Comment");
                                    if(commentText.equalsIgnoreCase("")){
                                        comment_layout.setVisibility(View.GONE);
                                    }
                                    else{
                                        comment.setText(commentText);
                                        comment_layout.setVisibility(View.VISIBLE);
                                    }


                                    // Log.e("order_idText",order_idText);

                                     order_id.setText("#"+order_idText);
                                     order_address.setText(order_addressText);
                                     order_date.setText(order_dateText+" "+order_timeText);
                                     order_status.setText(order_statusText);
                                     order_vehicle.setText(vehicle_makeText + " " + vehicle_modelText + " " + vehicle_colorText
                                             +" "+vehicle_plateText);

                                     gastype.setText(gastypeText);




                                     cancel_order.setVisibility(View.GONE);

                                     order_layout.setVisibility(View.VISIBLE);
                                     deliveryboy_layout.setVisibility(View.GONE);
                                     gasfilled_layout.setVisibility(View.GONE);
                                     delivereddate_layout.setVisibility(View.GONE);
                                     charges_layout.setVisibility(View.VISIBLE);
                                }
                                else if(OrderStatus.equalsIgnoreCase("ConfirmOrder")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("ConfirmOrder");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    order_idText = jsonObject1.getString("OrderID");
                                    order_addressText = jsonObject1.getString("Address");
                                    order_dateText = jsonObject1.getString("Date");
                                    order_timeText = jsonObject1.getString("Time");
                                    order_statusText = jsonObject1.getString("orderstatus");

                                    vehicle_makeText = jsonObject1.getString("Make");
                                    vehicle_modelText = jsonObject1.getString("Model");
                                    vehicle_colorText = jsonObject1.getString("Color");
                                    vehicle_plateText = jsonObject1.getString("Tag");

                                    gastypeText = jsonObject1.getString("GasType");
                                    gaspriceText = jsonObject1.getString("Price");

                                    timeSlotText = jsonObject1.getString("TimeSlot");
                                    extraAmountText = jsonObject1.getString("ExtraAmnt");
                                    deliveryChargesText = jsonObject1.getString("DeliveryCharge");

                                    delivery_charge.setText("$"+deliveryChargesText);
                                    extra_ammount.setText("$"+extraAmountText);
                                    gasprice.setText("$"+gaspriceText);

                                    if(extraAmountText.equals("0")){
                                        order_timeslot.setText(timeSlotText);
                                    }
                                    else{
                                        order_timeslot.setText(timeSlotText+"Extra amount is $"+extraAmountText);
                                    }

                                    commentText = jsonObject1.getString("Comment");

                                    if(commentText.equalsIgnoreCase("")){
                                        comment_layout.setVisibility(View.GONE);
                                    }
                                    else{
                                        comment.setText(commentText);
                                        comment_layout.setVisibility(View.VISIBLE);
                                    }

                                  //  Log.e("order_idText",order_idText);

                                    order_id.setText("#"+order_idText);
                                    order_address.setText(order_addressText);
                                    order_date.setText(order_dateText+" "+order_timeText);
                                    order_status.setText(order_statusText);
                                    order_vehicle.setText(vehicle_makeText + " " + vehicle_modelText + " " + vehicle_colorText
                                            +" "+vehicle_plateText);
                                    gastype.setText(gastypeText);
                                    order_timeslot.setText(timeSlotText);

                                    cancel_order.setVisibility(View.VISIBLE);
                                    order_layout.setVisibility(View.VISIBLE);
                                    deliveryboy_layout.setVisibility(View.GONE);
                                    gasfilled_layout.setVisibility(View.GONE);
                                    delivereddate_layout.setVisibility(View.GONE);
                                    charges_layout.setVisibility(View.VISIBLE);

                                }
                                else if(OrderStatus.equalsIgnoreCase("PendingOrder")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("PendingOrder");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    order_idText = jsonObject1.getString("OrderID");
                                    order_addressText = jsonObject1.getString("Address");
                                    order_dateText = jsonObject1.getString("Date");
                                    order_timeText = jsonObject1.getString("Time");
                                    order_statusText = jsonObject1.getString("orderstatus");

                                    vehicle_makeText = jsonObject1.getString("Make");
                                    vehicle_modelText = jsonObject1.getString("Model");
                                    vehicle_colorText = jsonObject1.getString("Color");
                                    vehicle_plateText = jsonObject1.getString("Tag");

                                    gastypeText = jsonObject1.getString("GasType");
                                    gaspriceText = jsonObject1.getString("Price");

                                    delivery_boynameText = jsonObject1.getString("DeliveyBoyName");
                                    delivery_boymobText = jsonObject1.getString("DeliveryBoyMobileNo");
                                    timeSlotText = jsonObject1.getString("TimeSlot");
                                    extraAmountText = jsonObject1.getString("ExtraAmnt");
                                    deliveryChargesText = jsonObject1.getString("DeliveryCharge");

                                    delivery_charge.setText("$"+deliveryChargesText);
                                    extra_ammount.setText("$"+extraAmountText);
                                    gasprice.setText("$"+gaspriceText);

                                    if(extraAmountText.equals("0")){
                                        order_timeslot.setText(timeSlotText);
                                    }
                                    else{
                                        order_timeslot.setText(timeSlotText+"Extra amount is $"+extraAmountText);
                                    }

                                    commentText = jsonObject1.getString("Comment");
                                    if(commentText.equalsIgnoreCase("")){
                                        comment_layout.setVisibility(View.GONE);
                                    }
                                    else{
                                        comment.setText(commentText);
                                        comment_layout.setVisibility(View.VISIBLE);
                                    }

                                    //  Log.e("order_idText",order_idText);

                                    order_id.setText("#"+order_idText);
                                    order_address.setText(order_addressText);
                                    order_date.setText(order_dateText+" "+order_timeText);
                                    order_status.setText(order_statusText);
                                    order_vehicle.setText(vehicle_makeText + " " + vehicle_modelText + " " + vehicle_colorText
                                            +" "+vehicle_plateText);

                                    gastype.setText(gastypeText);
                                    order_timeslot.setText(timeSlotText);

                                    delivery_boyname.setText(delivery_boynameText);
                                    delivery_boymob.setText(delivery_boymobText);

                                    cancel_order.setVisibility(View.GONE);
                                    order_layout.setVisibility(View.VISIBLE);
                                    deliveryboy_layout.setVisibility(View.VISIBLE);
                                    gasfilled_layout.setVisibility(View.GONE);
                                    delivereddate_layout.setVisibility(View.GONE);
                                    charges_layout.setVisibility(View.VISIBLE);


                                }

                                else if(OrderStatus.equalsIgnoreCase("CompleteOrder")){
                                    JSONArray jsonArray = jsonObject.getJSONArray("CompleteOrder");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                    order_idText = jsonObject1.getString("OrderID");
                                    order_addressText = jsonObject1.getString("Address");
                                    order_dateText = jsonObject1.getString("Date");
                                    order_timeText = jsonObject1.getString("Time");
                                    order_statusText = jsonObject1.getString("orderstatus");

                                    vehicle_makeText = jsonObject1.getString("Make");
                                    vehicle_modelText = jsonObject1.getString("Model");
                                    vehicle_colorText = jsonObject1.getString("Color");
                                    vehicle_plateText = jsonObject1.getString("Tag");

                                    gastypeText = jsonObject1.getString("GasType");
                                    gaspriceText = jsonObject1.getString("Price");

                                    delivery_boynameText = jsonObject1.getString("DeliveyBoyName");
                                    delivery_boymobText = jsonObject1.getString("DeliveryBoyMobileNo");

                                    gas_quantityText = jsonObject1.getString("Qty");
                                    gas_totalamountText = jsonObject1.getString("TotalAmount");

                                    order_delivereddateText = jsonObject1.getString("CompleteDate");

                                    ratingText = jsonObject1.getString("Rating");
                                    ratingDescriptionText = jsonObject1.getString("RatingDescription");
                                    timeSlotText = jsonObject1.getString("TimeSlot");
                                    extraAmountText = jsonObject1.getString("ExtraAmnt");
                                    deliveryChargesText = jsonObject1.getString("DeliveryCharge");

                                    delivery_charge.setText("$"+deliveryChargesText);
                                    extra_ammount.setText("$"+extraAmountText);
                                    gasprice.setText("$"+gaspriceText);

                                    if(extraAmountText.equals("0")){
                                        order_timeslot.setText(timeSlotText);
                                    }
                                    else{
                                        order_timeslot.setText(timeSlotText+"Extra amount is $"+extraAmountText);
                                    }

                                    commentText = jsonObject1.getString("Comment");
                                    if(commentText.equalsIgnoreCase("")){
                                        comment_layout.setVisibility(View.GONE);
                                    }
                                    else{
                                        comment.setText(commentText);
                                        comment_layout.setVisibility(View.VISIBLE);
                                    }

                                    //  Log.e("order_idText",order_idText);

                                    order_id.setText("#"+order_idText);
                                    order_address.setText(order_addressText);
                                    order_date.setText(order_dateText+" "+order_timeText);
                                    order_status.setText(order_statusText);
                                    order_vehicle.setText(vehicle_makeText + " " + vehicle_modelText + " " + vehicle_colorText
                                            +" "+vehicle_plateText);
                                    gastype.setText(gastypeText);
                                    order_timeslot.setText(timeSlotText);

                                    delivery_boyname.setText(delivery_boynameText);
                                    delivery_boymob.setText(delivery_boymobText);
                                    gas_quantity.setText(gas_quantityText+" Gallon");
                                    gas_totalamount.setText("$"+gas_totalamountText);
                                    order_delivereddate.setText(order_delivereddateText);
                                    if(ratingText.equalsIgnoreCase("")){
                                        ratinginsert_layout.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        ratingBar_show.setRating(Float.parseFloat(ratingText));
                                        ratingdescription_show.setText(ratingDescriptionText);
                                        ratingshow_layout.setVisibility(View.VISIBLE);
                                    }

                                    total_gasprice.setText("(Quantity "+gas_quantityText+" * Gas Price $"+gaspriceText+
                                            ")+Extra Amount $"+extraAmountText+" ="+gas_totalamountText);

                                   // e_ammount.setText("$"+extraAmountText);
                                    d_charge.setText("$"+deliveryChargesText);
                                    double total =Double.parseDouble(deliveryChargesText)+ Double.parseDouble(gas_totalamountText);
                                      Log.e("total", String.valueOf(total));
                                    total = Double.parseDouble(new DecimalFormat("##.##").format(total));
                                    gas_totalamount.setText("$"+total);

                                    cancel_order.setVisibility(View.GONE);
                                    order_layout.setVisibility(View.VISIBLE);
                                    deliveryboy_layout.setVisibility(View.VISIBLE);
                                    gasfilled_layout.setVisibility(View.VISIBLE);
                                    delivereddate_layout.setVisibility(View.VISIBLE);
                                    charges_layout.setVisibility(View.VISIBLE);

                                }

                                pDialog.dismiss();
                                //Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                             //   Intent intent = new Intent(OrderParticular.this,OrderList.class);
                               // startActivity(intent);
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


    private void showPopup() {
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.cancel_order_popup, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(
                layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        popup.setWidth(width-200);
        //popup.setAnimationStyle(R.style.Animation);
        // popup.showAtLocation(home_linearlayout, Gravity.CENTER,0,0);
        orderparticular_mainlayout.post(new Runnable() {
            public void run() {
                popup.showAtLocation(orderparticular_mainlayout, Gravity.CENTER, 0, 0);
                dimBehind(popup);
            }
        });
        TextView cancelorder_detail = layout.findViewById(R.id.cancelorder_detail);
        cancelorder_detail.setText("If you cancel the order, your $"+ deliveryChargesText+ " delivery amount will not be refunded.");

        Button cancel =layout. findViewById(R.id.cancel);
        Button ok =layout. findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(OrderParticular.this, R.style.AppCompatAlertDialogStyle);
                // Showing progress dialog before making http request
                pDialog.setMessage("Loading...");
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                cancelOrderVolley();
            }
        });


    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

    private void cancelOrderVolley() {

        String url = UrlConstants.CANCELORDER+"?AccessTokan="+access_tokene+"&CustID="
                +custid+"&InvoiceNo="+invoice_idText;
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
                                //Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(OrderParticular.this,OrderList.class);
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

    private void insertRatingVolley() {

        String url = UrlConstants.INSERTRATING+"?AccessToken="+access_tokene+"&Rating="
                +ratingBar_insertText+"&Description="+ratingdescription_insertText+"&InvoiceNo="+invoice_idText;
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
                                finish();
                                startActivity(getIntent());

                            }
                            else{
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
