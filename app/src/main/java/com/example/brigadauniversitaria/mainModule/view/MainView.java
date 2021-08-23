package com.example.brigadauniversitaria.mainModule.view;

import com.example.brigadauniversitaria.common.pojo.User;

public interface MainView {
    void friendAdded(User user);
    void friendUpdate(User user);
    void friendRemove(User user);

    void requestAdded(User user);
    void requestUpdate(User user);
    void requestRemove(User user);

    void showRequestAccepted(String username);
    void showRequestDenied();
    void showFriendRemove();
    void showError(int resMsg);

}
