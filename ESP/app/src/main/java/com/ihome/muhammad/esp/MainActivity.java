package com.ihome.muhammad.esp;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String ssid, pass, mac = "5C:CF:7F:C1:1A:4C", myip, ip;

    String espssid = "ESPap";
    String esppass = "thereisnospoon";
    String techssid = "TECHPLANET";
    String techpass = "tech-planet";

    Button bsave, bcon, bnext2, bnext3, bnext4, bnext5, bget, bsend, bsearch, bdo;
    EditText etssid, etpass, etcom;
    RelativeLayout g1, g2, g3, g4, g5, g6;
    TextView tvcon, tvdis;
    String espurl = "http://192.168.4.1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bsave = (Button) findViewById(R.id.bsave);
        bcon = (Button) findViewById(R.id.bconnect);
        bnext2 = (Button) findViewById(R.id.bnext2);
        bnext3 = (Button) findViewById(R.id.bnext3);
        bnext4 = (Button) findViewById(R.id.bnext4);
        bnext5 = (Button) findViewById(R.id.bnext5);
        bsend = (Button) findViewById(R.id.bsend);
        bget = (Button) findViewById(R.id.bget);
        bsearch = (Button) findViewById(R.id.bsearch);
        bdo = (Button) findViewById(R.id.bdo);
        etssid = (EditText) findViewById(R.id.etSSID);
        etpass = (EditText) findViewById(R.id.etPASS);
        etcom = (EditText) findViewById(R.id.etcom);
        tvcon = (TextView) findViewById(R.id.textView1);
        tvdis = (TextView) findViewById(R.id.tvdisplay);
        g1 = (RelativeLayout) findViewById(R.id.group1);
        g2 = (RelativeLayout) findViewById(R.id.group2);
        g3 = (RelativeLayout) findViewById(R.id.group3);
        g4 = (RelativeLayout) findViewById(R.id.group4);
        g5 = (RelativeLayout) findViewById(R.id.group5);
        g6 = (RelativeLayout) findViewById(R.id.group6);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssid = etssid.getText().toString();
                pass = etpass.getText().toString();
                g1.setVisibility(View.GONE);
                g2.setVisibility(View.VISIBLE);
            }
        });
        bcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setWifiConfig(espssid, esppass, true)) {
                    tvcon.setText("Connected!");
                }
            }
        });
        bnext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g2.setVisibility(View.GONE);
                g3.setVisibility(View.VISIBLE);
            }
        });
        bnext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g3.setVisibility(View.GONE);
                g4.setVisibility(View.VISIBLE);
            }
        });
        bnext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g4.setVisibility(View.GONE);
                g5.setVisibility(View.VISIBLE);
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                myip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                Toast.makeText(getApplicationContext(), "my ip is:" + myip, Toast.LENGTH_LONG).show();
            }
        });
        bnext5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g5.setVisibility(View.GONE);
                g6.setVisibility(View.VISIBLE);
            }
        });
        bget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infourl = espurl + "info";
                new RequestInfoTask().execute(infourl);
            }
        });
        bsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = espurl + "connect";
                url += "?ssid=" + techssid;//todo  put here the one obtained from the user
                url += "&password=" + techpass;
                new ConnectTask().execute(url);
            }
        });
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = espurl + "connect";
                url += "?ssid=" + ssid;
                url += "&password=" + pass;
                new SearchTask().execute("http://192.168.1.", "/info");
            }
        });
        bdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ip + "/do?";
                String com = etcom.getText().toString();
                new ComTask().execute(url + com);
            }
        });
    }

    class ComTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            try {
                Jsoup.connect(uri[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "the result is : " + result, Toast.LENGTH_LONG).show();
        }
    }

    class SearchTask extends AsyncTask<String, String, String[]> {
        @Override
        protected String[] doInBackground(String... uri) {
            long t = System.currentTimeMillis();
            String[] x = new String[2];
            for (int i = 0; i <= 255; i++) {
                try {
                    Document doc = Jsoup.connect(uri[0] + i + uri[1]).get();
                    Element e = doc.select("macAddress").first();
                    System.out.println("mac=" + e.text());
                    if (e.text().equals(mac)) {
                        x[0] = uri[0] + i;
                        break;
                    }
                } catch (Exception e) {
//                    e.printStackTrace();//too much time wasted
                    x[0] = "exception";
                }
            }
            t = System.currentTimeMillis() - t;
            x[1] = "" + t;
            return x;
        }

        @Override
        protected void onPostExecute(String[] result) {
            ip = result[0];
            Toast.makeText(getApplicationContext(), "it is at : " + result[0] + " time=" + result[1], Toast.LENGTH_LONG).show();
        }
    }

    class ConnectTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            try {
                Jsoup.connect(uri[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            setWifiConfig(ssid, pass, false);

            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_LONG).show();
        }
    }

    class RequestInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            try {
                Document doc = Jsoup.connect(uri[0]).get();
                Element e = doc.select("macAddress").first();
                return e.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "nothing";
        }

        @Override
        protected void onPostExecute(String result) {

            tvdis.setText(Html.fromHtml(result));
            mac = result;
            mac = "5C:CF:7F:C1:1A:4C";//todo remove this to use the obtained mac address

            //Do anything with response..
        }
    }

    public boolean setWifiConfig(String ssid, String sharedKey, boolean hidden) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.preSharedKey = "\"" + sharedKey + "\"";
        conf.hiddenSSID = hidden;
        conf.status = WifiConfiguration.Status.ENABLED;
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        try {
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    wifiManager.saveConfiguration();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
