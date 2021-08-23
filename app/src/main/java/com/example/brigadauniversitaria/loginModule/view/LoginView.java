package com.example.brigadauniversitaria.loginModule.view;

import android.content.Intent;

public interface LoginView {
    void showProgress();
    void hideProgress();

    void openMainActivity();
    void openUILogin();

    void showLoginSuccessfully(Intent data);
    void showMenssageStarting();
    void showError(int resMsg);
}
