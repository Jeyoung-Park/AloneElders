package com.aloneelders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private int NOTIFICATION_ID=10001;
    private static final String TAG="TAG_MyService";
    private static final String CHANNEL_ID="channel_id";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService onCreate 호출");

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        NotificationChannel channel=new NotificationChannel(CHANNEL_ID, "", NotificationManager.IMPORTANCE_LOW);
//        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//        Notification notification =
//                new Notification.Builder(this, CHANNEL_ID)
//                        .setContentTitle(getText(R.string.app_name))
//                        .setContentText(getText(R.string.notification_message))
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentIntent(pendingIntent)
////                        .setTicker(getText(R.string.ticker_text))
//                        .build();
//
//// Notification ID cannot be 0.
//        startForeground(2, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyService onStartCommand 호출");

        String input = intent.getStringExtra("inputExtra"); //인텐트 값
        // 안드로이드 O버전 이상에서는 알림창을 띄워야 포그라운드 사용 가능
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("서비스 실행중")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent) .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBing 호출");
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel( CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
