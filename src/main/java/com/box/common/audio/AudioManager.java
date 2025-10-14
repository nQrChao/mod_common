package com.box.common.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.box.common.utils.UtilHttpProxyCache;
import com.box.other.danikula.videocache.HttpProxyCacheServer;

public class AudioManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static AudioManager INSTANCE;
    public MediaPlayer mMediaPlayer;
    private Context mContext;
    private boolean isPreparing = false;

    private AudioBean preBean;

    private Handler mHandler;

    private AudioBean curBean;

    private AudioManager(Context context) {
        this.mContext = context;
        initMediaPlayer();
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                preparing();
            }
        };
    }

    public static AudioManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AudioManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public void playAssertAudio(String name) {
        if (mMediaPlayer != null && mContext != null) {
            try {
                mMediaPlayer.reset();
                AssetFileDescriptor fd = mContext.getAssets().openFd(name);
                mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(this);
                //mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnErrorListener(this);
            } catch (Exception e) {
                System.out.println("===============> mediaplayer play Exception:" + e.toString());
                e.printStackTrace();
            }
        }
    }

    protected void stopmp3() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            Toast.makeText(mContext, "停止播放", Toast.LENGTH_SHORT).show();
        }
    }


    public void playAudio(final String url, MediaPlayer.OnCompletionListener listener) {
        if (isPreparing) {
            setCurdata(url, AudioBean.START, true);
            return;
        }
        if (mMediaPlayer != null && mContext != null) {
            try {
                setPredata(url, AudioBean.START, true);
                mMediaPlayer.reset();
                HttpProxyCacheServer proxy = UtilHttpProxyCache.getAudioProxy(mContext);
                mMediaPlayer.setDataSource(proxy.getProxyUrl(url));
                isPreparing = true;
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnCompletionListener(listener);
                mMediaPlayer.setOnErrorListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean playing() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void play() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void stop(String url) {
        if (isPreparing) {
            setCurdata(url, AudioBean.STOP, true);
        } else {
            System.out.println("===============> pause2 mediaplayer isPlaying: " + mMediaPlayer.isPlaying());
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("===============> onPrepared: sleep");
        mHandler.sendEmptyMessageDelayed(1, 0); // 模拟mediaplayer准备数据中间时长，延时
    }

    private void preparing() {
        System.out.println("===========================================================> preparing: ");
        isPreparing = false;
        if (preBean != null && curBean != null && curBean.isNeed()) { // 需要处理
            System.out.println("===============> mediaplayer isNedd true");
            // 判断url是否一样，
            if (preBean.getUrl().equals(curBean.getUrl())) { // 比如点击暂停，又点击开始
                System.out.println("===============> url 相等");
                if (curBean.getStatus() == AudioBean.START && curBean.isNeed()) { // 如果同一个url并且需要播放，就直接start，比如prepare的时候，点击了暂停，在点击开始，同一个音频
                    System.out.println("===============> 开始播放");
                    start();
                } else { // 同一个url，如果是停止，就直接不处理，后面把isNeed设置成false
                    System.out.println("===============> 不需要播放");
                }
            } else { // 如果不同url，说明点击了另外的音频，
                System.out.println("===============> url 不相等");
                if (curBean.getStatus() == AudioBean.START && curBean.isNeed()) { // 重新play新的url
                    System.out.println("===============> 重新play新的url");
                    //playAudio(curBean.getUrl());
                } else { // 如果停止的话无需处理
                    System.out.println("===============> 不需要播放");
                }
            }
            setCurdata("", AudioBean.STOP, false);
        } else { // 如果最后一次操作不需要处理，则走正常流程
            System.out.println("===============> mediaplayer isNedd false");
            start();
        }
    }


    /**
     * 播放错误
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("===============> mediaplayer onError");
        return false;
    }

    /**
     * init
     */
    private void initMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
    }

    /**
     * 开始播放
     */
    private void start() {
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    /**
     * 保存prepare前的操作
     */
    private void setPredata(String url, int status, boolean isNeed) {
        if (preBean == null) {
            preBean = new AudioBean();
        }
        preBean.setUrl(url);
        preBean.setStatus(status);
        preBean.setNeed(isNeed);
    }

    /**
     * 保存prepare中的操作
     */
    private void setCurdata(String url, int status, boolean isNeed) {
        if (curBean == null) {
            curBean = new AudioBean();
        }
        curBean.setUrl(url);
        curBean.setStatus(status);
        curBean.setNeed(isNeed);
    }


}
