package com.audition.huiimagiccamera.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.utils
 * 创建日期： 2018/4/29
 * 描  述：
 * @author TANHUIHUI
 */

public class CameraTools {
    /**
     * 是否使屏幕常亮
     *
     * @param activity
     */
    public static void keepScreenLongLight(Activity activity , boolean isOpenLight) {
        Window window = activity.getWindow();
        if (isOpenLight) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }
}
