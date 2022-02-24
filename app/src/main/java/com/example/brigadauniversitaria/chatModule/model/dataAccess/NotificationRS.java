package com.example.brigadauniversitaria.chatModule.model.dataAccess;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.brigadauniversitaria.BrigadaApplication;
import com.example.brigadauniversitaria.R;
import com.example.brigadauniversitaria.chatModule.events.ChatEvent;
import com.example.brigadauniversitaria.common.Constants;
import com.example.brigadauniversitaria.common.model.EventErrorTypeListener;
import com.example.brigadauniversitaria.common.pojo.User;
import com.example.brigadauniversitaria.common.utils.UtilsCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationRS {
    ////se crean las variables al servidor de notificaciones y el metodo de notificacion
    private static final String BRIGADA_RS="https://brigadanotification.000webhostapp.com/brigada/dataAccess/TextingRS.php";
    private static final String SEND_NOTIFICATION="sendNotification";
    private static final String SEND_NOTIFICATION_INCIDENT="sendNotificationIncident";
    //metodo para notificaciones de chat
    public void sendNotification(String title,String message,String email,String uid,
                                 String myEmail,Uri photoUrl,EventErrorTypeListener listener){
        JSONObject parms = new JSONObject();
        try {
            parms.put(Constants.METHOD, SEND_NOTIFICATION);
            parms.put(Constants.TITLE, title);
            parms.put(Constants.MESSAGE, message);
            parms.put(Constants.TOPIC,UtilsCommon.getEmailToTopic(email));
            parms.put(User.UID, uid);
            parms.put(User.EMAIL, myEmail);
            parms.put(User.PHOTO_URL, photoUrl);
            parms.put(User.USERNAME, title);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(ChatEvent.ERROR_PROCESS_DATA, R.string.common_error_process_data);
        }
        //envia en json de nuetra peticion al servidor por parte de volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,BRIGADA_RS,parms,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt(Constants.SUCCESS);
                    switch (success){
                        case ChatEvent.SEND_NOTIFICATION_SUCCESS:

                            break;
                        case ChatEvent.ERROR_METHOD_NOT_EXIST:
                            listener.onError(ChatEvent.ERROR_METHOD_NOT_EXIST, R.string.chat_error_method_not_exist);
                            break;
                        default:
                            listener.onError(ChatEvent.ERROR_SERVER, R.string.common_error_server);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(ChatEvent.ERROR_PROCESS_DATA, R.string.common_error_process_data);
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error",error.getLocalizedMessage());
                listener.onError(ChatEvent.ERROR_VOLLEY,R.string.common_error_volley);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("Content_type","application/json; charset=utf-8");
                return params;
            }
        };
        BrigadaApplication.getInstance().addToQueue(jsonObjectRequest);
    }
    ///metodo notificaciones de incidentes
}
