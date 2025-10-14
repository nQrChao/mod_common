package com.chaoji.other.huantansheng.easyphotos.callback;


import com.chaoji.other.huantansheng.easyphotos.models.album.entity.Photo;

public abstract class PuzzleCallback {

    public abstract void onResult(Photo photo);

    public abstract void onCancel();
}
