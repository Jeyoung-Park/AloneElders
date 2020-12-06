package com.aloneelders;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UserPresentBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG="TAG_UserPresentBroadcastReceiver";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "UserPresentBroadcastReceiver 호출");

        /*Sent when the user is present after
         * device wakes up (e.g when the keyguard is gone)
         * */
        if(intent.getAction().equals(Intent.ACTION_USER_UNLOCKED)){
            Log.d(TAG, "폰 켜짐");
        }
        /*Device is shutting down. This is broadcast when the device
         * is being shut down (completely turned off, not sleeping)
         * */
        else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            Log.d(TAG, "폰 꺼짐");
        }
    }

}
