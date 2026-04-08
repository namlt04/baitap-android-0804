package com.example.a0804.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String title;
    private String posterUrl;
    private String description;
    private String genre;
    private double rating;

    public Movie() {
        // Required for Firebase
    }

    public Movie(String id, String title, String posterUrl, String description, String genre, double rating) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
