package com.example.hw9;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Events extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView noResults;
    List<Event> eventList = new ArrayList<>();



    @Override
    protected void onRestart(){
        super.onRestart();
        eventList = new ArrayList<>();
        try {
            JSONArray mJsonArray = new JSONArray(getIntent().getStringExtra("events"));
            System.out.println(mJsonArray);
            recyclerView = findViewById(R.id.lv_event_list);
            recyclerView.setHasFixedSize(true);
            fillEventsList(mJsonArray);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new EventsRecyclerViewAdapter(eventList, this);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eventList.size() == 0) {
            noResults = findViewById(R.id.noEvents);
            noResults.setVisibility(View.VISIBLE);
            noResults.setGravity(Gravity.CENTER);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_events);


        eventList = new ArrayList<>();
        try {
            JSONArray mJsonArray = new JSONArray(getIntent().getStringExtra("events"));
            System.out.println(mJsonArray);
            recyclerView = findViewById(R.id.lv_event_list);
            recyclerView.setHasFixedSize(true);
            fillEventsList(mJsonArray);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new EventsRecyclerViewAdapter(eventList, this);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eventList.size() == 0){
            noResults =  findViewById(R.id.noEvents);
            noResults.setVisibility(View.VISIBLE);
            noResults.setGravity(Gravity.CENTER);
        }
    }

    private void fillEventsList(JSONArray mJsonArray) {
        try {

            System.out.println(mJsonArray);
            for(int i = 0;i<mJsonArray.length();i++){
                JSONObject jsonobject = mJsonArray.getJSONObject(i);

                String eventName = jsonobject.getString("eventName");
                String eventId = jsonobject.getString("eventId");
                String date = jsonobject.getString("date");
                String time = jsonobject.getString("time");
                String url = jsonobject.getString("url");
                String category = jsonobject.getString("category");
                String venue = jsonobject.getString("venue");
                Event item = new Event(date,time, eventName,eventId, url,category,venue);
                eventList.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}