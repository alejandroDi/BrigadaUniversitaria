package com.example.brigadauniversitaria.loginModule.model.dataAccess;

import android.icu.lang.UScript;

import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.EventErrorTypeListener;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.loginModule.events.LoginEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void registerUser(User user){
        Map<String,Object> values = new HashMap<>();
        values.put(User.USERNAME,user.getUsername());
        values.put(User.EMAIL,user.getEmail());
        values.put(User.PHOTO_URL,user.getPhotoUrl());

        mDatabaseAPI.getUserReferenceByUid(user.getUid()).updateChildren(values);
    }

    public void checkUseExist(String uid, EventErrorTypeListener listener){
        mDatabaseAPI.getUserReferenceByUid(uid).child(User.EMAIL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            listener.onError(LoginEvent.USER_NOT_EXIST, R.string.login_error_user_exist);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        listener.onError(LoginEvent.ERROR_SERVER, R.string.login_message_error);
                    }
                });
    }
}
