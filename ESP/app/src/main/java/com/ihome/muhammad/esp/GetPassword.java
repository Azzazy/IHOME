package com.ihome.muhammad.esp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this passFrag must implement the
 * {@link GetPassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetPassword#newInstance} factory method to
 * create an instance of this passFrag.
 */
public class GetPassword extends Fragment {
    // the passFrag initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SSID = "ssid";

    private String ssid;
    private String pass;
    private EditText etPass;
    private OnFragmentInteractionListener mListener;

    public GetPassword() {
        // Required empty public constructor
    }

    public static GetPassword newInstance(String ssid) {
        GetPassword fragment = new GetPassword();
        Bundle args = new Bundle();
        args.putString(SSID, ssid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ssid = getArguments().getString(SSID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this passFrag
        View v = inflater.inflate(R.layout.fragment_get_password, container, false);
        TextView tvssid = (TextView) v.findViewById(R.id.tvSsid);
        tvssid.setText(ssid);
        etPass = (EditText) v.findViewById(R.id.etPass);
        v.findViewById(R.id.bDonePass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = etPass.getText().toString();
                mListener.onDoneClicked(pass);
            }
        });
        return v;
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
        void onDoneClicked(String pass);
    }
}
