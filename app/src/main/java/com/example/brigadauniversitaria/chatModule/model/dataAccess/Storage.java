package com.example.brigadauniversitaria.chatModule.model.dataAccess;

import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseStorageAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Storage {
    private static final String PATH_CHATS = "chats";

    private FirebaseStorageAPI mStorageAPI;

    public Storage() {
        mStorageAPI = FirebaseStorageAPI.getInstance();
    }

    /*
     *   Image chat
     * */
    public void uploadImageChat(Fragment fragment, final Uri imageUri, String myEmail,
                                final StorageUploadImageCallback callback){
        if (imageUri.getLastPathSegment() != null){
            StorageReference photoRef = mStorageAPI.getPhotosreferenceByEmail(myEmail).child(PATH_CHATS)
                    .child(imageUri.getLastPathSegment());
            photoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (uri != null){
                                        callback.onSuccess(uri);
                                    } else {
                                        callback.onError(R.string.chat_error_imageUpload);
                                    }
                                }
                            });
                }
            });
        }
    }
}
