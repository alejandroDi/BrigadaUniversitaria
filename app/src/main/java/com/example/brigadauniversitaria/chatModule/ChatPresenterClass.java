package com.example.brigadauniversitaria.chatModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.brigadauniversitaria.chatModule.events.ChatEvent;
import com.example.brigadauniversitaria.chatModule.model.ChatInteractor;
import com.example.brigadauniversitaria.chatModule.model.ChatInteractorClass;
import com.example.brigadauniversitaria.chatModule.view.ChatView;
import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.pojo.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatPresenterClass implements ChatPresenter{
    private ChatView mView;
    private ChatInteractor mInteractor;

    private String mFriendUid, mFriendEmail;

    public ChatPresenterClass(ChatView mView) {
        this.mView = mView;
        this.mInteractor = new ChatInteractorClass();
    }


    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    @Override
    public void onPause() {
        if (mView != null) {
            mInteractor.unsubscribeToFriend(mFriendUid);
            mInteractor.unsubscribeToMessage();
        }
    }

    @Override
    public void onResume() {
        if (mView != null){
            mInteractor.subscribeToFriend(mFriendUid, mFriendEmail);
            mInteractor.subscribeToMessage();
        }
    }

    @Override
    public void setupFriend(String uid,String email) {
        mFriendEmail = email;
        mFriendUid = uid;
    }

    @Override
    public User getCurrentUser() {
        return mInteractor.getCurrentUser();
    }

    @Override
    public void sendMessage(String msg) {
        if (mView != null){
            mInteractor.sendMessage(msg);
        }
    }

    @Override
    public void sendImage(Fragment fragment,Uri imageUri) {
        if (mView != null){
            mView.showProgress();
            mInteractor.sendImage(fragment, imageUri);
        }
    }

    @Override
    public void result(int requestCode,int resultCode,Intent data) {
        if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == FragmentActivity.RESULT_OK){
            mView.openDialogPreview(data);
        }
    }
    @Subscribe
    @Override
    public void onEventListener(ChatEvent event) {
        if (mView != null){
            switch (event.getTypeEvent()){
                case ChatEvent.MESSAGE_ADDED:
                    mView.onMessageReceived(event.getMessage());
                    break;
                case ChatEvent.IMAGE_UPLOAD_SUCCESS:
                    mView.hideProgress();
                    break;
                case ChatEvent.GET_STATUS_FRIEND:
                    mView.onStatusUser(event.isConnected(), event.getLastConnection());
                    break;
                case ChatEvent.ERROR_PROCESS_DATA:
                case ChatEvent.IMAGE_UPLOAD_FAIL:
                case ChatEvent.ERROR_SERVER:
                case ChatEvent.ERROR_VOLLEY:
                    mView.hideProgress();
                    mView.onError(event.getResMsg());
                    break;
            }
        }
    }
}
