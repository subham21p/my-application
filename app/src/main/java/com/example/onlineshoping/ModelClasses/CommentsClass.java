package com.example.onlineshoping.ModelClasses;

public class CommentsClass {

    String profile_pic;
    String email;
    String name;
    int items_id;
    double rating;
    String comment;
    String date;

    public CommentsClass(String profile_pic, String email, String name, int items_id, double rating, String comment, String date) {
        this.profile_pic = profile_pic;
        this.email = email;
        this.name = name;
        this.items_id = items_id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }


    public String getProfile_pic() {
        return profile_pic;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getItems_id() {
        return items_id;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
