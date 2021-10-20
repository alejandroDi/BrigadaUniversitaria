package com.example.brigadauniversitaria.profileModule.model;

import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.common.pojo.User;

public interface ProfileInteractor {
    void updateUsername(String username);
    User getCurrentUser();
    void updateImage(Uri uri,String oldPhotoUrl);
}
