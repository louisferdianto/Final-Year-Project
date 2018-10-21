package com.example.louisferdianto.app1.Models;

import com.example.louisferdianto.app1.R;

/**
 * Created by louisferdianto on 16/01/2018.
 */

public class Event {

    public String title;
    public String location;
    public String date;
    public String timeStart;
    public String timeEnd;
    public String categories;
    public String price;
    public String desc;
    public double longitude,latitude;
    public String imageUrl;
    public String participant;

    public Event(){}

    public Event(String photo,String title, String location,String participant, String date, String timeStart, String timeEnd, String categories, String price, String desc, double la,double lo) {
        this.imageUrl = photo;
        this.title = title;
        this.participant = participant;
        this.location = location;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.categories = categories;
        this.price = price;
        this.desc = desc;
        latitude = la;
        longitude = lo;
    }
    public String getPhoto() {
        return imageUrl;
    }

    public void setPhoto(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }
}
