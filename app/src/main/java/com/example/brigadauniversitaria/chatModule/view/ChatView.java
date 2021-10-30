package com.example.brigadauniversitaria.chatModule.view;

import android.content.Intent;

import com.example.brigadauniversitaria.common.pojo.Message;

public interface ChatView {
    void showProgress();
    void hideProgress();

    void onStatusUser(boolean connected,long lastConnection);

    void onError(int resMsg);

    void onMessageReceived(Message msg);

    void openDialogPreview(Intent data);


}
