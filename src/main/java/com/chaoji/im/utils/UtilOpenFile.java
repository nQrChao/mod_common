package com.chaoji.im.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.chaoji.other.blankj.utilcode.util.AppUtils;
import com.chaoji.im.sdk.ImSDK;

import java.io.File;

/**
 * 用于打开手机本地文件
 */
public class UtilOpenFile {
    public static void openFile(Context context, String path) {
        String suffix = GetFilePathFromUri.getFileSuffix(path);
        Intent intent;
        //doc或docx文件
        if ("doc".equals(suffix) || "docx".equals(suffix)) {
            intent = getWordFileIntent(path);
        }
        //excel
        else if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
            intent = getExcelFileIntent(path);
        }
        //ppt
        else if (suffix.equals("ppt") || suffix.equals("pptx")) {
            intent = getPptFileIntent(path);
        }
        //pdf
        else if (suffix.equals("pdf")) {
            intent = getPdfIntent(path);
        }
        //图片
        else if (suffix.equals("jpg") || suffix.equals("png")
            || suffix.equals("gif") || suffix.equals("bmp")
            || suffix.equals("jpeg")) {
            intent = getImageIntent(path);
        }
        //文本
        else if (suffix.equals("txt")) {
            intent = getTextFileIntent(path, false);
        }
        //html
        else if (suffix.equals("htm") || suffix.equals("html")) {
            intent = getHtmlFileIntent(path);
        }
        //chm
        else if (suffix.equals("chm")) {
            intent = getChmFileIntent(path);
        } else if (suffix.equals("apk")) {
            intent = getApkFileIntent(path);
        }
        //音频
        else if (suffix.equals("mp3") || suffix.equals("wav")
            || suffix.equals("wma") || suffix.equals("ogg")
            || suffix.equals("ape") || suffix.equals("acc")) {
            intent = getAudioFileIntent(path);
        }
        //视频
        else if (suffix.equals("avi") || suffix.equals("mov")
            || suffix.equals("asf") || suffix.equals("wmv")
            || suffix.equals("navi") || suffix.equals("3gp")
            || suffix.equals("ram") || suffix.equals("mkv")
            || suffix.equals("flv") || suffix.equals("mp4")
            || suffix.equals("rmvb") || suffix.equals("mpg")) {
            intent = getVideoFileIntent(path);
        } else {
            intent = getAllIntent(path);
        }
        context.startActivity(intent);
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        setFlags(intent);
        Uri uri = getUri(path);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getAllIntent(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        setFlags(intent);
        Uri uri = getUri(path);
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //获取一个用于打开word文档的intent
    public static Intent getWordFileIntent(String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        setFlags(intent);

        Uri uri = getUri(path);
        intent.setDataAndType(uri, "application/msword");

        return intent;
    }

    //获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);

        Uri uri = getUri(path);
        intent.setDataAndType(uri, "application/vnd.ms-excel");

        return intent;

    }

    //获取一个用于打开HTML的intent
    public static Intent getHtmlFileIntent(String path) {
        Uri uri =
            Uri.parse(path).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme(
                "content").encodedPath(path).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(uri, "text/html");

        return intent;
    }

    //获取一个用于打开图片的intent
    public static Intent getImageIntent(String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);

        Uri uri = getUri(path);
        intent.setDataAndType(uri, "image/*");

        return intent;
    }

    //获取一个用于打开PDF的intent
    public static Intent getPdfIntent(String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);

        Uri uri = getUri(path);
        intent.setDataAndType(uri, "application/pdf");

        return intent;
    }

    //获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String path, boolean paramBoolean) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);
        Uri uri = getUri(path);
        intent.setDataAndType(uri, "text/plain");

        return intent;

    }

    //android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        setFlags(intent);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);

        Uri uri = getUri(param);
        intent.setDataAndType(uri, "audio/*");

        return intent;

    }

    //android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        setFlags(intent);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);

        Uri uri = getUri(param);
        intent.setDataAndType(uri, "video/*");

        return intent;

    }

    //android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);

        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/x-chm");

        return intent;

    }

    //android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        setFlags(intent);

        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

        return intent;

    }

    /**
     * 适配android7.0的文件打开
     */
    private static void setFlags(Intent intent) {
        //android7.0需要设置uri阅读权限
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public static Uri getUri(String UrlStr) {
        File file = new File(UrlStr);
        Uri uri;
        uri = FileProvider.getUriForFile(ImSDK.Companion.getInstance().getContext(), AppUtils.getAppPackageName() + ".fileProvider", file);
        return uri;
    }

}
