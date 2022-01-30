package com.example.brigadauniversitaria.chatModule.model.dataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.chatModule.model.LastConectionEventListener;
import com.example.brigadauniversitaria.chatModule.model.MessageEventListener;
import com.example.brigadauniversitaria.chatModule.model.SendMessageListener;
import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.brigadauniversitaria.common.pojo.Message;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {
    private static final String PATH_CHATS = "chats";
    private static final String PATH_MESSAGES = "messages";

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    private ChildEventListener mMessagesEventListener;
    private ValueEventListener mFriendProfileListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public FirebaseRealtimeDatabaseAPI getmDatabaseAPI() {
        return mDatabaseAPI;
    }

    public void subscribeToMessages(String myEmail, String friendEmail, final MessageEventListener listener){
        if (mMessagesEventListener == null){
            mMessagesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    listener.onMessageReceived(getMessage(snapshot));
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot,@Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    switch (error.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.chat_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.common_error_server);
                            break;
                }
            }
            };
        }

        getChatsMessagesReference(myEmail, friendEmail).addChildEventListener(mMessagesEventListener);
    }

    private DatabaseReference getChatsMessagesReference(String myEmail,String friendEmail) {
        return getChatsReference(myEmail, friendEmail).child(PATH_MESSAGES);
    }

    private DatabaseReference getChatsReference(String myEmail, String friendEmail) {
        String myEmailEncoded = UtilsCommon.getEmailEncoded(myEmail);
        String friendEmailEncoded = UtilsCommon.getEmailEncoded(friendEmail);

        String keyChat = myEmailEncoded + FirebaseRealtimeDatabaseAPI.SEPARATOR + friendEmailEncoded;
        if (myEmailEncoded.compareTo(friendEmailEncoded) > 0){
            keyChat = friendEmailEncoded + FirebaseRealtimeDatabaseAPI.SEPARATOR + myEmailEncoded;
        }
        return mDatabaseAPI.getRootReferences().child(PATH_CHATS).child(keyChat);
    }

    private Message getMessage(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);

        if (message != null){
            message.setUid(dataSnapshot.getKey());
        }
        return message;
    }

    public void unsubscribeToMessages(String myEmail, String friendEmail){
        if (mMessagesEventListener != null){
            getChatsMessagesReference(myEmail, friendEmail).removeEventListener(mMessagesEventListener);
        }
    }

    public void subscribeToFriend(String uid, final LastConectionEventListener listener){
        if (mFriendProfileListener == null){
            mFriendProfileListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long lastConnectionFriend = 0;
                    String uidConnectedFriend = "";
                    try {
                        Long value = dataSnapshot.getValue(Long.class);
                        if (value != null){
                            lastConnectionFriend = value;
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        String lastConnectionWith = dataSnapshot.getValue(String.class);
                        if (lastConnectionWith != null && !lastConnectionWith.isEmpty()){
                            String[] values = lastConnectionWith.split(FirebaseRealtimeDatabaseAPI.SEPARATOR);
                            if (values.length > 0){
                                lastConnectionFriend = Long.valueOf(values[0]);
                                if (values.length > 1){
                                    uidConnectedFriend = values[1];
                                }
                            }
                        }
                    }
                    listener.onSuccess(lastConnectionFriend == Constants.ONLINE_VALUE,
                                      lastConnectionFriend, uidConnectedFriend);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
        }

        mDatabaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).keepSynced(true);
        mDatabaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH)
                .addValueEventListener(mFriendProfileListener);
    }

    public void unsubscribeToFriend(String uid){
        if (mFriendProfileListener != null){
            mDatabaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH)
                    .removeEventListener(mFriendProfileListener);
        }
    }

    /*
     *   read/unread messages
     * */
    public void setMessagesRead(String myUid, String friendUid){
        final DatabaseReference userReference = getOneContactReference(myUid, friendUid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(User.MESSAGES_UNREAD, 0);
                    userReference.updateChildren(updates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private DatabaseReference getOneContactReference(String uidMain, String uidChild) {
        return mDatabaseAPI.getUserReferenceByUid(uidMain).child(FirebaseRealtimeDatabaseAPI.PATH_CONTACTS)
                .child(uidChild);
    }

    public void sumUnreadMessages(String myUid, String friendUid){
        final DatabaseReference userReference = getOneContactReference(friendUid, myUid);
        userReference.keepSynced(true);

        userReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null){
                    return Transaction.success(mutableData);
                }

                user.setMessagesUnread(user.getMessagesUnread() + 1);
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    /*
     *   Send Message
     * */
    public void sendMessage(String msg, String photoUrl, String friendEmail, User myUser,
                            final SendMessageListener listener){
        Message message = new Message();
        message.setSender(myUser.getEmail());
        message.setMsg(msg);
        message.setPhotoUrl(photoUrl);

        DatabaseReference chatReference = getChatsMessagesReference(myUser.getEmail(), friendEmail);
        chatReference.push().setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    listener.onSuccess();
                }
            }
        });
    }
}
