package com.stealthfuel.app.stealthfuelon.other;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrenceData {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "MyPref";

    public SharedPrefrenceData(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getCustomerId() {
        return pref.getString("customer_id",null);
    }

    public void setCustomerId(String customer_id) {
        editor.putString("customer_id", customer_id);
        editor.commit();
    }

    public String getCustomerName() {
        return pref.getString("customer_name",null);
    }

    public void setCustomerName(String customer_name) {
        editor.putString("customer_name", customer_name);
        editor.commit();
    }

    public String getCustomerMob() {
        return pref.getString("customer_mob",null);
    }

    public void setCustomerMob(String customer_mob) {
        editor.putString("customer_mob", customer_mob);
        editor.commit();
    }

    public String getCustomerEmail() {
        return pref.getString("customer_email",null);
    }

    public void setCustomerEmail(String customer_email) {
        editor.putString("customer_email", customer_email);
        editor.commit();
    }

    public String getCustomerAccessToken() {
        return pref.getString("customer_access_token",null);
    }

    public void setCustomerAccessToken(String customer_access_token) {
        editor.putString("customer_access_token", customer_access_token);
        editor.commit();
    }


    public String getCustomerRefralCode() {
        return pref.getString("customer_refralcode",null);
    }
    public void setCustomerRefralCode(String customer_refralcode) {
        editor.putString("customer_refralcode", customer_refralcode);
        editor.commit();
    }



    public void logout(){
        //editor.clear();
        editor.remove("customer_id");
        editor.remove("customer_name");
        editor.remove("customer_mob");
        editor.remove("customer_email");
        editor.remove("customer_access_token");
        editor.remove("customer_refralcode");
        editor.commit();

    }

    //this method will save the device token to shared preferences
    public boolean setDeviceToken(String token) {
        editor.putString("devicetoken", token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        String token = pref.getString("devicetoken", null);
        //if (token == null) {
        //  token = "";
        //}
        //   Log.e("ewewewew",token);
        return token;
    }
}
