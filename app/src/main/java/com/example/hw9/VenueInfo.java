package com.example.hw9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class VenueInfo extends Fragment implements OnMapReadyCallback {

    public VenueInfo() {}
    MapView mapView;
    double lat, lng;
    TextView name;
    TextView address;
    TextView city;
    TextView phoneNumber;
    TextView openHours;
    TextView generalRule;
    TextView childRule;
    ProgressBar waitVenueProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_venue_info, container, false);
        EventDetails eventActivity = (EventDetails) getActivity();
        waitVenueProgressBar = root.findViewById(R.id.waitVenueProgressBar);
        waitVenueProgressBar.setVisibility(View.VISIBLE);
        JSONObject mjson = eventActivity.getEventInfo();
        try {
            requestVenueDetails(root, mjson.getString("venue"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mapView = root.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        return root;
    }
    public void requestVenueDetails(View root, String venueName){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        String requestVenueDetailsUrl = "https://myfirstproject-377018.wl.r.appspot.com/appvenuedetails?keyword="+venueName;
        System.out.println("requestVenueDetails" + requestVenueDetailsUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestVenueDetailsUrl, null, response -> {
            System.out.println("Response in VenueInfo" + response);
            name = root.findViewById(R.id.name);
            address = root.findViewById(R.id.address);
            city = root.findViewById(R.id.city);
            phoneNumber = root.findViewById(R.id.phoneNumber);
            openHours = root.findViewById(R.id.openHours);
            generalRule = root.findViewById(R.id.generalRule);
            childRule = root.findViewById(R.id.childRule);

            try {
                name.setText(response.getString("name"));
                name.post(() -> name.setSelected(true));
                address.setText(response.getString("address"));
                address.post(() -> address.setSelected(true));
                city.setText(response.getString("city"));
                city.post(() -> city.setSelected(true));
                phoneNumber.setText(response.getString("phoneNumber"));
                phoneNumber.post(() -> phoneNumber.setSelected(true));

                openHours.setText(response.getString("openHours"));
                Button btOpenHours = root.findViewById(R.id.openHoursShow);
                btOpenHours.setOnClickListener(v -> {
                    if (btOpenHours.getText().toString().equalsIgnoreCase("Show More"))
                    {
                        openHours.setMaxLines(Integer.MAX_VALUE);
                        btOpenHours.setText("Show Less");
                    }
                    else
                    {
                        openHours.setMaxLines(3);
                        btOpenHours.setText("Show More");
                    }
                });

                generalRule.setText(response.getString("generalRule"));
                Button btGeneralRule = root.findViewById(R.id.generalRuleShow);
                btGeneralRule.setOnClickListener(v -> {
                    if (btGeneralRule.getText().toString().equalsIgnoreCase("Show More"))
                    {
                        generalRule.setMaxLines(Integer.MAX_VALUE);
                        btGeneralRule.setText("Show Less");
                    }
                    else
                    {
                        generalRule.setMaxLines(3);
                        btGeneralRule.setText("Show More");
                    }
                });

                childRule.setText(response.getString("childRule"));
                Button btChildRule = root.findViewById(R.id.childRuleShow);
                btChildRule.setOnClickListener(v -> {
                    if (btChildRule.getText().toString().equalsIgnoreCase("Show More"))
                    {
                        childRule.setMaxLines(Integer.MAX_VALUE);
                        btChildRule.setText("Show Less");
                    }
                    else
                    {
                        childRule.setMaxLines(3);
                        btChildRule.setText("Show More");
                    }
                });

                lat = Double.parseDouble(response.getString("lat"));
                lng = Double.parseDouble(response.getString("lng"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            waitVenueProgressBar.setVisibility(View.GONE);
        }, error -> error.printStackTrace());
        mQueue.add(request);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng markerPlace = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(markerPlace).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(markerPlace));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}