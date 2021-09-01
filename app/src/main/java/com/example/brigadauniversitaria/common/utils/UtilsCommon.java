package com.example.brigadauniversitaria.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class UtilsCommon {
    /*
    * codificar un correo electronico
    * */

    public static String getEmailEncoded(String email){
        String preKey = email.replace("_","__");
        return  preKey.replace(".","_");
    }

    /*
    * charger img
    * */

    public static void loadImagen(Context context,String url,ImageView target){
        RequestOptions option = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(context)
                .load(url)
                .apply(option)
                .into(target);

    }
}
