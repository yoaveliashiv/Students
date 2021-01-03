package com.example.chat.services;

import com.google.firebase.messaging.FirebaseMessagingService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.R;

import com.google.firebase.messaging.RemoteMessage;
public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
    NotificationChannel notificationChannel= new NotificationChannel
            ("new message","new message", NotificationManager.IMPORTANCE_DEFAULT);
    NotificationManager notificationManager=getSystemService(NotificationManager.class);
    notificationManager.createNotificationChannel(notificationChannel);
}
        else{
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"new message");
        builder.setContentTitle("my tytle");
        builder.setContentText("מתי יוצא?");
        builder.setSmallIcon(R.drawable.profile_activity);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
        }
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.drawable.student_icon)
//                        .setContentTitle(title)
//                        .setContentText(message);
//        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
    }
}
