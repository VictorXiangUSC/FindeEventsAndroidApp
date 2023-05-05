package com.example.hw9;

import androidx.annotation.NonNull;

public class Event {
    private String date;
    private String time;
    private String eventName;
    private String eventId;
    private String url;
    private String category;
    private String venue;

    @NonNull
    @Override
    public String toString() {
        return  "{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventId='" + eventId + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", venue='" + venue + '\'' +
                '}';
    }

    public Event(String date, String time, String eventName, String eventId, String url, String category, String venue) {
        this.date = date;
        this.time = time;
        this.eventName = eventName;
        this.eventId = eventId;
        this.url = url;
        this.category = category;
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }


    public String getEventName() {
        return eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public String getCategory() {
        return category;
    }

    public String getVenue() {
        return venue;
    }

    public String getUrl() {
        return url;
    }
}

