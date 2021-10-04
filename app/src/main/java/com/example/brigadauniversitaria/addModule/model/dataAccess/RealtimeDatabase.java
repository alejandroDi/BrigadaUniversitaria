package com.example.brigadauniversitaria.addModule.model.dataAccess;

import android.app.backup.BackupAgent;

import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.addModule.events.AddEvent;
import com.example.brigadauniversitaria.common.model.BasicEventsCallback;
import com.example.brigadauniversitaria.common.model.EventsCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void checkUserExist(String email, EventsCallback callback){
        DatabaseReference usersReference = mDatabaseAPI.getRootReferences()
                .child(FirebaseRealtimeDatabaseAPI.PATH_USERS);
        Query userByEmailQuery = usersReference.orderByChild(User.EMAIL).equalTo(email).limitToFirst(1);
        userByEmailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    callback.onSuccess();
                } else {
                    callback.onError(AddEvent.ERROR_EXIST,R.string.addFriend_error_user_exist);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                callback.onError(AddEvent.ERROR_SERVER,R.string.addFriend_error_message);
            }
        });

    }

    public void checkRequestNotExist(String email,String myUid,EventsCallback callback){
        String emailEncoded = UtilsCommon.getEmailEncoded(email);
        DatabaseReference myRequestReferences = mDatabaseAPI.getRequestReference(emailEncoded).child(myUid);
        myRequestReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    callback.onError(AddEvent.ERROR_EXIST,R.string.addFriend_message_request_exist);
                }else {
                    callback.onSuccess();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                callback.onError(AddEvent.ERROR_SERVER,R.string.addFriend_error_message);

            }
        });

    }
    public void checkContactExist(String email,String myUid,EventsCallback callback){
        DatabaseReference myContactsReferences = mDatabaseAPI.getContactsReferences(myUid);
        Query userByEmailQuery = myContactsReferences.orderByChild(User.EMAIL).equalTo(email).limitToFirst(1);
        userByEmailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    callback.onError(AddEvent.ERROR_EXIST,R.string.friendAdded_message_request_exist);
                }else {
                    callback.onSuccess();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                callback.onError(AddEvent.ERROR_SERVER,R.string.addFriend_error_message);

            }
        });

    }

    public void addFriend(String email,User myUser,BasicEventsCallback callback){
        Map<String, Object> myUserMap = new HashMap<>();
        myUserMap.put(User.USERNAME, myUser.getUsername());
        myUserMap.put(User.EMAIL,myUser.getEmail());
        myUserMap.put(User.PHOTO_URL,myUser.getPhotoUrl());

        final String emailEncoded = UtilsCommon.getEmailEncoded(email);
        DatabaseReference userReference = mDatabaseAPI.getRequestReference(emailEncoded);
        userReference.child(myUser.getUid()).updateChildren(myUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        callback.onError();
                    }
                });
    }
}
