package com.example.brigadauniversitaria.addModule.model;

import com.example.brigadauniversitaria.addModule.events.AddEvent;
import com.example.brigadauniversitaria.addModule.model.dataAccess.RealtimeDatabase;
import com.example.brigadauniversitaria.common.model.BasicEventsCallback;
import com.example.brigadauniversitaria.common.model.EventsCallback;
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
            mDatabase.checkUserExist(email,new EventsCallback() {
                @Override
                public void onSuccess() {
                    mDatabase.checkContactExist(email,mAuthenticationAPI.getmFirebaseAuth().getCurrentUser().getUid(),
                                                new EventsCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        mDatabase.checkRequestNotExist(email, mAuthenticationAPI.getmFirebaseAuth().getCurrentUser().getUid(),
                                                                                       new EventsCallback() {
                                                                                           @Override
                                                                                           public void onSuccess() {
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

                                                                                           @Override
                                                                                           public void onError(int typeEvent,int resMsg) {
                                                                                               post(typeEvent, resMsg);
                                                                                           }
                                                                                       });
                                                    }

                                                    @Override
                                                    public void onError(int typeEvent,int resMsg) {
                                                        post(typeEvent,resMsg);
                                                    }
                                                });
                }

                @Override
                public void onError(int typeEvent,int resMsg) {
                    post(typeEvent, resMsg);
                }
            });


    }
    private void post(int typeEvent){
        post(typeEvent,0);
    }
    private void post(int typeEvent,int resMsg){
        AddEvent event = new AddEvent();
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        EventBus.getDefault().post(event);
    }
}
