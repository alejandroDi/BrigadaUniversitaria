package com.example.brigadauniversitaria.profileModule.model.dataAccess;

import android.content.Intent;
import android.icu.lang.UProperty;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.brigadauniversitaria.NavigatorActivity;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        this.mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public  void changeUsername(User myUser, UpdateUserListener listener){
        if (mDatabaseAPI.getUserReferenceByUid(myUser.getUid()) != null){
            Map<String,Object> update = new HashMap<>();
            update.put(User.USERNAME, myUser.getUsername());
            mDatabaseAPI.getUserReferenceByUid(myUser.getUid()).updateChildren(update)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            listener.onSuccess();
                            notifyContactsUsername(myUser, listener);
                        }
                    });
        }
    }
    private void  notifyContactsUsername(User myUser, UpdateUserListener listener){
        mDatabaseAPI.getContactsReferences(myUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReferences(friendUid,myUser.getUid());
                            Map<String, Object> update = new HashMap<>();
                            update.put(User.USERNAME,myUser.getUsername());
                            reference.updateChildren(update);
                        }
                        listener.onNotifyContacts();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        listener.onError(R.string.profile_error_userUpdated);
                    }
                });
    }

    private DatabaseReference getContactsReferences(String mainUid,String childUid) {
        return mDatabaseAPI.getUserReferenceByUid(mainUid)
                .child(FirebaseRealtimeDatabaseAPI.PATH_CONTACTS).child(childUid);
    }

    public void updatePhotoUrl(Uri downloadUri, String myUid, StorageUploadImageCallback callback){
        if (mDatabaseAPI.getUserReferenceByUid(myUid) != null){
            Map<String,Object> update = new HashMap<>();
            update.put(User.PHOTO_URL, downloadUri.toString());
            mDatabaseAPI.getUserReferenceByUid(myUid).updateChildren(update)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onSuccess(downloadUri);
                            notifyContactsPhoto(downloadUri.toString(), myUid, callback);
                        }
                    });
        }
    }

    private void notifyContactsPhoto(String photoUrl,String myUid,StorageUploadImageCallback callback) {
        mDatabaseAPI.getContactsReferences(myUid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReferences(friendUid,myUid);
                            Map<String, Object> update = new HashMap<>();
                            update.put(User.PHOTO_URL, photoUrl);
                            reference.updateChildren(update);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        callback.onError(R.string.profile_error_imageUpdated);
                    }
                });
    }

}
