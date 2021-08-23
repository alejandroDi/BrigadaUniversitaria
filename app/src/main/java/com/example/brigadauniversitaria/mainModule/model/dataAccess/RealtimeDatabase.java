package com.example.brigadauniversitaria.mainModule.model.dataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.common.model.BasicEventsCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ChildEventListener mUserEventListener;
    private ChildEventListener mRequestEventListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();

    }

    /*
    * references
    * */

    public FirebaseRealtimeDatabaseAPI getmDatabaseAPI() {
        return mDatabaseAPI;
    }

    private DatabaseReference getUsersReference(){
        return mDatabaseAPI.getRootReferences().child(FirebaseRealtimeDatabaseAPI.PATH_USERS);
    }

    /*
    * public methods
    * */

    public void subscribeToUserList(String myUid, final UserEventListener listener){
        if (mUserEventListener == null){
            mUserEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    listener.onUserAdded(getUser(snapshot));
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    listener.onUserUpdate(getUser(snapshot));
                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                    listener.onUserRemove(getUser(snapshot));
                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    switch (error.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.main_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.common_error_server);
                            break;
                    }
                }
            };
        }
        mDatabaseAPI.getContactsReferences(myUid).addChildEventListener(mUserEventListener);
    }
    private User getUser(DataSnapshot snapshot){
        User user = snapshot.getValue(User.class);
        if (user != null){
            user.setUid(snapshot.getKey());
        }
        return user;
    }


    public void subscribeToRequest(String email, final UserEventListener listener){
        if (mRequestEventListener == null){
            mRequestEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    listener.onUserAdded(getUser(snapshot));
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    listener.onUserUpdate(getUser(snapshot));
                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                    listener.onUserRemove(getUser(snapshot));
                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    listener.onError(R.string.common_error_server);
                }
            };
        }
        final String emailEncode = UtilsCommon.getEmailEncoded(email);
        mDatabaseAPI.getRequestReference(emailEncode).addChildEventListener(mRequestEventListener);
    }

    public void unsubcribeToUsers(String uid){
        if (mUserEventListener != null){
            mDatabaseAPI.getContactsReferences(uid).removeEventListener(mUserEventListener);
        }
    }

    public void unsubcribeToRequest(String email){
        if (mRequestEventListener != null){
            final String emailEncoded = UtilsCommon.getEmailEncoded(email);
            mDatabaseAPI.getRequestReference(emailEncoded).removeEventListener(mRequestEventListener);
        }
    }

    public void removeUser(String friendUid,String myUid, final BasicEventsCallback callback){
        Map<String, Object> removeUserMap = new HashMap<>();
        removeUserMap.put(myUid+"/"+FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+friendUid,null);
        removeUserMap.put(friendUid+"/"+FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+myUid,null);
        getUsersReference().updateChildren(removeUserMap,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error,@NonNull @NotNull DatabaseReference ref) {
                if (error == null){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        });
    }

    public void acceptRequest(User user, User myUser, final BasicEventsCallback callback){
        Map<String, String> userRequestMap = new HashMap<>();
        userRequestMap.put(User.USERNAME, user.getUsername());
        userRequestMap.put(User.EMAIL, user.getEmail());
        userRequestMap.put(User.PHOTO_URL, user.getPhotoUrl());

        Map<String, String> myuserMap = new HashMap<>();
        myuserMap.put(User.USERNAME, myUser.getUsername());
        myuserMap.put(User.EMAIL, myUser.getEmail());
        myuserMap.put(User.PHOTO_URL, myUser.getPhotoUrl());

        final String emailEncode = UtilsCommon.getEmailEncoded(myUser.getEmail());

        Map<String, Object> acceptRequest = new HashMap<>();
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS+"/"+user.getUid()+"/"+
                                  FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+myUser.getUid(), myuserMap);

        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS+"/"+myUser.getUid()+"/"+
                                  FirebaseRealtimeDatabaseAPI.PATH_CONTACTS+"/"+user.getUid(), userRequestMap);

        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_REQUESTS+"/"+emailEncode+"/"+
                                  user.getUid(),null);

        mDatabaseAPI.getRootReferences().updateChildren(acceptRequest,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error,@NonNull @NotNull DatabaseReference ref) {
              if (error == null){
                  callback.onSuccess();
              }else {
                  callback.onError();
              }
            }
        });
    }

    public void denyRequest(User user, String myEmail, final  BasicEventsCallback callback){
        final String emailEncoded = UtilsCommon.getEmailEncoded(myEmail);
        mDatabaseAPI.getRequestReference(emailEncoded).child(user.getUid())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error,@NonNull @NotNull DatabaseReference ref) {
                        if (error == null){
                            callback.onSuccess();
                        }else {
                            callback.onError();
                        }
                    }
                });
    }

}
