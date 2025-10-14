package com.chaoji.im.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.chaoji.other.blankj.utilcode.util.AppUtils;
import com.chaoji.other.blankj.utilcode.util.FileUtils;
import com.chaoji.other.blankj.utilcode.util.PathUtils;
import com.chaoji.other.blankj.utilcode.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UtilDir {
    private static final String logFileDirName = "log";
    private static final String gameFileDirName = "game";
    private static final String downloadFileName = "downloadFile";
    private static final String publicResDirName = "publicRes";
    private static final String screenShotDirName = "screenShot";
   public static final String screenRecordingDirName = "screenRecording";

    public static String getPath(String url) {
        String path = "";
        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            String p1 = url.replace("http://", "");
            String p2 = p1.replace("https://", "");
            String[] p = p2.split("/");
            path = p2.replace(p[0], "");
            path = path.replace(fileName, "");
            return (path.length() < 2) ? p2 : path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 根据传入的URL获取域名(带http://)
     */
    public static String getDomain(String url) {
        String domain = url;
        try {
            String p1 = url.replace("http://", "");
            domain = p1.replace("https://", "");
            return StringUtils.isEmpty(domain) ? url : domain;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain;
    }

    /**
     * 根据传入的URL获取域名(带http://)
     */
    public static String getDomainFile(String fileUrl) {
        String domain = fileUrl;
        try {
            String p1 = fileUrl.replace("file://", "");
            domain = p1.replace("file:/", "");
            return StringUtils.isEmpty(domain) ? fileUrl : domain;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain;
    }

    /**
     * 根据传入的URL获取域名(带http://)
     */
    public static String getDomain2(String url) {
        String domain = "";
        try {
            String[] p = url.split("/");
            domain = p[0] + p[1] + p[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain;
    }


    /**
     * 将视频保存到系统相册
     */
    public static boolean saveVideoToAlbum(Context context, String videoFilePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return saveVideoToAlbumBeforeQ(context, videoFilePath);
        } else {
            return saveVideoToAlbumAfterQ(context, videoFilePath);
        }
    }


    /**
     * 将视频保存到系统相册
     */
    public static boolean saveVideoToAlbum(Context context, File videoFile) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return saveVideoToAlbumBeforeQ(context, videoFile);
        } else {
            return saveVideoToAlbumAfterQ(context, videoFile);
        }
    }


    private static boolean saveVideoToAlbumBeforeQ(Context context, String videoFilePath) {
        File tempFile = new File(videoFilePath);
        return saveVideoToAlbumBeforeQ(context,tempFile);
    }

    private static boolean saveVideoToAlbumBeforeQ(Context context, File videoFile) {
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File destFile = new File(picDir, context.getPackageName() + File.separator + videoFile.getName());
        FileInputStream ins = null;
        BufferedOutputStream ous = null;
        try {
            ins = new FileInputStream(videoFile);
            ous = new BufferedOutputStream(new FileOutputStream(destFile));
            long nRead = 0L;
            byte[] buf = new byte[1024];
            int n;
            while ((n = ins.read(buf)) > 0) {
                ous.write(buf, 0, n);
                nRead += n;
            }
            MediaScannerConnection.scanFile(context, new String[]{destFile.getAbsolutePath()}, new String[]{"video/*"}, (path, uri) -> {
                Log.e("saveVideoToAlbum: " , path + " " + uri);
                // Scan Completed
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ins != null) {
                    ins.close();
                }
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存视频到相册、文件夹名称为appName
     */
    private static boolean saveVideoToAlbumAfterQ(Context context, String videoFilePath) {
        File destFile = FileUtils.getFileByPath(videoFilePath);
        return saveVideoToAlbumAfterQ(context,destFile);
    }

    /**
     * 保存视频到相册、文件夹名称为appName
     */
    private static boolean saveVideoToAlbumAfterQ(Context context, File videoFile) {
        ContentValues values = new ContentValues();
        Uri uriSavedVideo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + AppUtils.getAppName());
            values.put(MediaStore.Video.Media.TITLE, videoFile.getName());
            values.put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.getName());
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            uriSavedVideo = context.getContentResolver().insert(collection, values);
        } else {
            values.put(MediaStore.Video.Media.TITLE, videoFile.getName());
            values.put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.getName());
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
            uriSavedVideo = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.IS_PENDING, 1);
        }
        ParcelFileDescriptor pfd;
        try {
            pfd = context.getContentResolver().openFileDescriptor(uriSavedVideo, "w");
            FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
            FileInputStream in = new FileInputStream(videoFile);
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            pfd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear();
            values.put(MediaStore.Video.Media.IS_PENDING, 0);
            context.getContentResolver().update(uriSavedVideo, values, null, null);
            return true;
        }
        return false;
    }



    public static String APK_FILE_PATH;

    //内存应用文件路径
    public static String getAppFileDir() {
        return PathUtils.getInternalAppFilesPath() + File.separator;
    }

    //app存储文件游戏目录
    public static String getGameFileDir() {
        return getAppFileDir() + gameFileDirName + File.separator;
    }

    //下载文件的目录
    public static String getDownloadFileName() {
        return UtilDir.APK_FILE_PATH + downloadFileName + File.separator;
    }

    //截图目录
    public static String getScreenShotDir() {
        return UtilDir.APK_FILE_PATH + screenShotDirName + File.separator;
    }

    //log 文件存储目录
    public static String getLogDir() {
        return UtilDir.APK_FILE_PATH + logFileDirName + File.separator;
    }

    //录像目录
    public static String getAppFileMediaDir() {
        return getAppFileDir() + screenRecordingDirName + File.separator;
    }

    //公共缓存资源目录
    public static String getPublicResFilePath() {
        return UtilDir.APK_FILE_PATH + publicResDirName + File.separator;
    }

    //私有缓存资源目录
    public static String getOutFilePath(String gameId) {
        return UtilDir.APK_FILE_PATH + gameId + File.separator + gameId + File.separator;
    }

    public static String getGameZipFilePath(String gameId) {
        return UtilDir.APK_FILE_PATH + gameId + ".zip";
    }

    public static String getZipOutPath(String gameId) {
        //return AppDirUtils.APK_FILE_PATH + gameId + File.separator;
        return getOutFilePath(gameId);
    }

    public static String getMimeType(String name) {
        switch (name) {
            case "jpeg":
            case "jpg":
            case "JPG":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "png":
            case "PNG":
                return "image/png";
            case "ico":
                return "image/x-icon";
            case "mp3":
                return "audio/mpeg";
            case "css":
                return "text/css";
            case "htm":
            case "html":
                return "text/html";
            case "txt":
            case "atlas":
                return "text/plain";
            case "bin":
            case "json":
            case "_json":
            case "fnt":
            case "sk":
                return "application/octet-stream";
            case "js":
                return "application/x-javascript";
//			case "json":return "application/json";
            default:
                return "file/*";
        }
    }

}
