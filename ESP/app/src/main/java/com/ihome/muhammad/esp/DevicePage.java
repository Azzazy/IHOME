package com.ihome.muhammad.esp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DevicePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_page);
        Intent i = getIntent();
        int deviceID = i.getIntExtra(Home.ID_STRING, -1);
        if (Home.savedDeviceList == null || Home.savedDeviceList.isEmpty()) {
            Home.updateSavedDeviceList(this);
        }
        ((TextView) findViewById(R.id.textView3)).setText(Home.savedDeviceList.get(deviceID).toString());
    }
}
