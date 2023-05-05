package com.example.hw9;

public class Artist {

    private String name;
    private String photoUrl;
    private String followers;
    private String popularity;
    private String spotifyLink;
    private String[] albumUrls;

    public Artist(String name, String photoUrl, String followers, String popularity, String spotifyLink, String[] albumUrls) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.followers = followers;
        this.popularity = popularity;
        this.spotifyLink = spotifyLink;
        this.albumUrls = albumUrls;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getFollowers() {
        return followers;
    }


    public String getName() {
        return name;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getAlbumUrl1() {
        if(albumUrls.length > 0)
            return albumUrls[0];
        return "";
    }

    public String getAlbumUrl2() {
        if(albumUrls.length > 1)
            return albumUrls[1];
        return "";
    }

    public String getAlbumUrl3() {
        if(albumUrls.length > 2)
            return albumUrls[2];
        return "";
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }
}
