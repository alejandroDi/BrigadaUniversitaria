package com.example.brigadauniversitaria.chatModule.model.dataAccess;

import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseStorageAPI;
import com.google.firebase.storage.StorageReference;

public class Storage {
    private static final String PATH_CHAT = "chats";

    private FirebaseStorageAPI mStorageAPI;

    public Storage() {
        mStorageAPI = FirebaseStorageAPI.getInstance();
    }

    /*
    * Image Chat
    * */

    public void uploadImagechat(Fragment fragment,final Uri imageUri,String myEmail,
                                final StorageUploadImageCallback callback){
        if (imageUri.getLastPathSegment() != null){
            StorageReference photoRef = mStorageAPI.getPhotosreferenceByEmail(myEmail).child(PATH_CHAT)
                    .child(imageUri.getLastPathSegment());
        }

    }
}
