package com.example.brigadauniversitaria;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BrigadaApplication extends Application {
    private RequestQueue mRequestQueue;
    private static BrigadaApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        configFirebase();
        mInstance = this;
    }

    private void configFirebase(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    public static synchronized BrigadaApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getmRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;

    }

    public <T> void addToQueue(Request<T> request){
        request.setRetryPolicy(new DefaultRetryPolicy(1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getmRequestQueue().add(request);
    }
}
