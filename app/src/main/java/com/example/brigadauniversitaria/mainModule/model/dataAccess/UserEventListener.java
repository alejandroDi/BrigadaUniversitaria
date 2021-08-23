package com.example.brigadauniversitaria.mainModule.model.dataAccess;

import com.example.brigadauniversitaria.common.pojo.User;

public interface UserEventListener {
    void onUserAdded(User user);
    void onUserUpdate(User user);
    void onUserRemove(User user);

    void onError(int resMsg);
}
