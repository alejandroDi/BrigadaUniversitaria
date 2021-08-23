package com.example.brigadauniversitaria.common.model.dataAccess;

import com.example.brigadauniversitaria.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthenticationAPPI {
    private FirebaseAuth mFirebaseAuth;

    private static class SingletonHolder{

        private static final FirebaseAuthenticationAPPI INSTANCE = new FirebaseAuthenticationAPPI();

    }
    public static FirebaseAuthenticationAPPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    private FirebaseAuthenticationAPPI() {
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getmFirebaseAuth() {
        return this.mFirebaseAuth;
    }

    public User getAuthUser() {
        User user = new User();
        if (mFirebaseAuth != null && mFirebaseAuth.getCurrentUser() != null){
            user.setUid(mFirebaseAuth.getCurrentUser().getUid());
            user.setUsername(mFirebaseAuth.getCurrentUser().getDisplayName());
            user.setEmail(mFirebaseAuth.getCurrentUser().getEmail());
            user.setUri(mFirebaseAuth.getCurrentUser().getPhotoUrl());
        }
        return user;
    }
}
