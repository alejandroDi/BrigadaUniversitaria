package com.example.brigadauniversitaria.profileModule.model.dataAccess;

import android.net.Uri;

import com.example.brigadauniversitaria.common.model.StorageUploadImageCallback;

public interface StorageUpdateImageProfileCallback extends StorageUploadImageCallback {
    void onUploadFinished(Uri newuri);
}
