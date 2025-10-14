package com.chaoji.im.audio;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager2 {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager2 mInstance;

    private boolean isPrepare;

    private AudioManager2(String dir) {
        mDir = dir;
    }

    public static AudioManager2 getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager2.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager2(dir);
                }
            }
        }
        return mInstance;
    }


    public interface AudioStateListener {
        void wellPrepared();
    }

    public AudioStateListener mAudioStateListener;


    public void setOnAudioStateListener(AudioStateListener listener) {
        mAudioStateListener = listener;
    }

    // 去准备
    public void prepareAudio() {
        try {
            isPrepare = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            } else {
                if (!dir.isDirectory()) {
                    dir.delete();
                    dir.mkdirs();
                }
            }
            String fileName = generateFileName();
            File file = new File(dir, fileName);
            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepare = true;
            if (mAudioStateListener != null) {
                mAudioStateListener.wellPrepared();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString() + ".acc";
    }

    public int getVoiceLevel(int maxlevel) {
        if (isPrepare) {
            try {
                return maxlevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public void release() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }
}
