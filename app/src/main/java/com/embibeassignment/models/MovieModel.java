package com.embibeassignment.models;

public class MovieModel {

    public MovieModel(String title, String imageUrl, String release_date, String id, float rating, String overview, String genre) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.year = release_date;
        this.id = id;
        this.rating = rating;
        this.overview = overview;
        this.genre = genre;
    }

    public String title;
    public String imageUrl;
    public String year;
    public String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String overview;
    public String id;
    public float rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
