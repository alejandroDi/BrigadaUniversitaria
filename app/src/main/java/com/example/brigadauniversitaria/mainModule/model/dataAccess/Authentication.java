package com.example.brigadauniversitaria.mainModule.model.dataAccess;

import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseAuthenticationAPPI;

public class Authentication {
    private FirebaseAuthenticationAPPI mAuthenticationAPI;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPPI.getInstance();

    }

    public FirebaseAuthenticationAPPI getmAuthenticationAPI() {
        return mAuthenticationAPI;
    }

    public void singOff(){
        mAuthenticationAPI.getmFirebaseAuth().signOut();
    }
}
