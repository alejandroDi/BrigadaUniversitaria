package com.example.brigadauniversitaria;

import com.example.brigadauniversitaria.common.pojo.Contacto;
import com.example.brigadauniversitaria.common.pojo.User;

public interface iComunicaFragment {
    public void chatOpen(User user);
    public void contactOpen(Contacto contacto);
}
