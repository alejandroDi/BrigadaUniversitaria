package com.example.brigadauniversitaria.loginModule.model.dataAccess;

import com.google.firebase.auth.FirebaseUser;

public interface StatusAuthCallback {
    void onGetUser(FirebaseUser user);
    void onLauchUILogin();
}
