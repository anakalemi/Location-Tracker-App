package com.example.locationtrackerapp.entities;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String uuid;
    private String name;
    private String email;
    private double latitude;
    private double longitude;
    private Map<String, UserFriend> friends = new HashMap<>();
    private Map<String, UserFriendRequest> friendRequests = new HashMap<>();

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String uuid, String name, String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, UserFriend> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, UserFriend> friends) {
        this.friends = friends;
    }

    public Map<String, UserFriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Map<String, UserFriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
