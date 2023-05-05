package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.MyViewHolder> {
    List<Event> eventList;
    Context context;

    public EventsRecyclerViewAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cat_image;
        ImageView iv_fav_image;
        TextView tv_event_name;
        TextView tv_venue;
        TextView tv_category;
        TextView tv_date;
        TextView tv_time;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv_fav_image = itemView.findViewById(R.id.favoriteIcon);
            iv_cat_image = itemView.findViewById(R.id.eventImage);
            tv_event_name = itemView.findViewById(R.id.eventName);
            tv_venue = itemView.findViewById(R.id.venue);
            tv_category = itemView.findViewById(R.id.eventCategory);
            tv_date = itemView.findViewById(R.id.eventDate);
            tv_time = itemView.findViewById(R.id.eventTime);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_event_name.setText(eventList.get(position).getEventName());
        holder.tv_event_name.post(() -> holder.tv_event_name.setSelected(true));
        holder.tv_venue.setText(eventList.get(position).getVenue());
        holder.tv_venue.post(() -> holder.tv_venue.setSelected(true));
        holder.tv_category.setText(eventList.get(position).getCategory());
        holder.tv_date.setText(eventList.get(position).getDate());
        holder.tv_time.setText(eventList.get(position).getTime());
        String imageUrl = eventList.get(position).getUrl();
        Picasso.get().load(imageUrl.isEmpty() ? null : imageUrl).error(R.drawable.logo).resize(50, 50).centerCrop().into(holder.iv_cat_image);

        SharedPreferences sp = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        String spEventID = eventList.get(position).getEventId();
        if (sp.contains(spEventID)) {
            holder.iv_fav_image.setImageResource(R.drawable.heart_fill_red);
        } else {
            holder.iv_fav_image.setImageResource(R.drawable.heart_outline_white);
        }

        holder.iv_fav_image.setOnClickListener(v -> {
            SharedPreferences spClick = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorClick = spClick.edit();
            String spClickEventID = eventList.get(position).getEventId();
            String spClickEventName = eventList.get(position).getEventName();

            if (spClick.contains(spClickEventID)) {
                holder.iv_fav_image.setImageResource(R.drawable.heart_outline_white);
                editorClick.remove(spClickEventID);
                Toast.makeText(context,spClickEventName + " removed from favorites",Toast.LENGTH_SHORT).show();

                editorClick.commit();
            } else {
                holder.iv_fav_image.setImageResource(R.drawable.heart_fill_red);
                editorClick.putString(spClickEventID, eventList.get(position).toString());
                Toast.makeText(context,spClickEventName + " added to favorites",Toast.LENGTH_SHORT).show();

                editorClick.apply();
            }
        });

        holder.tv_event_name.setOnClickListener(v ->
                requestEventDetails(eventList.get(position).getEventId(),
                        eventList.get(position).getEventName(),position));
    }

    private void requestEventDetails(String event_id, String event_name, int position) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = "https://myfirstproject-377018.wl.r.appspot.com/appeventdetails?id="+event_id;
        System.out.println("requestEventDetails "+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            System.out.println("Response in eventDetails "+response);
            Intent intent = new Intent(context, EventDetails.class);
            intent.putExtra("eventDetails" ,response.toString());
            intent.putExtra("eventName",event_name);
            intent.putExtra("eventId",event_id);
            intent.putExtra("eventObj", eventList.get(position).toString());

            context.startActivity(intent);
        }, error -> error.printStackTrace());
        mQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }



}
