package com.example.brigadauniversitaria.common.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.brigadauniversitaria.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

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

    public static boolean validateEmail(Context context,EditText etEmail) {
        boolean isValid = true;

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()){
            etEmail.setError(context.getString(R.string.common_validate_field_required));
            etEmail.requestFocus();
            isValid = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(context.getString(R.string.common_validate_email_invalid));
            etEmail.requestFocus();
            isValid = false;
        }

        return isValid;
    }
}
