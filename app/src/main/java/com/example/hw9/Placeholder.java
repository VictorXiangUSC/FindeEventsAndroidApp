package com.example.hw9;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.databinding.FragmentMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Placeholder extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
    private Spinner category;
    private CheckBox autoDetect;
    private EditText location;
    private AutoCompleteTextView keyword;
    ArrayAdapter<String> autoSuggestAdapter;
    private EditText distance;
    FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue mQueue;
    private double lat;
    private double lng;
    ProgressBar waitEventsProgressBar;
    public static Placeholder newInstance(int index) {
        Placeholder fragment = new Placeholder();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
            getLocationPermission();
            View rootView = inflater.inflate(R.layout.fragment_search,container, false);
            Button search = rootView.findViewById(R.id.search);

            keyword = rootView.findViewById(R.id.keyword);
            autoSuggestAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
            keyword.setThreshold(1);
            keyword.setAdapter(autoSuggestAdapter);
            keyword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    String query = s.toString();
                    if (query.length() >= 1) {
                        requestAutocomplete(query);
                    }
                }
            });

            autoDetect = rootView.findViewById(R.id.autoDetect);
            location = rootView.findViewById(R.id.location);
            autoDetect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    location.setVisibility(View.GONE);
                } else {
                    location.setVisibility(View.VISIBLE);
                }
            });

            category = rootView.findViewById(R.id.category);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.categoryEntries, R.layout.spinner_item);
            category.setAdapter(adapter);
            distance =  rootView.findViewById(R.id.distance);

            search.setOnClickListener(v -> {
                boolean valid = true;
                String locationVal = location.getText().toString().trim();
                String keywordVal = keyword.getText().toString().trim();

                int spinnerPos = category.getSelectedItemPosition();
                String[] SegmentIDs = getResources().getStringArray(R.array.categoryValues);
                String categoryVal = String.valueOf(SegmentIDs[spinnerPos]);

                String distanceVal = distance.getText().toString().trim();

                if(keywordVal.isEmpty()){
                    valid = false;
                    keyword.setError("Please enter keyword");
                }else {
                    keyword.setError(null);
                }
                if(!autoDetect.isChecked() && locationVal.isEmpty()){
                    valid = false;
                    location.setError("Please enter location");
                }else{
                    location.setError(null);
                }

                if(valid) {
                    waitEventsProgressBar = rootView.findViewById(R.id.waitEventsProgressBar);
                    waitEventsProgressBar.setVisibility(View.VISIBLE);
                    if (!autoDetect.isChecked()){
                        requestLatLngBeforeSearch(keywordVal, categoryVal,distanceVal,locationVal);
                    }
                    else {
                        requestEvents(keywordVal, categoryVal, distanceVal);
                    }
                }else{
                    Toast.makeText(getActivity(), "Please enter keyword/location", Toast.LENGTH_SHORT).show();
                }
            });

            Button clear = rootView.findViewById(R.id.clear);
            clear.setOnClickListener(v -> {
                keyword.setError(null);
                location.setError(null);
                keyword.setText("");
                location.setText("");
                distance.setText("10");
                category.setSelection(0);
                autoDetect.setChecked(false);
                location.setText("");
            });

            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            return rootView;
        }

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }

    public void requestAutocomplete(String keyword_val){
        mQueue = Volley.newRequestQueue(getContext());
        String requestAutocompleteUrl = "https://myfirstproject-377018.wl.r.appspot.com/appautocomplete?keyword="+keyword_val;
        System.out.println("requestAutocompleteUrl "+ requestAutocompleteUrl);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, requestAutocompleteUrl, null, response -> {
            System.out.println("Response in Autocomplete "+response);
            List<String> suggestions = new ArrayList<>();
            for(int i = 0; i < response.length(); i++) {
                try {
                    suggestions.add(response.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            autoSuggestAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggestions);
            keyword.setAdapter(autoSuggestAdapter);
            autoSuggestAdapter.notifyDataSetChanged();
        }, error -> error.printStackTrace());
        mQueue.add(request);
    }

    public void requestEvents(String keyword_val, String category_val, String distance_val){
        mQueue = Volley.newRequestQueue(getContext());
        String requestEventsUrl = "https://myfirstproject-377018.wl.r.appspot.com/appevents?keyword="+keyword_val+"&category="+category_val+"&distance="+distance_val+"&latitude="+lat+"&longitude="+lng;
        System.out.println("requestEventsUrl "+ requestEventsUrl);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, requestEventsUrl, null, response -> {
            System.out.println("Response in Events" + response);
            waitEventsProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), Events.class);
            intent.putExtra("events",  response.toString());
            startActivity(intent);
        }, error -> error.printStackTrace());
        mQueue.add(request);
    }

    public void requestLatLngBeforeSearch(String keyword, String category, String distance, String location){
        mQueue = Volley.newRequestQueue(getContext());
        String requestEventsUrl = "https://myfirstproject-377018.wl.r.appspot.com/applocation?location="+location;
        System.out.println("requestLatLngBeforeSearch");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestEventsUrl, null, response -> {
            System.out.println("Response in LatLng"+response);
            try {
                lat = Double.parseDouble(Util.getProperty(response, "lat"));
                lng = Double.parseDouble(Util.getProperty(response, "lng"));
                System.out.println("lat " + lat + " lng " + lng);
                requestEvents(keyword, category, distance);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> error.printStackTrace());
        mQueue.add(request);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode ==100 && (grantResults.length>0)&&(grantResults[0]+grantResults[1] ==PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }else {
            System.out.println("Location Validation "+requestCode);
        }
    }
    public void getLocationPermission(){
        if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                } else {
                    LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000)
                            .setFastestInterval(1000).setNumUpdates(1);
                    LocationCallback locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                            Location location2 = locationResult.getLastLocation();
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    };
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                }
            });
        }else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}