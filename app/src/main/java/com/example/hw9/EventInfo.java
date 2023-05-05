package com.example.hw9;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class EventInfo extends Fragment {

    TextView artistTeamStr;
    TextView venue;
    TextView date;
    TextView time;
    TextView genres;
    TextView priceRange;
    TextView ticketStatus;
    TextView buyTicketUrl;
    ProgressBar waitEventProgressBar;
    public EventInfo() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event_info, container, false);
        EventDetails eventActivity = (EventDetails) getActivity();
        waitEventProgressBar = root.findViewById(R.id.waitEventProgressBar);
        waitEventProgressBar.setVisibility(View.VISIBLE);
        JSONObject mJson = eventActivity.getEventInfo();
        System.out.println("mJson" + mJson);
        artistTeamStr = root.findViewById(R.id.artistTeamStr);
        venue = root.findViewById(R.id.venue);
        date = root.findViewById(R.id.date);
        time = root.findViewById(R.id.time);
        genres = root.findViewById(R.id.genres);
        priceRange = root.findViewById(R.id.priceRange);
        ticketStatus = root.findViewById(R.id.ticketStatus);
        buyTicketUrl = root.findViewById(R.id.buyTicketUrl);
        ImageView seatMap = root.findViewById(R.id.seatMap);
        try {
            waitEventProgressBar.setVisibility(View.GONE);
            artistTeamStr.setText(mJson.getString("artistTeamStr"));
            artistTeamStr.post(() -> artistTeamStr.setSelected(true));
            venue.setText(mJson.getString("venue"));
            venue.post(() -> venue.setSelected(true));
            date.setText(mJson.getString("date"));
            time.setText(mJson.getString("time"));
            genres.setText(mJson.getString("genres"));
            genres.post(() -> genres.setSelected(true));
            priceRange.setText(mJson.getString("priceRange"));
            priceRange.post(() -> priceRange.setSelected(true));
            ticketStatus.setText(mJson.getString("ticketStatus"));
            String ticketColor = mJson.getString("ticketColor");
            switch (ticketColor) {
                case "green":
                    ticketStatus.setBackgroundResource(R.drawable.badge_green);
                    break;
                case "red":
                    ticketStatus.setBackgroundResource(R.drawable.badge_red);
                    break;
                case "black":
                    ticketStatus.setBackgroundResource(R.drawable.badge_black);
                    break;
                case "orange":
                    ticketStatus.setBackgroundResource(R.drawable.badge_orange);
                    break;
            }
            buyTicketUrl.setText(mJson.getString("buyTicketUrl"));
            Linkify.addLinks(buyTicketUrl, Linkify.ALL);
            buyTicketUrl.post(() -> buyTicketUrl.setSelected(true));
            String seatMapUrl =mJson.getString("seatMapUrl");
            Picasso.get().load(seatMapUrl.isEmpty() ? null : seatMapUrl).error(R.drawable.logo).resize(200, 200).centerCrop().into(seatMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root;
    }
}