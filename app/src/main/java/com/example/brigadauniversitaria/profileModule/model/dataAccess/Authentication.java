package com.example.brigadauniversitaria.profileModule.model.dataAccess;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.EventErrorTypeListener;
import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseAuthenticationAPPI;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.profileModule.events.ProfileEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

public class Authentication {
    private FirebaseAuthenticationAPPI mAuthenticationAPI;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPPI.getInstance();
    }

    public FirebaseAuthenticationAPPI getmAuthenticationAPI() {
        return mAuthenticationAPI;
    }

    public void updateUsernameFirebaseProfile(User myUser,EventErrorTypeListener listener){
        FirebaseUser user = mAuthenticationAPI.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(myUser.getUsername())
                    .build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        listener.onError(ProfileEvent.ERROR_PROFILE,R.string.profile_error_userUpdated);
                    }
                }
            });
        }
    }

    public void updateImageFirebaseProfile(final Uri dowloadUri,final StorageUploadImageCallback callback){
        FirebaseUser user = mAuthenticationAPI.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(dowloadUri)
                    .build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        callback.onSuccess(dowloadUri);
                    }else {
                        callback.onError(R.string.profile_error_imageUpdated);
                    }
                }
            });
        }
    }
}
