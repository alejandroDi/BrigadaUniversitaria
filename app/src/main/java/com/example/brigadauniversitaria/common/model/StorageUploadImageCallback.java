package com.example.brigadauniversitaria.common.model;

import android.net.Uri;

public interface StorageUploadImageCallback {
    void onSuccess(Uri newUri);
    void onError(int resMsg);
}
