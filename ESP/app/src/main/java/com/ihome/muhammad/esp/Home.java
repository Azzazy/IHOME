package com.ihome.muhammad.esp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ihome.muhammad.esp.JAVA.DevicesAdapter;
import com.ihome.muhammad.esp.JAVA.Device;
import com.ihome.muhammad.esp.JAVA.ESP;
import com.ihome.muhammad.esp.JAVA.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {
    public static String ID_STRING = "id";

    private RecyclerView recyclerView;
    private DevicesAdapter adapter;
    public static List<Device> savedDeviceList;
    public static List<Device> currentDeviceList;
    TextView tvStat;

    public static void show(Activity c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();//debug
    }

    public static void show(Fragment c, String s) {
        Toast.makeText(c.getActivity(), s, Toast.LENGTH_SHORT).show();//debug
    }

    public static void show(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();//debug
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        savedDeviceList = Arrays.asList(Device.getAll(this));

        currentDeviceList = new ArrayList<>();
        adapter = new DevicesAdapter(this, currentDeviceList);
        tvStat = (TextView) findViewById(R.id.tvHomeStatus);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

//        updateCurrentDeviceList();//debug

    }


    private void showAdd() {
//        Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Add.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                showAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStat.setText("Searching for Devices on the network");
        updateCurrentDeviceList();

    }

    public static void updateSavedDeviceList(Activity con) {
        savedDeviceList = Arrays.asList(Device.getAll(con));
//        show(con, "savedDeviceList updated");//debug
    }

    private void updateCurrentDeviceList() {
        //todo search for the devices in the network
        updateSavedDeviceList(this);
        searchForDevices();

    }

    private void searchForDevices() {
        new SearchTask().execute(Utils.getIPAddress(true));
    }

    class SearchTask extends AsyncTask<String, String, String[]> {
        @Override
        protected String[] doInBackground(String... uri) {
            long t = System.currentTimeMillis();//debug
            String[] x = new String[2/*debug*/];
            for (int i = 0; i <= 255; i++) {
                try {
                    //Assuming uri[0] = "192.168.1.236"
                    String baseIP = uri[0].substring(0, uri[0].lastIndexOf('.') + 1);
                    String infoDir = ESP.HTTP_PRE + ESP.getInfoURL(baseIP + i);
//                    System.out.println("trying in " + infoDir);//debug

                    Document doc = Jsoup.connect(baseIP + i + infoDir).get();//if passed this then it's a device

                    String mac = doc.select("macAddress").first().text();
                    System.out.println("mac=" + mac);//debug

                    Device d = Device.getWithMac(savedDeviceList, mac);
                    if (d != null)
                        currentDeviceList.add(d);
                } catch (Exception e) {
//                    e.printStackTrace();//too much time wasted//debug
                    x[0] = "exception";
                }
            }
            t = System.currentTimeMillis() - t;//debug
            x[1] = "" + t;//debug
            return x;
        }

        @Override
        protected void onPostExecute(String[] result) {
            System.out.println("Done in " + result[1] + "ms");
            show(getApplicationContext(), "Done Searching in " + result[1] + "ms");
            tvStat.setText("Done");
//            currentDeviceList.add(Device.getTest());//debug
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
