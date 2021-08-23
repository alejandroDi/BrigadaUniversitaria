package com.example.brigadauniversitaria.mainModule;

import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.mainModule.events.MainEvent;

public interface MainPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void singOff();
    User getCurrentUser();
    void removeFriend(String friendUid);

    void acceptRequest(User user);
    void denyRequest(User user);

    void onEventListener(MainEvent event);



}
