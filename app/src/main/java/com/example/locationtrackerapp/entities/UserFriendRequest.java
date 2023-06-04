package com.example.locationtrackerapp.entities;

public class UserFriendRequest {
    public static final int STATUS_REJECTED = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_PENDING = 2;
    private String senderUUID;
    private int status = STATUS_REJECTED;

    public UserFriendRequest() {
    }

    public UserFriendRequest(String senderUUID, int status) {
        this.senderUUID = senderUUID;
        this.status = status;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
