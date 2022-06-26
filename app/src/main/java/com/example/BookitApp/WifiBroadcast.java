package com.example.BookitApp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiBroadcast extends BroadcastReceiver {
    AlertDialog dialog;

    public WifiBroadcast(AlertDialog dialog) {
        this.dialog=dialog;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action= intent.getAction();
        if (action.equals("android.net.wifi.STATE_CHANGE")) {
            WifiManager wifiManager= (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            WifiInfo wifiInfo= wifiManager.getConnectionInfo();
            dialog.setMessage(wifiInfo.toString());
            dialog.show();
        }
    }
}