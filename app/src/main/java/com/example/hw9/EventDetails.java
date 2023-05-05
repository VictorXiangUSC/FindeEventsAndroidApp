package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetails extends AppCompatActivity {

    JSONObject mJson;

    TabLayout tabLayout;
    private final int[] tabIcons = {
            R.drawable.info,
            R.drawable.artist,
            R.drawable.venue
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        String eventName = getIntent().getStringExtra("eventName");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(eventName);
        actionBar.setDisplayHomeAsUpEnabled(true);


        SectionsPagerAdapter1 mSectionsPagerAdapter = new SectionsPagerAdapter1(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.view_pager1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        try {
            mJson = new JSONObject(getIntent().getStringExtra("eventDetails"));
            System.out.println(mJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getEventInfo(){
        return mJson;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

    }


    public static class SectionsPagerAdapter1 extends FragmentPagerAdapter {

        public SectionsPagerAdapter1(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new EventInfo();
                case 1:
                    return new ArtistInfo();
                case 2:
                    return new VenueInfo();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EVENTS";
                case 1:
                    return "ARTIST(S)";
                case 2:
                    return "VENUE";
            }
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SharedPreferences sp = this.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        String spEventId = getIntent().getStringExtra("eventId");
        try{
            if (sp.contains(spEventId)) {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_fill_white);

            } else {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_outline_white);
            }}catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.facebook_button:
                postFacebook();
                return true;
            case R.id.twit_button:
                postTwitter();
                return true;
            case R.id.fav_button:
                setFavorite(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavorite(MenuItem menu) {
        SharedPreferences spClick = this.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorClick = spClick.edit();
        String spClickEventID = getIntent().getStringExtra("eventId");
        String spClickEventName = getIntent().getStringExtra("eventName");
        try{
            if (spClick.contains(spClickEventID)) {
                menu.setIcon(R.drawable.heart_outline_white);
                editorClick.remove(spClickEventID);
                Toast.makeText(getApplicationContext(),spClickEventName + " removed from favorites",Toast.LENGTH_SHORT).show();
                editorClick.apply();

            } else {
                menu.setIcon(R.drawable.heart_fill_white);
                String event_obj = getIntent().getStringExtra("eventObj");
                editorClick.putString(spClickEventID, event_obj);
                Toast.makeText(getApplicationContext(),spClickEventName + " added to favorites",Toast.LENGTH_SHORT).show();
                editorClick.apply();
            }}catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void postFacebook() {
        try {

            String buyTicketUrl = mJson.getString("buyTicketUrl");
            String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + buyTicketUrl;
            System.out.println(facebookUrl);
            Uri uriUrl = Uri.parse(facebookUrl);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void postTwitter() {
        try {

            String eventName = mJson.getString("name");
            String buyTicketUrl = mJson.getString("buyTicketUrl");
            String twitterURL = "https://twitter.com/intent/tweet?text=Checkout " + eventName
                    + " at " + buyTicketUrl;

            System.out.println(twitterURL);
            Uri uriUrl = Uri.parse(twitterURL);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }catch (JSONException e){
            e.printStackTrace();

        }
    }

}