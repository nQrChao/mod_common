package com.box.other.kongzue.baseokhttp.listener;

public interface UploadProgressListener {

    void onUpload(float percentage, long current, long total, boolean done);
}
