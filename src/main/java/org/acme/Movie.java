package org.acme;

public class Movie {

    private Integer id;
    private String title;
    private String rating;
    private int duration;

    public Movie(Integer id, String title, String rating, int duration) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public int getDuration() {
        return duration;
    }
}
