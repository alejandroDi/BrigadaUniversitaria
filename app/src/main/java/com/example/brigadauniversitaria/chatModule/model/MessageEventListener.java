package com.example.brigadauniversitaria.chatModule.model;

import com.example.brigadauniversitaria.common.pojo.Message;

public interface MessageEventListener {
    void onMessageReceived(Message message);
    void onError(int resMsg);

}
