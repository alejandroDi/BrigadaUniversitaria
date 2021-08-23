package com.example.brigadauniversitaria.loginModule;

import android.app.Activity;
import android.content.Intent;

import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.loginModule.events.LoginEvent;
import com.example.brigadauniversitaria.loginModule.model.LoginInteractor;
import com.example.brigadauniversitaria.loginModule.model.LoginInteractorClass;
import com.example.brigadauniversitaria.loginModule.view.Login;
import com.example.brigadauniversitaria.loginModule.view.LoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginPresenterClass implements LoginPresenter {
    private LoginView mView;
    private LoginInteractor mInterator;

    public LoginPresenterClass(LoginView mView) {
        this.mView = mView;
        this.mInterator = new LoginInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        if (setProgress()) {
            mInterator.onResume();
        }

    }

    @Override
    public void onPause() {
        if (setProgress()){
            mInterator.onPause();
        }
    }

    private boolean setProgress() {
        if (mView != null){
            mView.showProgress();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        mView = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void result(int requestCode,int resultCode,Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case Login.RC_SIGN_IN:
                    if (data != null){
                        mView.showLoginSuccessfully(data);
                    }
                    break;
            }
        }else {
            mView.showError(R.string.login_message_error);
        }
    }

    @Override
    public void getStatusAuth() {
        if (setProgress()){
            mInterator.getStatusAuth();
        }

    }

    @Subscribe
    @Override
    public void onEventListener(LoginEvent event) {
        if (mView != null){
            mView.hideProgress();

            switch (event.getTypeEvent()){
                case LoginEvent.STATUS_AUTH_SUCCESS:
                    if (setProgress()){
                        mView.showMenssageStarting();
                        mView.openMainActivity();
                    }
                    break;
                case LoginEvent.STATUS_AUTH_ERROR:
                    mView.openUILogin();
                    break;
                case LoginEvent.ERROR_SERVER:
                    mView.showError(event.getResMsg());
                    break;
            }
        }
    }
}
