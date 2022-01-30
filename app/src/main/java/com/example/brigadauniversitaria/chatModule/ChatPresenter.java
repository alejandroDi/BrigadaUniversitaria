package com.example.brigadauniversitaria.chatModule;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.chatModule.events.ChatEvent;
import com.example.brigadauniversitaria.common.pojo.User;

public interface ChatPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void setupFriend(String uid,String email);
    User getCurrentUser();

    void sendMessage(String msg);
    void sendImage(Fragment fragment,Uri imageUri);

    void result(int requestCode,int resultCode,Intent data);

    void onEventListener(ChatEvent event);

}
