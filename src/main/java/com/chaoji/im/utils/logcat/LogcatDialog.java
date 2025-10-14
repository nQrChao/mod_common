package com.chaoji.im.utils.logcat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chaoji.common.R;
import com.chaoji.other.hjq.toast.Toaster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * desc   : Logcat 显示窗口
 */
public final class LogcatDialog extends LinearLayout
        implements View.OnLongClickListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, LogcatManager.Callback,
        LogcatAdapter.OnItemLongClickListener, LogcatAdapter.OnItemClickListener {

    private final Context context;
    private final Activity activity;
    private final static String[] ARRAY_LOG_LEVEL = {"Verbose", "Debug", "Info", "Warn", "Error"};
    private final static String[] ARRAY_LOG_LEVEL_PORTRAIT = {"V", "D", "I", "W", "E"};

    private final View mRootView;
    private final View mBarView;
    private final CheckBox mCheckBox;
    private final View mSaveView;
    private final ViewGroup mLevelLayout;
    private final TextView mLevelView;
    private final View mClearView;
    private final View mHideView;
    private final RecyclerView mRecyclerView;
    private final View mDownView;

    private final RelativeLayout llLogInfo;

    private final LinearLayoutManager mLinearLayoutManager;
    private final LogcatAdapter mAdapter;

    private final Button testBtn;

    private String mLogLevel = LogLevel.VERBOSE;

    /**
     * 暂停输出日志标记
     */
    private boolean mPauseLogFlag;

    /**
     * Tag 过滤规则
     */
    private final List<String> mTagFilter = new ArrayList<>();

    /**
     * 当前是否授予了读取日志权限
     */
    private final boolean mGrantedReadLogPermission;

    public LogcatDialog(@NonNull Context context) {
        this(context, null);
    }

    public LogcatDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogcatDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LogcatDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.activity = (Activity) context;
        LogcatConfig.init(context.getApplicationContext());
        LayoutInflater.from(getContext()).inflate(R.layout.logcat_logcat_view, this, true);
        mRootView = findViewById(R.id.ll_log_root);
        mBarView = findViewById(R.id.ll_log_bar);
        mCheckBox = findViewById(R.id.cb_log_switch);
        mSaveView = findViewById(R.id.iv_log_save);
        mLevelLayout = findViewById(R.id.ll_log_level);
        mLevelView = findViewById(R.id.tv_log_level);
        mClearView = findViewById(R.id.iv_log_logcat_clear);
        mHideView = findViewById(R.id.iv_log_logcat_hide);
        mRecyclerView = findViewById(R.id.lv_log_logcat_list);
        mDownView = findViewById(R.id.ib_log_logcat_down);
        llLogInfo = findViewById(R.id.ll_log_info);
        testBtn = findViewById(R.id.testBtn);

        mAdapter = new LogcatAdapter(context);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        mLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAnimation(null);
        mCheckBox.setOnCheckedChangeListener(this);

        mSaveView.setOnClickListener(this);
        mLevelLayout.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        mHideView.setOnClickListener(this);
        mDownView.setOnClickListener(this);
        testBtn.setOnClickListener(this);

        mSaveView.setOnLongClickListener(this);
        mCheckBox.setOnLongClickListener(this);
        mLevelLayout.setOnLongClickListener(this);
        mClearView.setOnLongClickListener(this);
        mHideView.setOnLongClickListener(this);

        mGrantedReadLogPermission = activity.checkSelfPermission(Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            LogcatManager.setCanObtainUid(true);
        } else if (mGrantedReadLogPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogcatManager.setCanObtainUid(true);
        }

        LogcatManager.setCallback(this);
        // 开始捕获
        LogcatManager.start();
        initLogFilter();
        // mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 延迟设置适配器，这样可以提升性能，因为进入这个界面的时候，会频繁添加日志，如果这个时候适配器已经绑定
                // 那么就会引发 itemView 频繁测试和绘制，这样会很卡，并且还会出现 ANR 的情况
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 1000);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLinearLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }, 1200);

        setLogLevel(LogLevel.ERROR);
    }


    @Override
    public void onReceiveLog(LogcatInfo info) {
        // 如果当前没有授予读取日志权限，则只打印当前应用的日志，如果有权限，则显示所有日志
        if (!mGrantedReadLogPermission) {
            try {
                String uidString = info.getUid();
                if (uidString != null && !"".equals(uidString)) {
                    int uid = Integer.parseInt(uidString);
                    if (uid != android.os.Process.myUid()) {
                        // 这个日志必须是当前应用打印的
                        return;
                    }
                }
            } catch (NumberFormatException ignore) {
                // Android 10 及以下机型获取日志打印的 uid 会返回几个空格
                // java.lang.NumberFormatException: For input string: "    "
                // 切勿在此处打印任何异常或者日志，避免造成无限递归
            }
        }

        // 这个 Tag 必须不在过滤列表中
        if (mTagFilter.contains(info.getTag())) {
            return;
        }
        mRecyclerView.post(new LogRunnable(info));
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mCheckBox) {
            Toaster.show(R.string.logcat_capture);
        } else if (v == mSaveView) {
            Toaster.show(R.string.logcat_save);
        } else if (v == mLevelView) {
            Toaster.show(R.string.logcat_level);
        } else if (v == mClearView) {
            Toaster.show(R.string.logcat_empty);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveView) {
            try {
                File file = LogcatUtils.saveLogToFile(context, mAdapter.getData());
                Toaster.show(getResources().getString(R.string.logcat_save_succeed) + file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                Toaster.show(getResources().getString(R.string.logcat_save_fail));
            }
        } else if (v == mLevelLayout) {
            new ChooseWindow(activity)
                    .setList(ARRAY_LOG_LEVEL)
                    .setListener(new ChooseWindow.OnListener() {
                        @Override
                        public void onSelected(int position) {
                            switch (position) {
                                case 0:
                                    setLogLevel(LogLevel.VERBOSE);
                                    break;
                                case 1:
                                    setLogLevel(LogLevel.DEBUG);
                                    break;
                                case 2:
                                    setLogLevel(LogLevel.INFO);
                                    break;
                                case 3:
                                    setLogLevel(LogLevel.WARN);
                                    break;
                                case 4:
                                    setLogLevel(LogLevel.ERROR);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .show();
        } else if (v == mClearView) {
            LogcatManager.clear();
            mAdapter.clearData();
        } else if (v == mHideView) {
            mLinearLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            //llLogInfo.setVisibility((llLogInfo.getVisibility() == GONE) ? VISIBLE : GONE);
        } else if (v == mDownView) {
            // 滚动到列表最底部
            mLinearLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
        } else if (v == testBtn) {
            llLogInfo.setVisibility((llLogInfo.getVisibility() == GONE) ? VISIBLE : GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Toaster.show(R.string.logcat_capture_pause);
            LogcatManager.pause();
            mPauseLogFlag = true;
        } else {
            LogcatManager.resume();
            mPauseLogFlag = false;
        }
    }

    @Override
    public void onItemClick(LogcatInfo info, int position) {
        mAdapter.onItemClick(position);
    }

    @Override
    public boolean onItemLongClick(LogcatInfo info, final int position) {
        new ChooseWindow(activity)
                .setList(R.string.logcat_options_copy, R.string.logcat_options_share, R.string.logcat_options_delete, R.string.logcat_options_shield)
                .setListener(new ChooseWindow.OnListener() {
                    @Override
                    public void onSelected(final int location) {
                        switch (location) {
                            case 0:
                                copyLog(position);
                                break;
                            case 1:
                                shareLog(position);
                                break;
                            case 2:
                                mAdapter.removeItem(position);
                                break;
                            case 3:
                                addFilter(mAdapter.getItem(position).getTag());
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
        return true;
    }

    private void copyLog(int position) {
        ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            manager.setPrimaryClip(ClipData.newPlainText("log", mAdapter.getItem(position).getContent()));
            Toaster.show(R.string.logcat_copy_succeed);
        } else {
            Toaster.show(R.string.logcat_copy_fail);
        }
    }

    private void shareLog(int position) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mAdapter.getItem(position).getContent());
        activity.startActivity(Intent.createChooser(intent, getResources().getString(R.string.logcat_options_share)));
    }

    private void setLogLevel(String level) {
        if (level.equalsIgnoreCase(mLogLevel)) {
            refreshLogLevelLayout();
            return;
        }

        mLogLevel = level;
        mAdapter.setLogLevel(level);
        LogcatConfig.setLogLevelConfig(level);
        refreshLogLevelLayout();
    }

    private void refreshLogLevelLayout() {
        String[] arrayLogLevel;
        if (LogcatUtils.isPortrait(context)) {
            arrayLogLevel = ARRAY_LOG_LEVEL_PORTRAIT;
        } else {
            arrayLogLevel = ARRAY_LOG_LEVEL;
        }
        switch (mLogLevel) {
            case LogLevel.VERBOSE:
                mLevelView.setText(arrayLogLevel[0]);
                break;
            case LogLevel.DEBUG:
                mLevelView.setText(arrayLogLevel[1]);
                break;
            case LogLevel.INFO:
                mLevelView.setText(arrayLogLevel[2]);
                break;
            case LogLevel.WARN:
                mLevelView.setText(arrayLogLevel[3]);
                break;
            case LogLevel.ERROR:
                mLevelView.setText(arrayLogLevel[4]);
                break;
            default:
                break;
        }
    }

    private class LogRunnable implements Runnable {

        private final LogcatInfo info;

        private LogRunnable(LogcatInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            mAdapter.addItem(info);
        }
    }

    /**
     * 初始化 Tag 过滤器
     */
    private void initLogFilter() {
        try {
            mTagFilter.addAll(LogcatUtils.readTagFilter(context));
        } catch (IOException e) {
            e.printStackTrace();
            Toaster.show(R.string.logcat_read_config_fail);
        }

        String[] list = getResources().getStringArray(R.array.logcat_filter_list);
        for (String tag : list) {
            if (tag == null || "".equals(tag)) {
                continue;
            }
            if (mTagFilter.contains(tag)) {
                continue;
            }
            mTagFilter.add(tag);
        }
    }

    /**
     * 添加过滤的 TAG
     */
    private void addFilter(String tag) {
        if ("".equals(tag)) {
            return;
        }
        if (mTagFilter.contains(tag)) {
            return;
        }
        mTagFilter.add(tag);

        try {
            List<String> newTagFilter = new ArrayList<>(mTagFilter);
            newTagFilter.removeAll(Arrays.asList(getResources().getStringArray(R.array.logcat_filter_list)));

            File file = LogcatUtils.writeTagFilter(context, newTagFilter);
            Toaster.show(getResources().getString(R.string.logcat_shield_succeed) + file.getPath());

            // 从列表中删除关于这个 Tag 的日志
            List<LogcatInfo> data = mAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                LogcatInfo info = data.get(i);
                if (info.getTag().equals(tag)) {
                    mAdapter.removeItem(i);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toaster.show(R.string.logcat_shield_fail);
        }
    }

    public void onBackPressed() {
        // 移动到上一个任务栈
        activity.moveTaskToBack(false);
    }

    protected void onResume() {
        if (mPauseLogFlag) {
            return;
        }
        LogcatManager.resume();
    }

    protected void onPause() {
        if (mPauseLogFlag) {
            return;
        }
        LogcatManager.pause();
    }
    protected void onDestroy() {
        LogcatManager.destroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshLogLevelLayout();
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }



}