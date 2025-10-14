package com.chaoji.other.kongzue.baseokhttp.listener;

import java.io.File;

public interface OnDownloadListener {
    
    void onDownloadSuccess(File file);
    
    void onDownloading(int progress);
    
    void onDownloadFailed(Exception e);
}
