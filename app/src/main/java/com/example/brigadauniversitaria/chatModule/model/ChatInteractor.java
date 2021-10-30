package com.example.brigadauniversitaria.chatModule.model;

import android.net.Uri;

import androidx.fragment.app.Fragment;

public interface ChatInteractor {
    void subscribeToFriend(String friendUid,String friendEmail);
    void unsubscribeToFriend(String friendUid);

    void subscribeToMessage();
    void unsubscribeToMessage();

    void sendMessage(String msg);
    void sendImage(Fragment fragment,Uri imageUri);
}
