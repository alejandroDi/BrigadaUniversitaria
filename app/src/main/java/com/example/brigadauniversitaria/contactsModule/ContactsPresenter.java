package com.example.brigadauniversitaria.contactsModule;


public interface ContactsPresenter {
    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void showContacts();
    void callContacts();
}
