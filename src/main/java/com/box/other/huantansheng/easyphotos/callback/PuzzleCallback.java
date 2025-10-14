package com.box.other.huantansheng.easyphotos.callback;


import com.box.other.huantansheng.easyphotos.models.album.entity.Photo;

public abstract class PuzzleCallback {

    public abstract void onResult(Photo photo);

    public abstract void onCancel();
}
