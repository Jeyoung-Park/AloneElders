package com.aloneelders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.InsetDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="TAG_MainActivity";
    private EditText EditText_name, EditText_phoneNumber;
    private SharedPreferences sharedPref;
    private Button Button_send_sms;
    private String mName, mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //문자 권한 허용
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        }

        Button_send_sms=findViewById(R.id.Button_send_sms);

        sharedPref = getSharedPreferences(
                getString(R.string.sharedPreference_key), Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean(getString(R.string.sharedPreference_key_isFirst), false)){
            Log.d(TAG, "sharedPref");
            makeDialog_info();
        }

        mName=sharedPref.getString(getString(R.string.sharedPreference_key_name), "default_name");
        mPhoneNumber=sharedPref.getString(getString(R.string.sharedPreference_key_phoneNumber), "default_phoneNumber");

        registerScreenLockStateBroadcastReceiver();

        Button_send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                sendSMS();
                Log.d(TAG, "이름: "+mName+"/ 전화번호: "+mPhoneNumber);
            }
        });

        Log.d(TAG, "이름: "+sharedPref.getString(getString(R.string.sharedPreference_key_name), "default_name")+"/ 전화번호: "+sharedPref.getString(getString(R.string.sharedPreference_key_phoneNumber), "default_number"));
    }

    private void sendSMS(){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(mPhoneNumber, null, "테스트 문자 from "+mName, null, null);
    }

    private void makeDialog_info(){
        Log.d(TAG, "makeDialog_info");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialog_view = factory.inflate(R.layout.dialog_first, null);
        EditText_name=dialog_view.findViewById(R.id.EditText_dialogFirst_name);
        EditText_phoneNumber=dialog_view.findViewById(R.id.EditText_dialogFirst_phoneNumber);

        SharedPreferences.Editor editor = sharedPref.edit();

        builder.setView(dialog_view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String currentName=EditText_name.getText().toString();
                        String currentPhoneNumber=EditText_phoneNumber.getText().toString();
                        if(currentName.length()<=0) makeToast("한 글자 이상의 이름을 입력해주세요.");
                        else if(currentPhoneNumber.length()<=0) makeToast("전화번호를 입력해주세요.");
                        else{
                            editor.putBoolean(getString(R.string.sharedPreference_key_isFirst), true);
                            editor.putString(getString(R.string.sharedPreference_key_name), currentName);
                            editor.putString(getString(R.string.sharedPreference_key_phoneNumber), currentPhoneNumber);
                            editor.apply();
                            mName=sharedPref.getString(getString(R.string.sharedPreference_key_name), "default_name");
                            mPhoneNumber=sharedPref.getString(getString(R.string.sharedPreference_key_phoneNumber), "default_phoneNumber");
                        }
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void makeToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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