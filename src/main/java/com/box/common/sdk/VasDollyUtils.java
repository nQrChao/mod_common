package com.box.common.sdk;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.box.com.BuildConfig;
import com.box.common.MMKVConfig;
import com.box.common.network.Des;
import com.box.common.utils.subpackage.Base64;
import com.box.common.utils.subpackage.ChannelReaderUtil;
import com.box.other.blankj.utilcode.util.Logs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class VasDollyUtils {
    private static final String TAG = VasDollyUtils.class.getSimpleName();
    private static final String E = !TextUtils.isEmpty(BuildConfig.DEFAULE_CHANNEL_VASID) ? BuildConfig.DEFAULE_CHANNEL_VASID : "aa1000000";
    private static String defaultVasId = !TextUtils.isEmpty(BuildConfig.DEFAULE_CHANNEL_VASID) ? BuildConfig.DEFAULE_CHANNEL_VASID : E;
    private static String vasDollyId = defaultVasId;

    public static String initVasId(Application app) {
        vasDollyId = getVasIdFromApk(app);
        MMKVConfig.INSTANCE.setModVasId(vasDollyId);
        return vasDollyId;
    }


    /**
     * 从apk中获取渠道信息
     */
    public static String getVasIdFromApk(Application app) {
        String vasChannel = ChannelReaderUtil.getChannel(app);
        if (!TextUtils.isEmpty(vasChannel)) {
            byte[] decode = Base64.decode(vasChannel);
            if (decode != null) {
                String channel = new String(decode);
                if (!TextUtils.isEmpty(channel)) {
                    String vasId = "";
                    try {
                        JSONObject jsonObject = new JSONObject(channel);
                        vasId = jsonObject.getString("vasId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(vasId)) {
                        defaultVasId = vasId;
                    }
                }
            } else {
                String vasId = "";
                try {
                    JSONObject jsonObject = new JSONObject(vasChannel);
                    vasId = jsonObject.getString("vasId");
                } catch (JSONException e) {
                }
                if (!TextUtils.isEmpty(vasId)) {
                    defaultVasId = vasId;
                }
            }
            return defaultVasId;
        }

        if (!TextUtils.isEmpty(BuildConfig.DEFAULE_CHANNEL_VASID)) {
            return BuildConfig.DEFAULE_CHANNEL_VASID;
        }

        //从apk包中获取
        ApplicationInfo applicationInfo = app.getApplicationInfo();
        String sourceDir = applicationInfo.sourceDir;
        //注意这里：默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + "vasId";
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("_");
        for (int i = 0; i < split.length; i++) {
        }
        String vasId = defaultVasId;
        if (split.length >= 2) {
            vasId = ret.substring(split[0].length() + 1);
        }

        Log.e("ApkUtils-initVASID", vasId);
        return vasId;
    }

    public static int getChannelGameidFromApk(Application app) {
        //从apk包中获取
        ApplicationInfo applicationInfo = app.getApplicationInfo();
        String sourceDir = applicationInfo.sourceDir;
        //注意这里：默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + "gameid";
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            String[] split = ret.split("_");
            String gameid = null;
            if (split.length >= 2) {
                gameid = ret.substring(split[0].length() + 1);
            }

            assert gameid != null;
            return Integer.parseInt(gameid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public static String getVasId() {
        return vasDollyId;
    }

    public static void setVasId(String vasId) {
        vasDollyId = vasId;
    }

    public static void setDefaultVasId(String vasId) {
        defaultVasId = vasId;
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        MessageDigest mdInst = null;
        try {
            byte[] btInput = s.getBytes();
            mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String MapToString(Map<String, String> map) {
        String returnString = "";
        Set<String> set = map.keySet();
        for (String st : set) {
            returnString += st + "=" + map.get(st) + "&";
        }
        return returnString.substring(0, returnString.length() - 1);
    }

    public static String getSignKey(Map<String, String> params) {
        Map<String, String> newParams = new TreeMap<>();
        for (String key : params.keySet()) {
            String value = params.get(key);
            try {
                //2018.07.12 api sign签名时，如果参数为空，键值也需要加入到签名
                if (!TextUtils.isEmpty(value)) {
                    String encodeValue = URLEncoder.encode(value, "UTF-8");
                    String newValue = encodeValue.replace("*", "%2A");
                    newParams.put(key, newValue);
                } else {
                    newParams.put(key, "");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String sign = (MapToString(newParams) + Des.signKey);
        Logs.e(TAG, "signstr(MD5前串):" + sign);
        return MD5(sign);
    }


    public static boolean checkState(String state) {
        if ("ok".equals(state)) {
            return true;
        }
        if ("err".equals(state)) {
            return false;
        }
        return false;
    }


    public static final Map<String, Integer> apps = new HashMap<String, Integer>();

    public static void loadCustomPkgInfos(Map<String, Integer> apps,Application app) {
        try {
            PackageManager packageManager = app.getPackageManager();
            List<PackageInfo> infos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
            for (PackageInfo info : infos) {
                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    apps.put(info.packageName, info.versionCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
