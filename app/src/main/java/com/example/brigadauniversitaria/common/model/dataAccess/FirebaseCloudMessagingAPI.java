package com.example.brigadauniversitaria.common.model.dataAccess;


import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import okhttp3.internal.Util;

public class FirebaseCloudMessagingAPI {
    private FirebaseMessaging mFirebaseMessaging;

    private static class SingletonHolder{
        private static final FirebaseCloudMessagingAPI INSTANCE = new FirebaseCloudMessagingAPI();
    }

    public static FirebaseCloudMessagingAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public FirebaseCloudMessagingAPI() {
        this.mFirebaseMessaging = FirebaseMessaging.getInstance();
    }

    ////metodos

    public void subscribeToMyTopic(String myEmail){
        mFirebaseMessaging.subscribeToTopic(UtilsCommon.getEmailToTopic(myEmail))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            //reitentar y notificar
                        }
                    }
                });
    }

    public void unsubscribeToMyTopic(String myEmail){
        mFirebaseMessaging.unsubscribeFromTopic(UtilsCommon.getEmailToTopic(myEmail))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            //reintentar
                        }
                    }
                });
    }
}
