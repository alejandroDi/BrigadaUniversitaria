package com.example.brigadauniversitaria.chatModule.model;

public interface LastConectionEventListener {
    void onSuccess(boolean online,long lastConnection, String uidConnectedFriend);
}
