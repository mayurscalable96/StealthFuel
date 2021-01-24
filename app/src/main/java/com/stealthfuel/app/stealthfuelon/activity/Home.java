package com.stealthfuel.app.stealthfuelon.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;

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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.stealthfuel.app.stealthfuelon.adapter.PlaceArrayAdapter;
import com.stealthfuel.app.stealthfuelon.geterseter.VehicalListItem;
import com.stealthfuel.app.stealthfuelon.other.SharedPrefrenceData;
import com.stealthfuel.app.stealthfuelon.other.UrlConstants;
import com.google.android.gms.location.LocationListener;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stealthfuel.app.stealthfuelon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home extends NavigationDrawerActivity implements OnMapReadyCallback ,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LinearLayout home_linearlayout;
    MarkerOptions markerOptions = new MarkerOptions();
  //  private AutoCompleteTextView delivery_address;
    String yourAddress;
    List<Address> addressList;
    LatLng latLong;
    Spinner spinner_maptype;
    List<String> listMapType = new ArrayList<>();
    List<String> listVehicles = new ArrayList<>();


    List<VehicalListItem>  vehicalListItemList = new ArrayList<>() ;


    TextView gas_price,gas_type;
    Spinner spinner_vehicles;
    View view;

    SharedPrefrenceData sharedPrefrenceData;
    String c_id,c_accesstoken;
    Button next;
    String vehical_yearText,vehical_makeText,vehical_modelText,vehical_colorText,vehical_plateText
            ,vehical_stateText,vehical_gasText,vehical_idText,vehical_gastypeidText;
    double latitude,longitude;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    HashMap<String,String> hashMap = new HashMap<>();
    String zip;
    private ProgressDialog pDialog;
    String gas_priceText;
    String BusinessHourFromTime,BusinessHourToTime;
    AutocompleteSupportFragment delivery_address;
    String apiKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!

        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);
        toolbartextView.setText("Stealth Fuel");



        sharedPrefrenceData = new SharedPrefrenceData(this);
        c_id = sharedPrefrenceData.getCustomerId();
        c_accesstoken = sharedPrefrenceData.getCustomerAccessToken();


        gas_price = findViewById(R.id.gas_price);
        gas_type = findViewById(R.id.gas_type);

        view = findViewById(android.R.id.content);
        spinner_vehicles = findViewById(R.id.spinner_vehicles);

        home_linearlayout = findViewById(R.id.home_linearlayout);

        pDialog = new ProgressDialog(Home.this, R.style.AppCompatAlertDialogStyle);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        getAllGasPrice();

        // showPopup();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkLocationPermission();
        }

     //   delivery_address = findViewById(R.id.delivery_address);
      //  delivery_address.setThreshold(1);
        /*delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delivery_address.setText("");
            }
        });*/


        addItemsOnSpinner();

        spinner_maptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              /*  if(position==0){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }*/
                String selected_item = spinner_maptype.getSelectedItem().toString();
                switch (selected_item) {
                    case "Map":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case "Satellite":
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "Hybrid":
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "Terrain":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        spinner_vehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                VehicalListItem vehicalListItem = vehicalListItemList.get(position);
                vehical_idText = vehicalListItem.getVehical_id();
                vehical_yearText = vehicalListItem.getVehical_year();
                vehical_makeText = vehicalListItem.getVehical_make();
                vehical_modelText = vehicalListItem.getVehical_model();
                vehical_colorText = vehicalListItem.getVehical_color();
                vehical_plateText = vehicalListItem.getVehical_plateno();
                vehical_stateText = vehicalListItem.getVehical_state();
                vehical_gasText = vehicalListItem.getVehical_gastype();
                vehical_gastypeidText = vehicalListItem.getVehical_gastypeid();
                gas_type.setText(vehical_gasText);
                if(hashMap.containsKey(zip+vehical_gasText)){
                    gas_priceText = hashMap.get(zip+vehical_gasText);
                    gas_price.setText("$"+gas_priceText);
                }
                else{
                    gas_priceText = "0";
                   // Log.e("no value","novalue");
                    gas_price.setText("$"+gas_priceText);
                    Snackbar.make(view, "Currently service is not available for this location.", Snackbar.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listVehicles.size()<=0){
                    showPopup();
                }
                else if(gas_priceText.equals("0")){
                    Snackbar.make(view, "Currently service is not available for this location.", Snackbar.LENGTH_LONG).show();
                }
                else if(!checkBusinessHours()){
                    showPopupBusinessHour();
                }
                else if(delivery_address.a.getText().toString().length()<=0){
                    Snackbar.make(view, "Select location.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    intent = new Intent(Home.this,Order.class);
                    intent.putExtra("vehical_id",vehical_idText);
                    intent.putExtra("vehical_year",vehical_yearText);
                    intent.putExtra("vehical_make",vehical_makeText);
                    intent.putExtra("vehical_model",vehical_modelText);
                    intent.putExtra("vehical_color",vehical_colorText);
                    intent.putExtra("vehical_plate",vehical_plateText);
                    intent.putExtra("vehical_state",vehical_stateText);
                    intent.putExtra("vehical_gasetype",vehical_gasText);
                    intent.putExtra("vehical_gasetypeid",vehical_gastypeidText);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("address",yourAddress);
                    intent.putExtra("price",gas_priceText);
                    startActivity(intent);
                }
            }


        });

       // delivery_address.setOnItemClickListener(mAutocompleteClickListener);
      //  mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
        //        BOUNDS_MOUNTAIN_VIEW, null);
        //delivery_address.setAdapter(mPlaceArrayAdapter);
        //delivery_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delivery_address.setText("");
//                delivery_address.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
//        });
//
//
//        delivery_address.setDropDownHeight(0);

        getBusinessHours();

        apiKey = getString(R.string.apiKey);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
         delivery_address = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        delivery_address.a.setTextSize(14f);
        delivery_address.a.setTextColor(getResources().getColor(R.color.grey));
        delivery_address.a.setTypeface( ResourcesCompat.getFont(this, R.font.calibri));
        delivery_address.a.setGravity(Gravity.CENTER);
        delivery_address.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        delivery_address.setHint("Delivery Address");
        delivery_address.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        delivery_address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e("select place", "Place: " + place.getName() + ", " + place.getId());
                latLong = place.getLatLng();
                Log.e("latlomg", String.valueOf(latLong));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e("error", "An error occurred: " + status);
            }
        });


        Log.e("DeviceToken", sharedPrefrenceData.getDeviceToken());
    }

    public void addItemsOnSpinner() {

        spinner_maptype =  findViewById(R.id.spinner_maptype);

        listMapType.add("Satellite");
        listMapType.add("Map");
        listMapType.add("Hybrid");
        listMapType.add("Terrain");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview, listMapType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_maptype.setAdapter(dataAdapter);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                //.addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

                mMap.setMyLocationEnabled(true);
               // Log.e("1","1");
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
           // Log.e("2","2");
        }

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
               // Log.e("onCameraMove","onCameraMove");
            }
        });

        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                //Log.e("onCameraMoveCanceled","onCameraMoveCanceled");
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //Log.e("stop moving","stop moving");
                latLong = mMap.getCameraPosition().target;
                delivery_address.setText("");
                getAddressText(latLong);

            }
        });

    }
    @Override
    public void onLocationChanged(Location location) {
        //Log.e("location change","location change");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latLong = new LatLng(location.getLatitude(), location.getLongitude());

         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        // mMap.clear();
        //  mMap.addMarker(markerOptions);
         //Log.e("are","are");
         getAddressText(latLong);

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e("conn", "Google Places API connected.");
        //Log.e("qw","wqwq");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("susupended", "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("not connected", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    // Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                Log.e("aaa","aaaa");
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void getAddressText(LatLng latLng){

        Geocoder geocoder;

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList= geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            //Log.e(" latitude", String.valueOf(latitude));
            // Log.e(" longitude", String.valueOf(longitude));
            if (addressList.size() > 0)
            {
                yourAddress = addressList.get(0).getAddressLine(0);
                delivery_address.setText(yourAddress);

                //String yourCity = yourAddresses.get(0).getAddressLine(1);
                //String yourCountry = yourAddresses.get(0).getAddressLine(2);
                //Log.e(" AAAAAAAAAAAAA",yourAddress);
                zip = addressList.get(0).getPostalCode();
               // Log.e("zip ",zip);
                if(hashMap.containsKey(zip+vehical_gasText)){
                   gas_priceText = hashMap.get(zip+vehical_gasText);
                   gas_price.setText("$"+gas_priceText);
                }
                else{
                    gas_priceText = "0";
                    gas_price.setText("$"+gas_priceText);
                    Snackbar.make(view, "Currently service is not available for this location.", Snackbar.LENGTH_LONG).show();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getVehicalListVolley() {
       // Log.e("device token",sharedPrefrenceData.getDeviceToken());

        String url = UrlConstants.VEHICLElISTWITHOUTINDEX+"?CustID="+c_id+"&AccessTokan="
                +c_accesstoken;
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
                                    String v_gasTypeId = jsonObject1.getString("GasTypeId");

                                    //Log.e("v_id",v_id);

                                    vehicalListItem.setVehical_id(v_id);
                                    vehicalListItem.setVehical_year(v_year);
                                    vehicalListItem.setVehical_make(v_make);
                                    vehicalListItem.setVehical_model(v_model);
                                    vehicalListItem.setVehical_color(v_color);
                                    vehicalListItem.setVehical_plateno(v_plateno);
                                    vehicalListItem.setVehical_state(v_state);
                                    vehicalListItem.setVehical_gastype(v_gasType);
                                    vehicalListItem.setVehical_gastypeid(v_gasTypeId);
                                    vehicalListItemList.add(vehicalListItem);
                                    listVehicles.add(v_make +" " + v_model + " " + v_color + " " + v_plateno);

                                }

                                ArrayAdapter<String> vehicalAdapter = new ArrayAdapter<String>(Home.this,
                                        R.layout.spinner_textview1, listVehicles);
                                vehicalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_vehicles.setAdapter(vehicalAdapter);
                                pDialog.dismiss();
                            }
                            else{
                                pDialog.dismiss();
                                showPopup();
                                // pDialog.dismiss();
                                Snackbar.make(view, Status, Snackbar.LENGTH_LONG).show();
                            }

                            //adapter.notifyDataSetChanged();

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


    private void getAllGasPrice() {
        // Log.e("device token",sharedPrefrenceData.getDeviceToken());

        String url = UrlConstants.ALLGASPRICE+"?AccessTokan="+c_accesstoken;
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

                                JSONArray jsonArray = jsonObject.getJSONArray("GasTypeDetails");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String GasType = jsonObject1.getString("GasType");
                                    String Price = jsonObject1.getString("Price");
                                    String State = jsonObject1.getString("State");
                                    String ZipCode = jsonObject1.getString("ZipCode");

                                    hashMap.put(ZipCode+GasType,Price);


                                }
                                //Log.e("hasmap", String.valueOf(hashMap));
                                getVehicalListVolley();


                            }
                            else{
                                String ErrorMessage = jsonObject.getString("ErrorMessage");
                                if(ErrorMessage.equalsIgnoreCase("Invalid Token")){
                                   sharedPrefrenceData.logout();
                                   intent = new Intent(Home.this,Login.class);
                                   startActivity(intent);
                                } else{
                                    getVehicalListVolley();
                                    //pDialog.dismiss();
                                    Snackbar.make(view, "Price Data Not Available", Snackbar.LENGTH_LONG).show();
                                }


                            }

                            //adapter.notifyDataSetChanged();

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

    private void getBusinessHours() {
        // Log.e("device token",sharedPrefrenceData.getDeviceToken());

        String url = UrlConstants.BUSINESSHOURS+"?AccessTokan="+c_accesstoken;
        Log.e("url ",url);
        // Pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String Status = response.getString("Status");

                            if(Status.equalsIgnoreCase("Success")) {
                                JSONObject jsonObject = response.getJSONObject("Response");
                                JSONArray jsonArray = jsonObject.getJSONArray("BusinessDetail");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    BusinessHourFromTime = jsonObject1.getString("BHourFrom");
                                    BusinessHourToTime = jsonObject1.getString("BHourTo");

                                }


                            }
                            else{
                                //pDialog.dismiss();
                                //Snackbar.make(view, "Price Data Not Available", Snackbar.LENGTH_LONG).show();
                            }

                            //adapter.notifyDataSetChanged();

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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean checkBusinessHours(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
        Date currentDate = new Date();
        String currentDateTimeString = sdf.format(currentDate);

        Date current = null,from=null,to  = null;
        try {
            current = sdf.parse(currentDateTimeString);
            from = sdf.parse(BusinessHourFromTime);
            to = sdf.parse(BusinessHourToTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            if(!current.before(from) && !current.after(to) ){
               return true;
            }else{
               return false;
            }

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
        View layout = layoutInflater.inflate(R.layout.add_vehical_popup, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(
                layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        popup.setWidth(width-200);
        //popup.setAnimationStyle(R.style.Animation);
        // popup.showAtLocation(home_linearlayout, Gravity.CENTER,0,0);
        home_linearlayout.post(new Runnable() {
            public void run() {
                popup.showAtLocation(home_linearlayout, Gravity.CENTER, 0, 0);
                dimBehind(popup);
            }
        });
        Button skip =layout. findViewById(R.id.skip);
        Button ok =layout. findViewById(R.id.ok);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,AddVehical.class);
                startActivity(intent);
            }
        });


    }

    private void showPopupBusinessHour() {
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.business_hour_popup, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(
                layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        popup.setWidth(width-200);
        //popup.setAnimationStyle(R.style.Animation);
        // popup.showAtLocation(home_linearlayout, Gravity.CENTER,0,0);
        home_linearlayout.post(new Runnable() {
            public void run() {
                popup.showAtLocation(home_linearlayout, Gravity.CENTER, 0, 0);
                dimBehind(popup);
            }
        });
        Button ok =layout. findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });



    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
}
