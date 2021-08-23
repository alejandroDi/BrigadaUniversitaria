package com.example.brigadauniversitaria.common.model.dataAccess;

import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.pojo.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class FirebaseRealtimeDatabaseAPI {
    public static final String SEPARATOR = "___&___";
    public static final String PATH_USERS = "users";
    public static final String PATH_CONTACTS = "contacts";
    public static final String PATH_REQUESTS = "requests";

    private DatabaseReference mDatabaseReference;

    private static class SingletonHolder{

        private static final FirebaseRealtimeDatabaseAPI INSTANCE = new FirebaseRealtimeDatabaseAPI();

    }
    public static FirebaseRealtimeDatabaseAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    private FirebaseRealtimeDatabaseAPI(){
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }


    /*
    * REFERENCES
    * */

    public DatabaseReference getRootReferences(){
        return mDatabaseReference.getRoot();
    }
    public DatabaseReference getUserReferenceByUid(String uid){
        return getRootReferences().child(PATH_USERS).child(uid);
    }

    public DatabaseReference getContactsReferences(String uid) {
        return getUserReferenceByUid(uid).child(PATH_CONTACTS);
    }

    public DatabaseReference getRequestReference(String email) {
        return getRootReferences().child(PATH_REQUESTS);
    }

    public void updateMyLastConnection(boolean online,String uid) {
        updateMyLastConnection(online, "", uid);
    }

    public void updateMyLastConnection(boolean online, String uidFriend, String uid){
        String lastConnectionWith = Constants.ONLINE_VALUE + SEPARATOR + uidFriend;
        Map<String, Object> values = new HashMap<>();
        values.put(User.LAST_CONNECTION_WITH, online? lastConnectionWith : ServerValue.TIMESTAMP);
        getUserReferenceByUid(uid).updateChildren(values);

        if (online){
            getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).onDisconnect()
                    .setValue(ServerValue.TIMESTAMP);
        }else{
            getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).onDisconnect().cancel();        }

    }
}
