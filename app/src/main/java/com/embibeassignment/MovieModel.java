package com.embibeassignment;

public class MovieModel {
    public MovieModel(String title, String imageUrl, String release_date, String id, float rating) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.year = release_date;
        this.id = id;
        this.rating = rating;
    }

    String title, imageUrl, year;
    String id; float rating;

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
