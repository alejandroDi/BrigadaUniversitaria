package com.example.brigadauniversitaria.mainModule.model;

import com.example.brigadauniversitaria.common.pojo.User;

public interface MainInteractor {
    void subcribeToUserList();
    void unsubcribeToUserList();

    void singOff();

    User getCurrentUser();
    void removeFriend(String friendUid);

    void acceptRequest(User user);
    void denyRequest(User user);

}
