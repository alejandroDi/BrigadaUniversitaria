package com.example.brigadauniversitaria.addModule.view;

public interface AddView {
    void enableUIElements();
    void disableUIElements();
    void showProgress();
    void hideProgress();

    void friendAdded();
    void friendNotAdded();
    void showMessageExist(int resMsg);
}
