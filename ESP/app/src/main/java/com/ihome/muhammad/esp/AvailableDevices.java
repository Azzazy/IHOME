package com.ihome.muhammad.esp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihome.muhammad.esp.JAVA.Device;
import com.ihome.muhammad.esp.JAVA.ESP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Set;

import static com.ihome.muhammad.esp.Home.show;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this passFrag must implement the
 * {@link AvailableDevices.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailableDevices#newInstance} factory method to
 * create an instance of this passFrag.
 */
public class AvailableDevices extends Fragment {
    private static final String SSID = "param1";
    private static final String PASS = "param2";

    private String ssid;
    private String pass;
    private TextView tvInfo, tvStatus;
    private OnFragmentInteractionListener mListener;
    private Device dev;

    public AvailableDevices() {
        // Required empty public constructor
    }

    public static AvailableDevices newInstance(String ssid, String pass) {
        AvailableDevices fragment = new AvailableDevices();
        Bundle args = new Bundle();
        args.putString(SSID, ssid);
        args.putString(PASS, pass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ssid = getArguments().getString(SSID);
            pass = getArguments().getString(PASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_available_devices, container, false);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvInfo = (TextView) getView().findViewById(R.id.tvDeviceInfo);
        tvStatus = (TextView) getView().findViewById(R.id.tvAvailableDevicesStatus);
        getView().findViewById(R.id.bAddDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ESP.getConnectURL(ssid, pass);
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();//debug
//                new ConnectTask().execute(url);//debug
                dev = new Device("ABC123", "ab:cd:ef:gh", "Room", "0000");
                show(getActivity(), "the dev" + dev.toString());
                Device.add(getContext(), dev);
                show(getActivity(), "done adding");
                Intent i = new Intent(getContext(), Home.class);
                getContext().startActivity(i);
            }
        });
        Toast.makeText(getContext(), "We are here", Toast.LENGTH_SHORT).show();
//        if (setWifiConfig(ESP.AP_SSID, ESP.AP_PASS, true)) {//debug
//            new RequestInfoTask().execute(ESP.getInfoURL(), ESP.AP_SSID);
//        } else {
//            Toast.makeText(getContext(), "Not Connected to ESP!!", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        }
    }

    class RequestInfoTask extends AsyncTask<String, String, Document> {
        @Override
        protected Document doInBackground(String... uri) {
            try {
                String name = Add.getWifiName(getContext());
                while (name == null || !name.equals(uri[1])) {
                    name = Add.getWifiName(getContext());
                }
                Document doc;
                while (true) {
                    try {
                        doc = Jsoup.connect(uri[0]).get();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    break;
                }
                return doc;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Document result) {
            tvInfo.setText(Html.fromHtml(result.outerHtml()));
            tvStatus.setText("Found Device");
            String model = result.select("model").first().text();
            String mac = result.select("macAddress").first().text();
            dev = new Device(model, mac, model, "0");
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

        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        try {
            for (WifiConfiguration i : list) {//todo make sure wifi is on
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
