package com.gmail.hanivisushiva.aksharafinserve;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gmail.hanivisushiva.aksharafinserve.Storage.SharedPrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "message";
    NotificationManager notificationManager;
    public final static String TOKEN_BROADCAST = "Akshara_broadcast";

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token-----: " + token);
        //Toast.makeText(getApplicationContext(),"Refreshed token: " + token,Toast.LENGTH_LONG).show();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token){
        SharedPrefManager.get_mInstance(getApplicationContext()).saveDeviceToken(token);
        Log.e("sdchjsdbc",token+"s dkvj jv");
        Log.e("sdchjsdbc",SharedPrefManager.get_mInstance(getApplicationContext()).getDeviceToken()+"s dkvj jv");
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

//        Log.e("dataChat4",remoteMessage.getData().get("icon"));
     //   Log.e("dataChat5",remoteMessage.getData().get("user"));
       // Log.e("dataChat6",remoteMessage.getData().get("title"));


      /*  Log.e("dataChat",remoteMessage.getNotification().getIcon());
        Log.e("dataChat1",remoteMessage.getNotification().getBody());
        Log.e("dataChat2",remoteMessage.getNotification().getTitle());
        Log.e("dataChat3", String.valueOf(remoteMessage.getNotification().getBodyLocalizationArgs())); */

        SharedPreferences sharedPreferences=getSharedPreferences("UserPreferences",MODE_PRIVATE);
        String currenruser=sharedPreferences.getString("currentuser","none");
        String user=remoteMessage.getData().get("user");
        if(!currenruser.equals(user))
        {
            sendNotification(remoteMessage);
        }

    }

    private void sendNotification(RemoteMessage remoteMessage)
    {
        Log.e("keyset",remoteMessage.getData().keySet().toString());
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");


        RemoteMessage.Notification notification=remoteMessage.getNotification();
        //  int j=Integer.parseInt(user.replaceAll("[\\D]]",""));
        Intent intent=new Intent(this, MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,123,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Aksharafinserv");

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle(title)
                .setContentText(body)
                .setSound(defaultsound)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createChannel();
           } */

        notificationManager.notify(1, notificationBuilder.build());
    }

    /* @TargetApi (Build.VERSION_CODES.O)
     private void createChannel()
     {
         NotificationChannel channel = new NotificationChannel("MUCHATLU_CH_ID",
                 "muchatlu app",
                 NotificationManager.IMPORTANCE_DEFAULT);
         channel.enableLights(false);
         channel.enableVibration(true);
         channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
         notificationManager.createNotificationChannel(channel);

     } */
    public NotificationManager notificationManager()
    {
        if(notificationManager==null)
        {
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  notificationManager;
    }
   /* @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri sound, String icon)
    {
        return new Notification.Builder(getApplicationContext(),"MUCHATLU_CH_ID")
                .setContentIntent(pendingIntent).setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.mlogo).setSound(sound).setAutoCancel(true);

    } */

}

