package com.example.brigadauniversitaria.addModule;

import com.example.brigadauniversitaria.addModule.events.AddEvent;

public interface AddPresenter {
    void onShow();
    void onDestroy();

    void addFriend(String email);
    void onEventListener(AddEvent event);

}
