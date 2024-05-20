package com.example.finalproject.booking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID "; //REQUIRED 4 ANDROID 0 AND ABOVE

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        //all notification wil be received here

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //get data from notification
        String notificationType = message.getData().get("notificationType");
        if (notificationType.equals("NewBooking")){
            String buyerUid = message.getData().get("buyerUid");
            String sellerUid = message.getData().get("sellerUid");
            String bookingId = message.getData().get("bookingId");
            String notificationTitle = message.getData().get("notificationTitle");
            String notificationDescription = message.getData().get("notificationDescription");

            if(firebaseUser!=null && firebaseAuth.getUid().equals(sellerUid)) {
                //user login and user to which notification is sent
                showNotification(bookingId, sellerUid, buyerUid, notificationTitle, notificationDescription, notificationType);

            }
        }
        if (notificationType.equals("BookingStatusChanged")){
            String buyerUid = message.getData().get("buyerUid");
            String sellerUid = message.getData().get("sellerUid");
            String bookingId = message.getData().get("bookingId");
            String notificationTitle = message.getData().get("notificationTitle");
            String notificationDescription = message.getData().get("notificationMessage");

            if(firebaseUser!=null && firebaseAuth.getUid().equals(buyerUid)){
                //user login and user to which notification is sent
                showNotification(bookingId, sellerUid, buyerUid, notificationTitle, notificationDescription, notificationType);

            }

        }
    }

    private void showNotification(String id, String sellerUid, String buyerUid, String notificationTitle, String notificationDescription, String notificationType) {

        // notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //id for notification, random
        int notificationID = new Random().nextInt(300);

        //check if android version is Oreo0 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setupNotificationChannel(notificationManager);
        }
        //handle notification click, start order activity
        Intent intent = null;
        if (notificationType.equals("NewBooking")){
            //Open BookingDetailsSellerActivity
            intent = new Intent(this, HomeActivity.class );
            intent.putExtra("bookingId", id);
            intent.putExtra("bookingBy", buyerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        } else if (notificationType.equals("BookingStatusChanged")){
            //Open MyBookingActivity
            intent = new Intent(this, MyBookingActivity.class );
            intent.putExtra("bookingId", id);
            intent.putExtra("bookingTo", sellerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        //Large Icon
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_faceid);

        //sound of notification
        Uri notificationSounUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_faceid)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSound(notificationSounUri)
                .setAutoCancel(true) // cancel click when clicked
                .setContentIntent(pendingIntent); // add Intent

        //show notification
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void setupNotificationChannel(NotificationManager notificationManager) {

        CharSequence channelName = "Some Sample Text";
        String channelDescription = "Channel Description here";

        NotificationChannel notificationChannel =  new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        if (notificationManager !=null){
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }



    /*private void prepareNotificationMessage(String id, String message) {
        // Prepare data for notification
        String NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC;
        String NOTIFICATION_TITLE = "Your Booking: " + id;
        String NOTIFICATION_MESSAGE = message;
        String NOTIFICATION_TYPE = "BookingStatusChanged";

        // Prepare JSON (what and where to send)
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("sellerUid", firebaseAuth.getUid());
            notificationBodyJo.put("orderId", id);
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE);

            notificationJo.put("to", NOTIFICATION_TOPIC);
            notificationJo.put("data", notificationBodyJo);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to prepare notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        sendFcmNotification(notificationJo);
    }

    private void sendFcmNotification(JSONObject notificationJo) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,
                response -> {
                    // Notification sent
                },
                error -> {
                    // Notification failed
                    Toast.makeText(DashboardAdminActivity.this, "Failed to send notification: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }



    private void prepareNotificationMessage(String id){
        // when change status Paid, Cancelled, Completed, send notification to user

        //Prepare data for notification
        String NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC;
        String NOTIFICATION_TITLE = "New Booking: " + hotel_id;
        String NOTIFICATION_MESSAGE = "Congratulation...! You have new Booking.";
        String NOTIFICATION_TYPE = "NewBooking";


        //prepare json (what and where send)
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
            //What

            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid", auth.getUid());
            notificationBodyJo.put("sellerUid", hotel_id);
            notificationBodyJo.put("orderId",id);
            notificationBodyJo.put("notificationTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage",NOTIFICATION_MESSAGE);

            //where
            notificationJo.put("to", NOTIFICATION_TOPIC); // to all who sub to this topic
            notificationJo.put("data", notificationBodyJo);
        } catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        sendFcmNoNotification(notificationJo, id);
    }

    private void sendFcmNoNotification(JSONObject notificationJo, String id) {
        //send volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // after sending fcm start MyBookingActivity
                Intent intent = new Intent(Payment2Activity.this, MyBookingActivity.class);
                intent.putExtra("bookingTo", hotel_id);
                intent.putExtra("bookingId", id);
                intent.putExtra("bookingBy", auth.getUid());
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //if failed sending fcm, still start payment2activity
                Intent intent = new Intent(Payment2Activity.this, MyBookingActivity.class);
                intent.putExtra("bookingTo", hotel_id);
                intent.putExtra("bookingId", id);
                startActivity(intent);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // put required headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key="+Constants.FCM_KEY);
                return headers;
            }
        };
        // enque the volley request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }*/
}



