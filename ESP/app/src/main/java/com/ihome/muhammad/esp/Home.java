package com.ihome.muhammad.esp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
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
import android.widget.Toast;

import com.ihome.muhammad.esp.JAVA.DevicesAdapter;
import com.ihome.muhammad.esp.JAVA.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DevicesAdapter adapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        deviceList = Arrays.asList(Device.getAll(this));


        adapter = new DevicesAdapter(this, deviceList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        updateDevices();
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
        updateDevices();

    }

    /**
     * Adding few albums for testing
     */
    private void updateDevices() {
//        deviceList.clear();
        Device[] devs = Device.getAll(this);
        for (Device d : devs) {
            System.out.println("d=" + d.toString());
        }
        deviceList = Arrays.asList(devs);
//        deviceList.add(new Device("F312", "25:23:12:78:12:96", "Bed Room", "192.168.1.21"));
//        deviceList.add(new Device("A670", "25:23:12:45:12:96", "TV", "192.168.1.22"));
//        deviceList.add(new Device("A430", "25:23:34:78:12:96", "Computer", "192.168.1.23"));
//        deviceList.add(new Device("A670", "25:22:12:78:12:96", "Ali", "192.168.1.24"));
//        deviceList.add(new Device("A670", "12:23:12:78:12:96", "Master Room", "192.168.1.25"));
//        deviceList.add(new Device("B512", "25:23:12:23:12:96", "Kitchen", "192.168.1.26"));
//        deviceList.add(new Device("A670", "25:23:12:42:12:12", "Mohamed", "192.168.2.168.1.21"));
//        deviceList.add(new Device("A670", "25:23:12:45:12:96", "TV", "192.168.1.22"));
//        deviceList.add(new Device("A430", "25:23:34:78:12:96", "Computer", "192.168.1.23"));
//        deviceList.add(new Device("A670", "25:22:12:78:12:96", "Ali", "192.168.1.24"));
//        deviceList.add(new Device("A670", "12:23:12:78:12:96", "Master Room", "192.168.1.25"));
//        deviceList.add(new Device("B512", "25:23:12:23:12:96", "Kitchen", "192.168.1.26"));
//        deviceList.add(new Device("A670", "25:23:12:42:12:12", "Mohamed", "192.168.2.168.1.21"));
//        deviceList.add(new Device("A670", "25:23:12:45:12:96", "TV", "192.168.1.22"));
//        deviceList.add(new Device("A430", "25:23:34:78:12:96", "Computer", "192.168.1.23"));
//        deviceList.add(new Device("A670", "25:22:12:78:12:96", "Ali", "192.168.1.24"));
//        deviceList.add(new Device("A670", "12:23:12:78:12:96", "Master Room", "192.168.1.25"));
//        deviceList.add(new Device("B512", "25:23:12:23:12:96", "Kitchen", "192.168.1.26"));
//        deviceList.add(new Device("A670", "25:23:12:42:12:12", "Mohamed", "192.168.1.27"));

        adapter.notifyDataSetChanged();
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
