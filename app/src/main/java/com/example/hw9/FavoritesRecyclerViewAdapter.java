package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.MyViewHolder> {
    List<Event> eventList;
    Context context;
    TextView noResults;

    public FavoritesRecyclerViewAdapter(List<Event> eventList, Context context, TextView noResults) {
        this.eventList = eventList;
        this.context = context;
        this.noResults = noResults;
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


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_event_name.setText(eventList.get(position).getEventName());
        holder.tv_venue.setText(eventList.get(position).getVenue());
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
            holder.iv_fav_image.setImageResource(R.drawable.heart_outline_black);
        }

        holder.iv_fav_image.setOnClickListener(v -> {
            SharedPreferences spClick = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorClick = spClick.edit();
            String spClickEventID = eventList.get(position).getEventId();
            String spClickEventName = eventList.get(position).getEventName();

            eventList.remove(position);
            Toast.makeText(context,spClickEventName + " removed from favorites",Toast.LENGTH_SHORT).show();

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventList.size());

            editorClick.remove(spClickEventID);
            editorClick.apply();

            if(eventList.size() == 0) {
                noResults.setVisibility(View.VISIBLE);
                noResults.setGravity(Gravity.CENTER);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

}
