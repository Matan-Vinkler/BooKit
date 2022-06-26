package com.example.BookitApp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    AlertDialog dialog;
    WifiBroadcast wifiBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDialog();
        wifiBroadcast= new WifiBroadcast(dialog);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;

                if(user != null) {
                    i = new Intent(MainActivity.this, HomePageActivity.class);
                }
                else {
                    i = new Intent(MainActivity.this, RegisterActivity.class);
                }

                startActivity(i);
                finish();
            }
        },4000);
    }
    public void createDialog() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("wifi");
        builder.setCancelable(true);
        builder.setPositiveButton("press ok to continue", null);
        dialog = builder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
        registerReceiver(wifiBroadcast,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiBroadcast);
    }
}