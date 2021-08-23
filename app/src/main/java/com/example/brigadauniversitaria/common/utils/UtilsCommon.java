package com.example.brigadauniversitaria.common.utils;

public class UtilsCommon {
    /*
    * codificar un correo electronico
    * */

    public static String getEmailEncoded(String email){
        String preKey = email.replace("_","__");
        return  preKey.replace(".","_");
    }
}
