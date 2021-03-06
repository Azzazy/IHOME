package com.ihome.muhammad.esp.JAVA;

/**
 * Created by muhammad on 25/01/17.
 */

public class ESP {

    final public static String TAG_MAC = "macAddress";
    final public static String TAG_MODEL = "model";

    final public static String HTTP_PRE = "http://";

    final private static String BASE = "http://192.168.4.1";
    final private static String INFO = "/info";
    final private static String CONNECT = "/connect";
    final private static String COMMAND = "/do";

    final private static String SSID = "ssid";
    final private static String PASS = "password";

    final public static String AP_SSID = "ESPap";
    final public static String AP_PASS = "thereisnospoon";

    private static String get(String ip, String dist) {
        return ip + dist + "?";
    }

    private static String arg(String parm, String arg) {
        return parm + "=" + arg + "&";
    }

    public static String getConnectURL(String ssid, String pass) {
        return get(BASE, CONNECT) + arg(SSID, ssid) + arg(PASS, pass);
    }

    public static String getInfoURL(String ip) {
        return (ip == null) ? get(BASE, INFO) : get(ip, INFO);
    }
}
