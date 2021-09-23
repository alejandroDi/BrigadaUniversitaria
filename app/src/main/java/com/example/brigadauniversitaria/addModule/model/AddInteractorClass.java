package com.example.brigadauniversitaria.addModule.model;

import com.example.brigadauniversitaria.addModule.events.AddEvent;
import com.example.brigadauniversitaria.addModule.model.dataAccess.RealtimeDatabase;
import com.example.brigadauniversitaria.common.model.BasicEventsCallback;
import com.example.brigadauniversitaria.common.model.dataAccess.FirebaseAuthenticationAPPI;

import org.greenrobot.eventbus.EventBus;

public class AddInteractorClass implements AddInteractor {
    private RealtimeDatabase mDatabase;
    private FirebaseAuthenticationAPPI mAuthenticationAPI;

    public AddInteractorClass() {
        this.mDatabase = new RealtimeDatabase();
        this.mAuthenticationAPI = FirebaseAuthenticationAPPI.getInstance();
    }

    @Override
    public void addFriend(String email) {
        mDatabase.addFriend(email,mAuthenticationAPI.getAuthUser(),new BasicEventsCallback() {
            @Override
            public void onSuccess() {
                post(AddEvent.SEND_REQUEST_SUCCESS);
            }

            @Override
            public void onError() {
                post(AddEvent.ERROR_SERVER);
            }
        });

    }
    private void post(int typeEvent){
        AddEvent event = new AddEvent();
        event.setTypeEvent(typeEvent);
        EventBus.getDefault().post(event);
    }
}
