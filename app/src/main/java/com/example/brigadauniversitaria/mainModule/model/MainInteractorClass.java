package com.example.brigadauniversitaria.mainModule.model;

import android.provider.SyncStateContract;

import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.model.BasicEventsCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseCloudMessagingAPI;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.mainModule.events.MainEvent;
import com.example.brigadauniversitaria.mainModule.model.dataAccess.Authentication;
import com.example.brigadauniversitaria.mainModule.model.dataAccess.RealtimeDatabase;
import com.example.brigadauniversitaria.mainModule.model.dataAccess.UserEventListener;

import org.greenrobot.eventbus.EventBus;

public class MainInteractorClass implements MainInteractor {
    private RealtimeDatabase mDatabase;
    private Authentication mAuthentication;
    //notify
    private FirebaseCloudMessagingAPI mCloudMessaginAPI;

    private User mMyUser = null;

    public MainInteractorClass() {
        mDatabase = new RealtimeDatabase();
        mAuthentication = new Authentication();
        //NOTIFY
        mCloudMessaginAPI = FirebaseCloudMessagingAPI.getInstance();
    }

    @Override
    public void subcribeToUserList() {
        mDatabase.subscribeToUserList(getCurrentUser().getUid(),new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.USER_ADDED, user);
            }

            @Override
            public void onUserUpdate(User user) {
                post(MainEvent.USER_UPDATE, user);
            }

            @Override
            public void onUserRemove(User user) {
                post(MainEvent.USER_REMOVE, user);
            }

            @Override
            public void onError(int resMsg) {
                postError(resMsg);
            }
        });

        mDatabase.subscribeToRequest(getCurrentUser().getEmail(),new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.REQUEST_ADDED, user);
            }

            @Override
            public void onUserUpdate(User user) {
                post(MainEvent.REQUEST_UPDATE, user);
            }

            @Override
            public void onUserRemove(User user) {
                post(MainEvent.REQUEST_REMOVE, user);
            }

            @Override
            public void onError(int resMsg) {
                post(MainEvent.ERROR_SERVER);
            }
        });

        changeConnectionStatus(Constants.ONLINE);
    }
    private void changeConnectionStatus(boolean online) {
        mDatabase.getmDatabaseAPI().updateMyLastConnection(online, getCurrentUser().getUid());
    }

    @Override
    public void unsubcribeToUserList() {
        mDatabase.unsubcribeToUsers(getCurrentUser().getUid());
        mDatabase.unsubcribeToRequest(getCurrentUser().getEmail());

        changeConnectionStatus(Constants.OFFLINE);
    }

    @Override
    public void singOff() {
        mCloudMessaginAPI.unsubscribeToMyTopic(getCurrentUser().getEmail());
        mAuthentication.singOff();
    }

    @Override
    public User getCurrentUser() {
        return mMyUser == null? mAuthentication.getmAuthenticationAPI().getAuthUser() : mMyUser;
    }

    @Override
    public void removeFriend(String friendUid) {
        mDatabase.removeUser(friendUid,getCurrentUser().getUid(),new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.USER_REMOVE);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void acceptRequest(User user) {
        mDatabase.acceptRequest(user,getCurrentUser(),new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_ACCEPTED, user);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void denyRequest(User user) {
        mDatabase.denyRequest(user,getCurrentUser().getEmail(),new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_DENIED);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);

            }
        });

    }

    private void post(int typeEvent,User user){
        post(typeEvent,user,0);

    }

    private void post(int typeEvent,User user,int resMsg) {
        MainEvent event = new MainEvent();
        event.setTypeEvent(typeEvent);
        event.setUser(user);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);

    }

    private void postError(int resMsg) {
        post(MainEvent.ERROR_SERVER, null, resMsg);
    }

    private void post(int typeEvent) {
        post(typeEvent, null, 0);
    }


}
