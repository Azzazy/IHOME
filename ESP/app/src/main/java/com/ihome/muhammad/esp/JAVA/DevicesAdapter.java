package com.ihome.muhammad.esp.JAVA;

/**
 * Created by muhammad on 14/01/17.
 */


import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihome.muhammad.esp.R;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Device> deviceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type;
        public ImageView overflow;
        public int id;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            type = (TextView) view.findViewById(R.id.type);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            id = -1;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, id + " is Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public DevicesAdapter(Context mContext, List<Device> deviceList) {
        this.mContext = mContext;
        this.deviceList = deviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Device device = deviceList.get(position);
        System.out.print("holder" + holder);
        holder.name.setText(device.getName());
        holder.type.setText(device.getModel());
        holder.id = position;
        // loading album cover using Glide library
//        Glide.with(mContext).load(device.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_device, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_device_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_device_rename:
                    Toast.makeText(mContext, "Rename", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
