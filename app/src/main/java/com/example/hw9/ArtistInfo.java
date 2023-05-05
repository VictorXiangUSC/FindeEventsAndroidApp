package com.example.hw9;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistInfo extends Fragment {

    private RecyclerView.Adapter mAdapter;
    List<Artist> artistList = new ArrayList<>();
    ProgressBar waitArtistProgressBar;
    public ArtistInfo() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        artistList = new ArrayList<>();
        View root = inflater.inflate(R.layout.fragment_artist_info, container, false);
        EventDetails eventActivity = (EventDetails) getActivity();
        System.out.println("getting activity");
        waitArtistProgressBar = root.findViewById(R.id.waitArtistProgressBar);
        waitArtistProgressBar.setVisibility(View.VISIBLE);
        JSONObject mJson = eventActivity.getEventInfo();

        try {
            String genres = mJson.getString("genres");
            if(!genres.contains("Music")){
                waitArtistProgressBar.setVisibility(View.GONE);
                TextView noResults =  root.findViewById(R.id.noArtists);
                noResults.setVisibility(View.VISIBLE);
                noResults.setGravity(Gravity.CENTER);
                return root;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray artistTeams;
        try {
            artistTeams = mJson.getJSONArray("artistTeams");
        } catch (JSONException e) {
            TextView no_results =  root.findViewById(R.id.noArtists);
            no_results.setVisibility(View.VISIBLE);
            no_results.setGravity(Gravity.CENTER);
            e.printStackTrace();
            return root;
        }
        for(int i = 0; i < artistTeams.length(); i++){
            try {
                String curArtist = artistTeams.getString(i);
                requestArtist(curArtist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RecyclerView recyclerView = root.findViewById(R.id.lv_artist_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ArtistsRecyclerViewAdapter(artistList, getContext());
        recyclerView.setAdapter(mAdapter);
        return root;
    }

    public void requestArtist(String artist){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        String requestArtistUrl = "https://myfirstproject-377018.wl.r.appspot.com/appartist?artist="+artist;
        System.out.println("requestArtist " + requestArtistUrl);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestArtistUrl, null, response -> {
            System.out.println("Response in artistInfo" + response);
            waitArtistProgressBar.setVisibility(View.GONE);
            try {
                String name = response.getString("name");
                String photoUrl = response.getString("photoUrl");
                String followers = response.getString("followers");
                String popularity = response.getString("popularity");
                String spotifyLink = response.getString("spotifyLink");
                JSONArray albumUrlsJsonArr = response.getJSONArray("albumUrls");
                String[] albumUrls = new String[albumUrlsJsonArr.length()];
                for(int i = 0; i < albumUrlsJsonArr.length(); i++)
                    albumUrls[i] = albumUrlsJsonArr.getString(i);
                if(name.equals(""))
                    return;
                Artist item = new Artist(name, photoUrl, followers, popularity, spotifyLink, albumUrls);
                artistList.add(item);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> error.printStackTrace());
        mQueue.add(request);
    }
}