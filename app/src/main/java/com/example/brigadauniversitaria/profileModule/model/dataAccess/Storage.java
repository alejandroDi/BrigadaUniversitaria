package com.example.brigadauniversitaria.profileModule.model.dataAccess;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseStorageAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class Storage {
    private FirebaseStorageAPI mStorageAPI;
    private static final String PATH_PROFILE = "profile";

    public Storage() {
        mStorageAPI = FirebaseStorageAPI.getInstance();
    }

    public void uploadImageProfile (Uri imageUri,String email,
                                    final StorageUploadImageCallback callback){
        if (imageUri.getLastPathSegment() != null){
            final StorageReference photoRef = mStorageAPI.getPhotosreferenceByEmail(email)
                    .child(PATH_PROFILE).child(imageUri.getLastPathSegment());
            photoRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (uri != null){
                                        callback.onSuccess(uri);
                                    }else {
                                        callback.onError(R.string.profile_error_imageUpdated);
                                    }
                                }
                            });
                        }
                    });
        }else {
            callback.onError(R.string.profile_error_invalid_image);
        }
    }

    public void deleteOldImage(String oldPhotoUrl, String dowloadUrl){
        if (oldPhotoUrl != null && !oldPhotoUrl.isEmpty()){
            StorageReference storageReference = mStorageAPI.getmFirebaseStorage()
                    .getReferenceFromUrl(dowloadUrl);
            StorageReference oldStorageReference = null;
            try {
                oldStorageReference = mStorageAPI.getmFirebaseStorage()
                        .getReferenceFromUrl(oldPhotoUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (oldStorageReference != null
                    && !oldStorageReference.getPath().equals(storageReference.getPath())){
                oldStorageReference.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });
            }


        }
    }
}
