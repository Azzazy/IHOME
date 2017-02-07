package com.ihome.muhammad.esp.JAVA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.ihome.muhammad.esp.Home;
import com.ihome.muhammad.esp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

    public static Device getTest() {//debug
        return new Device("TEST123", "TE:ST:TE:ST", "The Test Machine", "192.168.1.1");
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
        if (devices == null) {
            Toast.makeText(con, "No Registered Devices", Toast.LENGTH_SHORT).show();
            return null;
        }
        ArrayList<Device> devs = new ArrayList<>();
        for (String s : devices) {
            devs.add(new Device(s));
        }
        Device[] ds = new Device[devs.size()];
        return devs.toArray(ds);
    }

    public static void setAll(Context con, Device[] devices) {
        ArrayList<String> devs = new ArrayList<>();
        for (Device d : devices) {
            devs.add(d.toString());
        }
        SharedPreferences sharedPref = con.getSharedPreferences(
                con.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(con.getString(R.string.registered_devices_key), new HashSet<String>(devs));
        editor.apply();
        Toast.makeText(con, "Saved all", Toast.LENGTH_SHORT).show();
    }

    public static void add(Context con, Device device) {
        Device currents[] = getAll(con);
        ArrayList<Device> all;
        if (currents != null) {
            all = new ArrayList<>(Arrays.asList(currents));
        } else {
            all = new ArrayList<>();
        }
        all.add(device);
        setAll(con, all.toArray(new Device[all.size()]));
    }

    @Override
    public String toString() {
        String temp = "";
        temp += model + "_";
        temp += mac + "_";
        temp += name + "_";
        temp += ip;
        return temp;
    }

    public static Device getDeviceOrNull(List<Device> deviceList, String ip) {
        try {
            Document doc = Jsoup.connect(ESP.HTTP_PRE + ESP.getInfoURL(ip)).get();//if passed this then it's a device
            String mac = doc.select(ESP.TAG_MAC).first().text();
            System.out.println("mac=" + mac);//debug
            for (Device d : deviceList) {
                if (d.mac.equals(mac)) {
                    return d;
                }
            }
            System.out.println("no matching device found");//debug
        } catch (Exception e) {
//    e.printStackTrace();//for saving time
            System.out.println("failed at ip " + ip);
        }
        return null;
    }
}
