package com.example.hw9;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.MyViewHolder>{
    List<Artist> artistList;
    Context context;

    public ArtistsRecyclerViewAdapter(List<Artist> artistList, Context context) {
        this.artistList = artistList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name;
        TextView followers;
        TextView spotifyLink;
        ProgressBar popularityBar;
        TextView popularityText;
        ImageView album1;
        ImageView album2;
        ImageView album3;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            followers = itemView.findViewById(R.id.followers);
            spotifyLink = itemView.findViewById(R.id.spotifyLink);
            popularityBar = itemView.findViewById(R.id.popularityBar);
            popularityText = itemView.findViewById(R.id.popularityText);
            album1 = itemView.findViewById(R.id.album1);
            album2 = itemView.findViewById(R.id.album2);
            album3 = itemView.findViewById(R.id.album3);

        }
    }


    @NotNull
    @Override
    public ArtistsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ArtistsRecyclerViewAdapter.MyViewHolder holder, int position) {
        String photoUrl = artistList.get(position).getPhotoUrl();
        Picasso.get().load(photoUrl.isEmpty() ? null : photoUrl).error(R.drawable.logo).resize(50, 50).centerCrop().into(holder.photo);
        holder.name.setText(artistList.get(position).getName());
        holder.name.post(() -> holder.name.setSelected(true));
        holder.followers.setText(artistList.get(position).getFollowers());
        holder.spotifyLink.setText(artistList.get(position).getSpotifyLink());
        Linkify.addLinks(holder.spotifyLink, Linkify.ALL);
        holder.spotifyLink.post(() -> holder.spotifyLink.setSelected(true));
        holder.popularityBar.setProgress(Integer.parseInt(artistList.get(position).getPopularity()));
        holder.popularityText.setText(artistList.get(position).getPopularity());
        String albumUrl1 = artistList.get(position).getAlbumUrl1();
        String albumUrl2 = artistList.get(position).getAlbumUrl2();
        String albumUrl3 = artistList.get(position).getAlbumUrl3();
        Picasso.get().load(albumUrl1.isEmpty() ? null : albumUrl1).error(R.drawable.logo).resize(50, 50).centerCrop().into(holder.album1);
        Picasso.get().load(albumUrl2.isEmpty() ? null : albumUrl2).error(R.drawable.logo).resize(50, 50).centerCrop().into(holder.album2);
        Picasso.get().load(albumUrl3.isEmpty() ? null : albumUrl3).error(R.drawable.logo).resize(50, 50).centerCrop().into(holder.album3);
    }


    @Override
    public int getItemCount() {
        return artistList.size();
    }

}
