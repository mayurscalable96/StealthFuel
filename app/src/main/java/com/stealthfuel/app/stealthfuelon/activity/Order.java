package com.stealthfuel.app.stealthfuelon.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.stealthfuel.app.stealthfuelon.adapter.TimeSlotAdapter;
import com.stealthfuel.app.stealthfuelon.geterseter.TimeSlotListItem;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Order extends AppCompatActivity implements View.OnClickListener{


    //EditText vehical_year,vehical_make,vehical_model,vehical_color,vehical_plate,vehical_state
      //      ,vehical_gastype,vehical_price,vehical_address,comment;

    String vehical_yearText,vehical_makeText,vehical_modelText,vehical_colorText,vehical_plateText,vehical_stateText
            ,vehical_gastypeText,vehical_gastypeIdText,vehical_priceText,vehical_addressText,commentText;
    String vehical_id;
    double latitude,longitude;
    Intent intent;
    TextView toolbartextView;
    androidx.appcompat.widget.Toolbar toolbar;

    Calendar calendar;


    private int mYear, mMonth, mDay, mHour, mMinute;

    int SelectedDate;
    String dateText, timeText;
    Button submit_order;
    View view;
    SharedPrefrenceData sharedPrefrenceData;
    String access_tokene,c_id;
    private ProgressDialog pDialog;
    Spinner spinner_timeslot;
    List<String> listTimeSlot= new ArrayList<String>();
    List<String> listExtraAmount = new ArrayList<String>();
    List<String> listTimeSlotAmount = new ArrayList<String>();
    String ExtraAmountText,TimeSlotText,TimeSlotAmountText;

    RecyclerView recyclerview_timeslot;
    TimeSlotAdapter adapter;
    List<TimeSlotListItem> timeSlotListItems;
    String selected_date;
    String dayOfWeek;

    Button use_fixtime,use_timeslot,submit_fixtime;
    TextView time_picker,date_picker,fixeamount;
    LinearLayout layout_fixtime,layout_timeslot;

    String fixedTimeAmout;
    String TimePickerText,DatePickerText;
    String DeliveryCharge;
    TextView delivery_charge1,delivery_charge2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_WEEK, 6);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                //Log.e("here","here");
                //Log.e("date", String.valueOf(date));
                dayOfWeek = date.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());
                selected_date = DateFormat.format("MM/dd/yyyy", date).toString();
                //Log.e("dayofwek",dayOfWeek);
                Log.e("selected_date",selected_date);
                adapter.getFilter().filter(dayOfWeek);
            }
        });

        calendar = Calendar.getInstance();

        sharedPrefrenceData = new SharedPrefrenceData(this);
        access_tokene = sharedPrefrenceData.getCustomerAccessToken();
        c_id = sharedPrefrenceData.getCustomerId();

        toolbartextView = findViewById(R.id.toolbartextView);
        toolbartextView.setText("Schedule");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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


        view = findViewById(android.R.id.content);


        pDialog = new ProgressDialog(Order.this, R.style.AppCompatAlertDialogStyle);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        //getAllTimeSlot();

        timeSlotRecyclerView();
        fixAmount();
        deliveryAmount();


        use_fixtime = findViewById(R.id.use_fixtime);
        use_timeslot = findViewById(R.id.use_timeslot);
        submit_fixtime = findViewById(R.id.submit_fixtime);
        time_picker = findViewById(R.id.time_picker);
        date_picker = findViewById(R.id.date_picker);
        fixeamount = findViewById(R.id.fixeamount);
        layout_fixtime = findViewById(R.id.layout_fixtime);
        layout_timeslot = findViewById(R.id.layout_timeslot);
        delivery_charge1 = findViewById(R.id.delivery_charge1);
        delivery_charge2 = findViewById(R.id.delivery_charge2);

        use_fixtime.setOnClickListener(this);
        use_timeslot.setOnClickListener(this);
        submit_fixtime.setOnClickListener(this);
        time_picker.setOnClickListener(this);
        date_picker.setOnClickListener(this);

        layout_fixtime.setVisibility(View.GONE);
        layout_timeslot.setVisibility(View.VISIBLE);
        //use_fixtime.setVisibility(View.VISIBLE);
        use_timeslot.setVisibility(View.GONE);

    }

    private void timeSlotRecyclerView(){
        recyclerview_timeslot = findViewById(R.id.recyclerview_timeslot);
        timeSlotListItems = new ArrayList<>();
        adapter = new TimeSlotAdapter(this,timeSlotListItems);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(this);
        recyclerview_timeslot.setLayoutManager(layoutManager);
        recyclerview_timeslot.setAdapter(adapter);
        getTimeSlotByDay();
    }

    private void getTimeSlotByDay(){
        String url = UrlConstants.ALLTIMESLOT+"?AccessTokan="+access_tokene;
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

                                final Calendar defaultSelectedDate = Calendar.getInstance();
                                dayOfWeek = defaultSelectedDate.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());
                                JSONObject jsonObject = response.getJSONObject("Response");
                                JSONArray jsonArray = jsonObject.getJSONArray("TimeSlot");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    TimeSlotListItem timeSlotListItem = new TimeSlotListItem();

                                    String TimeSlot = jsonObject1.getString("TimeSlot");
                                    String ExtraAmount = jsonObject1.getString("Extraamnt");
                                    String TimeSlotAmount = jsonObject1.getString("TimeSlotAmount");
                                    String Day = jsonObject1.getString("Day");
                                    String CutOffTime = jsonObject1.getString("Cuttofftime");
                                    if(dayOfWeek.equalsIgnoreCase(Day)){
                                        Log.e("CutOffTime",CutOffTime);
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
                                        Date currentDate = new Date();
                                        String currentDateTimeString = sdf.format(currentDate);


                                        Date current = null,before=null;
                                        try {
                                            current = sdf.parse(currentDateTimeString);
                                            before = sdf.parse(CutOffTime);


                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Log.e("excep", String.valueOf(e));
                                        }
                                        if(current.before(before) ){
                                            timeSlotListItem.setTimeSlot(TimeSlot);
                                            timeSlotListItem.setExtraAmount(ExtraAmount);
                                            timeSlotListItem.setTimeSlotAmount(TimeSlotAmount);
                                            timeSlotListItem.setDay(Day);
                                            timeSlotListItems.add(timeSlotListItem);
                                        }else{
                                            Log.e("ok","ok");
                                        }
                                    }
                                    else{
                                        timeSlotListItem.setTimeSlot(TimeSlot);
                                        timeSlotListItem.setExtraAmount(ExtraAmount);
                                        timeSlotListItem.setTimeSlotAmount(TimeSlotAmount);
                                        timeSlotListItem.setDay(Day);
                                        timeSlotListItems.add(timeSlotListItem);
                                    }
                                }


                                selected_date = DateFormat.format("MM/dd/yyyy", defaultSelectedDate).toString();
                                Log.e("dayofwek",dayOfWeek);
                                Log.e("selected date",selected_date);
                                adapter.getFilter().filter(dayOfWeek);
                            }
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            pDialog.dismiss();
                            //If an error occurs, this prints the error to the log
                            Log.e("JSONException", String.valueOf(e));
                            e.printStackTrace();
                        }


                       // pDialog.dismiss();
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showSnackbar(){
        Snackbar.make(view, "No available time slot.", Snackbar.LENGTH_LONG).show();
    }

    public void recycleClick(TimeSlotListItem timeSlotListItem ){

        intent = new Intent(Order.this,OrderConfirm.class);
        intent.putExtra("extra_amount",timeSlotListItem.getExtraAmount());
        intent.putExtra("time_slot",timeSlotListItem.getTimeSlot());
        intent.putExtra("time_slotamount",timeSlotListItem.getTimeSlotAmount());

        intent.putExtra("selected_date",selected_date);
        intent.putExtra("vehical_id",vehical_id);
        intent.putExtra("vehical_year",vehical_yearText);
        intent.putExtra("vehical_make",vehical_makeText);
        intent.putExtra("vehical_model",vehical_modelText);
        intent.putExtra("vehical_color",vehical_colorText);
        intent.putExtra("vehical_plate",vehical_plateText);
        intent.putExtra("vehical_state",vehical_stateText);
        intent.putExtra("vehical_gasetype",vehical_gastypeText);
        intent.putExtra("vehical_gasetypeid",vehical_gastypeIdText);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("address",vehical_addressText);
        intent.putExtra("price",vehical_priceText);
        intent.putExtra("delivery_charge",DeliveryCharge);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.use_fixtime:{
                layout_fixtime.setVisibility(View.VISIBLE);
                use_timeslot.setVisibility(View.VISIBLE);
                use_fixtime.setVisibility(View.GONE);
                layout_timeslot.setVisibility(View.GONE);
                break;
            }
            case R.id.use_timeslot:{
                layout_fixtime.setVisibility(View.GONE);
                use_timeslot.setVisibility(View.GONE);
                use_fixtime.setVisibility(View.VISIBLE);
                layout_timeslot.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.submit_fixtime:{
                TimePickerText = time_picker.getText().toString();
                DatePickerText = date_picker.getText().toString();

                if(DatePickerText.equals("Select Date")){
                    Snackbar.make(view, "Select Date", Snackbar.LENGTH_LONG).show();
                }
                else if(TimePickerText.equals("Select Fix Time")){
                    Snackbar.make(view, "Select Fix Time", Snackbar.LENGTH_LONG).show();
                }
                else{
                    intent = new Intent(Order.this,OrderConfirm.class);
                    intent.putExtra("extra_amount",fixedTimeAmout);
                    intent.putExtra("time_slot",TimePickerText);
                    intent.putExtra("time_slotamount",TimePickerText+ " Extra Amount is $"+fixedTimeAmout);

                    intent.putExtra("selected_date",DatePickerText);
                    intent.putExtra("vehical_id",vehical_id);
                    intent.putExtra("vehical_year",vehical_yearText);
                    intent.putExtra("vehical_make",vehical_makeText);
                    intent.putExtra("vehical_model",vehical_modelText);
                    intent.putExtra("vehical_color",vehical_colorText);
                    intent.putExtra("vehical_plate",vehical_plateText);
                    intent.putExtra("vehical_state",vehical_stateText);
                    intent.putExtra("vehical_gasetype",vehical_gastypeText);
                    intent.putExtra("vehical_gasetypeid",vehical_gastypeIdText);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("address",vehical_addressText);
                    intent.putExtra("price",vehical_priceText);
                    intent.putExtra("delivery_charge",DeliveryCharge);
                    startActivity(intent);
                }

                break;
            }
            case R.id.time_picker:{
                selectTime();
                break;
            }
            case R.id.date_picker:{
                selectDate();
                break;
            }
        }
    }

    //String time,date;
    public void selectTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {


                    @Override
                    public void onTimeSet(TimePicker view1, int hourOfDay,
                                          int minute) {
                       // Log.e("CurrentMinute", String.valueOf(mMinute));
                       // Log.e("SelectedMinute", String.valueOf(minute));

                       // Log.e("CurrentHour", String.valueOf(mHour));
                       // Log.e("SelectedHour", String.valueOf(hourOfDay));
                       // time = hourOfDay + ":" + minute;
                        boolean isPM = (hourOfDay >= 12);


                        if(mDay == SelectedDate) {

                            if(hourOfDay - mHour > 1){

                                time_picker.setText(String.format("%d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                            }
                            else if(hourOfDay - mHour == 1){
                                if(minute >= mMinute){
                                    time_picker.setText(String.format("%d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                                }
                                else  if(minute < mMinute){
                                    Snackbar.make(view, "Select 1 hour ahead time.", Snackbar.LENGTH_LONG).show();
                                    //Toast.makeText(getApplicationContext(), "Select 1 hour ahead time", Toast.LENGTH_SHORT).show();
                                }
                            }

                            else if(hourOfDay - mHour < 1){
                                Snackbar.make(view, "Select 1 hour ahead time.", Snackbar.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "Select 1 hour ahead time", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            time_picker.setText(String.format("%d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));
                        }


                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public void selectDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog=new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        SelectedDate = dayOfMonth;
                        Log.e("Current Date", String.valueOf(mDay));
                        Log.e("Selected Date", String.valueOf(SelectedDate));

                        //date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.YEAR, year);
                        Date myDate = cal.getTime();



                        date_picker.setText(new SimpleDateFormat("MM/dd/yyyy").format(myDate));
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void fixAmount() {

        String url = UrlConstants.FIXEDTIMEAMOUNT+"?AccessTokan="+access_tokene;
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

                                //pDialog.dismiss();
                                // Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();

                                JSONArray jsonArray = jsonObject.getJSONArray("FixedTimeAmount");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                fixedTimeAmout = jsonObject1.getString("FixedTimeAmount");
                                fixeamount.setText("If you are using on demand request you pay extra amount that is $"+fixedTimeAmout+".");



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


    private void deliveryAmount() {

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
                                delivery_charge1.setText("Delivery charge is $"+DeliveryCharge+".");
                                delivery_charge2.setText("Delivery charge is $"+DeliveryCharge+".");

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

}
