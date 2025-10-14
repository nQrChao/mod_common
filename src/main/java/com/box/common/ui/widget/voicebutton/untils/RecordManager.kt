package com.box.common.ui.widget.voicebutton.untils

import android.media.MediaRecorder
import com.box.other.blankj.utilcode.util.Logs
import com.box.common.ui.widget.voicebutton.interfaces.MediaRecorderStateListener
import java.io.File
import java.util.*
import kotlin.math.log10

/**
 * @Description: 音频录制管理类
 */
class RecordManager(private val recordFileDir: String) {  //音频文件存储目录

    var recordAbsoluteFileDir: String? = null  //音频文件绝对存储路径

    private var mediaRecorder: MediaRecorder? = null  //通过MediaRecorder实现录音功能

    var maxRecordLength = 1000 * 60  //最大录音时长,默认1000*60（一分钟）

    private var isRecording = false  //录音控件初始化状态标志

    private val volumeBase: Long = 600  //音量分级标准

    private var mediaRecorderStateListener: MediaRecorderStateListener? = null

    fun setMediaRecorderStateListener(mediaRecorderStateListener: MediaRecorderStateListener) {
        this.mediaRecorderStateListener = mediaRecorderStateListener
    }

    /**
     * 创建录音文件目录
     *
     */
    private fun createRecordFileDir() {
        try {
            val dir = File(recordFileDir)
            if (!dir.exists()) {
                dir.mkdir()  //生成目录
            }
            //生成随机文件名
            val file = File(dir, UUID.randomUUID().toString() + ".aac")
            recordAbsoluteFileDir = file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化MediaRecorder
     *
     */
    private fun initRecorder() {
        mediaRecorder = MediaRecorder().apply {
            reset()
            setOutputFile(recordAbsoluteFileDir)  //设置输出文件
            setMaxDuration(maxRecordLength)  //设置录制最长时间
            setAudioSource(MediaRecorder.AudioSource.MIC)  //设置MediaRecorder的音频源为麦克风
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)  //设置音频输出格式
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  //设置音频编码器为amr
            prepare()  //准备结束,可以开始录音了
            start()
        }
//        mediaRecorder = MediaRecorder().apply {
//            reset()
//            setOutputFile(recordAbsoluteFileDir)  //设置输出文件
//            setMaxDuration(maxRecordLength)  //设置录制最长时间
//            setAudioSource(MediaRecorder.AudioSource.MIC)  //设置MediaRecorder的音频源为麦克风
//            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)  //设置音频输出格式
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  //设置音频编码器为amr
//            prepare()  //准备结束,可以开始录音了
//            start()
//        }
    }

    /**
     * 设置录制时不同状态的监听
     *
     */
    private fun setRecorderStateListener() {
        //设置信息监听器。可监听录制结束事件，包括达到录制时长或者达到录制大小
        mediaRecorder?.setOnInfoListener { _, what, _ ->
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {   //到达最大录音时间
                mediaRecorderStateListener?.onReachMaxRecordTime(recordAbsoluteFileDir)  //回调
            }
        }
        mediaRecorder?.setOnErrorListener { _, what, extra ->   //录音发生错误
            mediaRecorderStateListener?.onError(what, extra)
        }
        mediaRecorderStateListener?.wellPrepared()
    }

    /**
     * 准备录音控件
     */
    fun prepareAudio() {
        createRecordFileDir()
        initRecorder()
        setRecorderStateListener()
        isRecording = true
    }

    /**
     * 正常录音结束释放资源
     *
     */
    fun release() {
        if (mediaRecorder != null) {
            try {
                //下面三个参数必须加，不加的话会奔溃，mediaRecorder.stop();
                //报错为：RuntimeException:stop failed
                mediaRecorder?.setOnErrorListener(null)
                mediaRecorder?.setOnInfoListener(null)
                mediaRecorder?.setPreviewDisplay(null)
                mediaRecorder?.stop()
            } catch (e: IllegalStateException) {
                Logs.e("Exception", e.toString())
            } catch (e: RuntimeException) {
                Logs.e("Exception", e.toString())
            } catch (e: Exception) {
                Logs.e("Exception",e.toString())
            } finally {
                mediaRecorder?.release()
                mediaRecorder = null
            }
        }
        isRecording = false
    }

    /**
     * 中途取消录音,删除音频文件
     *
     */
    fun cancel() {
        release()
        recordAbsoluteFileDir?.let {
            val file = File(it)
            file.delete()
        }
        recordAbsoluteFileDir = null
    }

    /**
     * 根据音量分级更新麦克状态
     */
    fun getVoiceLevel(maxLevel: Int): Int {
        if (isRecording && mediaRecorder != null) {
            // maxAmplitude:麦克风当前的音量值
            val ratio = mediaRecorder!!.maxAmplitude.toDouble() / volumeBase
            var db = 0.0  //分贝
            if (ratio > 1) {
                db = 20 * log10(ratio)  //计算分贝（dB）值的标准公式
                return if (db / 4 > maxLevel) {
                    maxLevel
                } else (db / 4).toInt()
            }
        }
        return 0
    }
}