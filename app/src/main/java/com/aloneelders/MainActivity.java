package com.aloneelders;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="TAG_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.sharedPreference_key), Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean(getString(R.string.sharedPrerfeence_key_isFirst), false)){
            Log.d(TAG, "sharedPref");
            makeDialog_info();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.sharedPrerfeence_key_isFirst), true);
            editor.commit();
        }

        registerScreenLockStateBroadcastReceiver();
    }

    private void makeDialog_info(){
        Log.d(TAG, "makeDialog_info");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_first, null))
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setCancelable(false);
        // Create the AlertDialog object and return it
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void registerScreenLockStateBroadcastReceiver(){
        final IntentFilter intentFilter=new IntentFilter();

        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);

        BroadcastReceiver screenOnOffReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction=intent.getAction();
                KeyguardManager keyguardManager= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if(strAction.equals(Intent.ACTION_SCREEN_OFF)){
                    Log.d(TAG, "Screen Off");
                }
                else if(strAction.equals(Intent.ACTION_SCREEN_ON)){
                    Log.d(TAG, "screen on");
                }
                if(strAction.equals(Intent.ACTION_USER_PRESENT)&&!keyguardManager.inKeyguardRestrictedInputMode()){
                    Log.d(TAG, "Device Unlocked");
                }
                else{
                    Log.d(TAG, "Device LOCKED");
                }
            }
        };

        getApplicationContext().registerReceiver(screenOnOffReceiver, intentFilter);
    }
}