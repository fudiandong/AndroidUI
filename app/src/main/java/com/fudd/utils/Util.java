package com.fudd.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by fudd-office on 2017-3-14 10:58.
 * Email: 5036175@qq.com
 * QQ: 5036175
 * Description:
 */

public class Util {
    public static boolean isDebugMode(Context context){
        PackageManager pm = context.getPackageManager();

        try {
            ApplicationInfo  info = pm.getApplicationInfo(context.getPackageName(),0);
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean useStrictMode(Context context){
        return  isDebugMode(context) && Build.VERSION.SDK_INT >= 9;
    }
}
