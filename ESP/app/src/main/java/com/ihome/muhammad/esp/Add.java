package com.ihome.muhammad.esp;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Add extends AppCompatActivity implements GetPassword.OnFragmentInteractionListener, AvailableDevices.OnFragmentInteractionListener {
    GetPassword passFrag;
    AvailableDevices avFrag;
    String pass, ssid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ssid = getWifiName(this);
        if (savedInstanceState == null) {
            displayFragment();
        }
    }

    @Override
    public void onDoneClicked(String pass) {
        //Todo: test connecting to the wifi using this pass
        pass = pass;
        avFrag = AvailableDevices.newInstance(ssid, pass);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, avFrag).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void displayFragment() {
        if (findViewById(R.id.fragmentContainer) != null) {
            passFrag = GetPassword.newInstance(ssid);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, passFrag).commit();
        }
    }

    public static String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    String ssid = wifiInfo.getSSID();
                    ssid = ssid.substring(1, ssid.length() - 1);
                    return ssid;
                }
            }
        }
        return null;
    }
}
