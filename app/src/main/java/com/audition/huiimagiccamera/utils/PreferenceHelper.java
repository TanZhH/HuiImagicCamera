package com.audition.huiimagiccamera.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author tanzhuohui
 * @date 2018/3/12
 */

public class PreferenceHelper {
    public static String getCameraId(Context context){
        SharedPreferences preferences;
        String cameraId;
        if(context != null){
            preferences = context.getSharedPreferences("CameraConfig" , Context.MODE_PRIVATE);
            cameraId = preferences.getString("camera" , "0");
        }else {
            cameraId = "0";
        }
        return cameraId;
    }

    public static boolean writeCameraId(Context context , String value){
        SharedPreferences preferences = context.getSharedPreferences("CameraConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cameraid" , value);
        return editor.commit();
    }

}
