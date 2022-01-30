package com.example.brigadauniversitaria;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BrigadaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        configFirebase();
    }

    private void configFirebase(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
