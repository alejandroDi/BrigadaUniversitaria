package com.example.brigadauniversitaria.mainModule.view.adapters;

import com.example.brigadauniversitaria.common.pojo.User;

public interface OnItemClickListener {
    void onItemClick(User user);
    void onItemLongClick(User user);

    void onAcceptRequest(User user);
    void onDenyRequest(User user);
}
