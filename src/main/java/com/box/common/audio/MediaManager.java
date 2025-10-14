package com.box.common.audio;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private String currentFilePath;
    private AudioManager2.AudioStateListener onAudioStateListener;
    static Context context;

    public MediaManager(Context context) {
        this.context = context;
    }


    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
//            File file = new File(filePath);
//            FileInputStream fis = new FileInputStream(file);
//            mMediaPlayer.setDataSource(fis.getFD());
//            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    public static boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }


    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
