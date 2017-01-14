package com.ihome.muhammad.esp.JAVA;

import android.content.Context;
import android.content.SharedPreferences;

import com.ihome.muhammad.esp.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by muhammad on 14/01/17.
 */

public class Device {
    String model, mac, name, ip;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Device(String record) {
        String[] data = record.split("_");
        this.model = data[0];
        this.mac = data[1];
        this.name = data[2];
        this.ip = data[3];
    }

    public Device(String model, String mac, String name, String ip) {
        this.model = model;
        this.mac = mac;
        this.name = name;
        this.ip = ip;
    }

    static public Device[] getAll(Context con) {
        SharedPreferences sharedPref = con.getSharedPreferences(
                con.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Set<String> devices = sharedPref.getStringSet(con.getString(R.string.registered_devices_key), null);
        ArrayList<Device> devs = new ArrayList<>();
        for (String s : devices) {
            devs.add(new Device(s));
        }
        Device[] ds = new Device[devs.size()];
        return devs.toArray(ds);
    }

    public String getRecord() {
        String temp = "";
        temp += model + "_";
        temp += mac + "_";
        temp += name + "_";
        temp += ip;
        return temp;
    }
}