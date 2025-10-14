package com.chaoji.other.huantansheng.easyphotos.utils.bitmap;

import java.io.File;
import java.io.IOException;

public interface SaveBitmapCallBack {
    void onSuccess(File file);

    void onIOFailed(IOException exception);

    void onCreateDirFailed();
}
