package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Favorites extends Fragment {
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    View rootView;
    TextView noResults;

    public Favorites() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites,container, false);
        recyclerView = rootView.findViewById(R.id.favoritesList);
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            List<Event> eventList = new ArrayList<Event>();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            sp = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
            spEditor = sp.edit();
            JSONObject jsonObject;
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            try{
                Map<String, ?> allEntries = sp.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    jsonObject = new JSONObject(entry.getValue().toString());
                    String date = jsonObject.getString("date");
                    String time = jsonObject.getString("time");
                    String eventName = jsonObject.getString("eventName");
                    String eventId = jsonObject.getString("eventId");
                    String url = jsonObject.getString("url");
                    String category = jsonObject.getString("category");
                    String venue = jsonObject.getString("venue");

                    Event item = new Event(date,time, eventName,eventId, url,category,venue);
                    eventList.add(item);
                }
                noResults =  rootView.findViewById(R.id.noFavorites);
                RecyclerView.Adapter mAdapter = new FavoritesRecyclerViewAdapter(eventList, getContext(), noResults);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (eventList.size() == 0){
                noResults.setVisibility(View.VISIBLE);
                noResults.setGravity(Gravity.CENTER);
            }else{
                noResults.setVisibility(View.GONE);
            }
        }
    }


}