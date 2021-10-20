package com.example.brigadauniversitaria.profileModule;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.profileModule.events.ProfileEvent;

public interface ProfilePresenter {
    void onCreate();
    void onDestroy();

    void setupUser(String username, String email, String photoUrl);
    void checkMode();

    void updateUsername(String username);
    void updateImage(Uri uri);
    User getCurrentUser();
    void result(int requestCode,int resultCode,Intent data);

    void onEventListener(ProfileEvent event);
}
