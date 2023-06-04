package com.example.locationtrackerapp.entities;

public class UserFriend {
    public static final int STATUS_NOT_CONNECTED = 0;
    public static final int STATUS_CONNECTED = 1;

    private String uuid;
    private int status = STATUS_NOT_CONNECTED;

    public UserFriend() {
    }

    public UserFriend(String uuid, int status) {
        this.uuid = uuid;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
