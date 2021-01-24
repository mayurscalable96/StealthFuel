package com.stealthfuel.app.stealthfuelon.other;

import android.content.Intent;
import android.util.Log;

import com.stealthfuel.app.stealthfuelon.activity.OrderList;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Belal on 03/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrenceData sharedPrefrenceData;

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        storeToken(token);
    }

    private void storeToken(String token) {
        Log.e("token",token);

         sharedPrefrenceData = new SharedPrefrenceData(this);
        //we will save the token in sharedpreferences later
        sharedPrefrenceData.setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                Map<String, String> params = remoteMessage.getData();
             //  Log.e("sd", String.valueOf(params));
                JSONObject json = new JSONObject(params);
                Log.e("json", String.valueOf(json));
               sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void   sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
           // JSONObject data = json.getJSONObject("data");
          //  Log.e("data", String.valueOf(data));

            //parsing json data
            String title = json.getString("title");
            String message = json.getString("message");
            //String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplication());
            Intent intent = null;
            intent = new Intent(getApplicationContext(), OrderList.class);

            //if there is no image
           // if(imageUrl.equals("null")){
                //displaying small notification
            mNotificationManager.showSmallNotification(title, message, intent);
            //}else{
                //if there is an image
                //displaying a big notification
              //  mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            //}
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}
