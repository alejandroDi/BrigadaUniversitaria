package com.example.brigadauniversitaria.chatModule.events;

import com.example.brigadauniversitaria.common.pojo.Message;

public class ChatEvent {
    public static int MESSAGE_ADDED = 0;
    public static int MESSAGE_UPLOAD_SUCCESS = 1;

    public static int GET_STATUS_FRIEND = 2;

    public static int ERROR_SERVER = 100;
    public static int IMAGE_UPLOAD_FAIL = 101;
    public static int ERROR_VOLLEY = 102;
    public static int ERROR_PROCESS_DATA = 103;

    private int typeEvent;
    private int resMsg;
    private Message message;
    private boolean connected;
    private long lastConnection;

    public ChatEvent() {

    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getResMsg() {
        return resMsg;
    }

    public void setResMsg(int resMsg) {
        this.resMsg = resMsg;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }
}
