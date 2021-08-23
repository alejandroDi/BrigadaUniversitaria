package com.example.brigadauniversitaria.loginModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseAuthenticationAPPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Authentication {
    private FirebaseAuthenticationAPPI mAuthenticationAPI;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPPI.getInstance();
    }

    public void  onResume(){
        mAuthenticationAPI.getmFirebaseAuth().addAuthStateListener(mAuthStateListener);
    }

    public void onPause(){
        if (mAuthStateListener != null){
            mAuthenticationAPI.getmFirebaseAuth().removeAuthStateListener(mAuthStateListener);
        }
    }

    public void getStatusAuth(StatusAuthCallback callback){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    callback.onGetUser(user);
                }else {
                    callback.onLauchUILogin();
                }
            }
        };
    }

    public User getCurrentUser(){
        return mAuthenticationAPI.getAuthUser();
    }
}
