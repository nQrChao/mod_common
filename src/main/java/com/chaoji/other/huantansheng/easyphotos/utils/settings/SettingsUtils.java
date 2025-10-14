package com.chaoji.other.huantansheng.easyphotos.utils.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.chaoji.other.huantansheng.easyphotos.constant.Code;


public class SettingsUtils {

    public static void startMyApplicationDetailsForResult(Activity cxt, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
        cxt.startActivityForResult(intent, Code.REQUEST_SETTING_APP_DETAILS);
    }
}
