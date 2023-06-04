package com.example.locationtrackerapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.locationtrackerapp.entities.User;
import com.example.locationtrackerapp.entities.UserFriend;
import com.example.locationtrackerapp.entities.UserFriendRequest;
import com.example.locationtrackerapp.utils.LocationTrackerAppUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUserService {
    private static final String TAG = "FirebaseUserService";
    private final DatabaseReference mDatabase;
    private final Context context;

    public FirebaseUserService(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void storeUserInFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        User userData = new User(currentUser.getUid(),
                currentUser.getDisplayName(),
                currentUser.getEmail());

        DatabaseReference usersRef = mDatabase.child("users");

        usersRef.child(currentUser.getUid()).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User stored in Firebase.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing user in Firebase: " + e.getMessage());
                });
    }

    // Get all registered users except current user
    public void getAllUsers(UserCallback callback) {
        DatabaseReference usersRef = mDatabase.child("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && !user.getUuid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userList.add(user);
                    }
                }
                callback.onUsersLoaded(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error retrieving user data: " + databaseError.getMessage());
                callback.onDataCancelled(databaseError.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onUsersLoaded(List<User> userList);

        void onDataCancelled(String errorMessage);
    }

    public void loadCurrentUserByUuid(String uuid) {
        DatabaseReference usersRef = mDatabase.child("users").child(uuid);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        LocationTrackerAppUtils.setCurrentUser(user);
                    }
                } else {
                    Log.d(TAG, "User with UUID " + uuid + " not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while retrieving user data
                Log.e(TAG, "Error retrieving user data: " + databaseError.getMessage());
            }
        });
    }


    public void sendFriendRequest(String receiverUuid) {
        DatabaseReference usersRef = mDatabase.child("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference receiverUserRef = usersRef.child(receiverUuid);
        UserFriendRequest friendRequest = new UserFriendRequest(currentUser.getUid(),
                UserFriendRequest.STATUS_PENDING);
        String requestId = receiverUserRef.child("friendRequests").push().getKey();
        receiverUserRef.child("friendRequests").child(requestId).setValue(friendRequest)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Friend request sent successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error sending friend request: " + e.getMessage());
                });

    }

    public void rejectFriendRequest(String requestUuid) {
        DatabaseReference friendRequestRef = mDatabase.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("friendRequests")
                .child(requestUuid);

        friendRequestRef.child("status").setValue(UserFriendRequest.STATUS_REJECTED)
                .addOnSuccessListener(aVoid -> {
                    LocationTrackerAppUtils.getCurrentUser().getFriendRequests()
                            .get(requestUuid)
                            .setStatus(UserFriendRequest.STATUS_REJECTED);

                    Toast.makeText(context, "Friend request rejected successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error rejecting friend request: " + e.getMessage());
                });

    }

    public void acceptFriendRequest(String requestUuid, String senderUuid) {
        DatabaseReference friendRequestRef = mDatabase.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("friendRequests")
                .child(requestUuid);

        friendRequestRef.child("status").setValue(UserFriendRequest.STATUS_ACCEPTED)
                .addOnSuccessListener(aVoid -> {
                    LocationTrackerAppUtils.getCurrentUser().getFriendRequests()
                            .get(requestUuid)
                            .setStatus(UserFriendRequest.STATUS_ACCEPTED);
                    addFriend(senderUuid);
                    Toast.makeText(context, "Friend request accepted successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error accepting friend request: " + e.getMessage());
                });

    }

    public void addFriend(String senderUuid) {
        DatabaseReference usersRef = mDatabase.child("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference senderUserRef = usersRef.child(senderUuid);
        UserFriend senderFriend = new UserFriend(currentUser.getUid(), UserFriend.STATUS_CONNECTED);
        String requestId = senderUserRef.child("friends").push().getKey();
        senderUserRef.child("friends").child(requestId).setValue(senderFriend)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Friend connected.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error connecting with friend: " + e.getMessage());
                });


        DatabaseReference currentUserRef = usersRef.child(currentUser.getUid());
        UserFriend currentFriend = new UserFriend(senderUuid, UserFriend.STATUS_CONNECTED);
        String request2Id = currentUserRef.child("friends").push().getKey();
        currentUserRef.child("friends").child(request2Id).setValue(currentFriend)
                .addOnSuccessListener(aVoid -> {
                    LocationTrackerAppUtils.getCurrentUser().getFriends()
                            .put(request2Id, currentFriend);

                    Toast.makeText(context, "Friend connected.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error connecting with friend: " + e.getMessage());
                });

    }


}
