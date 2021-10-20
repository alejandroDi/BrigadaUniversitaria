package com.example.brigadauniversitaria.profileModule.model.dataAccess;

public interface UpdateUserListener {
    void onSuccess();
    void onNotifyContacts();
    void onError(int resMsg);
}
