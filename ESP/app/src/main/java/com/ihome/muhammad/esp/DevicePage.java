package com.ihome.muhammad.esp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_device_delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_device_rename:
                Toast.makeText(this, "Rename", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_device, menu);
        return true;
    }
}
