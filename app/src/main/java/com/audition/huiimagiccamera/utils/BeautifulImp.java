package com.audition.huiimagiccamera.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.seu.magicfilter.beautify.MagicJni;

import java.nio.ByteBuffer;

import me.kareluo.imaging.myinterface.BeautifulInterface;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.utils
 * 创建日期： 2018/5/15
 * 描  述：
 */

public class BeautifulImp implements BeautifulInterface {
    @Override
    public Bitmap setMopi(Bitmap bitmap, int progress) {
        ByteBuffer byteBuffer = MagicJni.jniStoreBitmapData(bitmap);
        MagicJni.jniInitMagicBeautify(byteBuffer);
        Log.e("tzh", "setMopi: " + progress +"     "+ progress/10);
        MagicJni.jniStartSkinSmooth((float) (progress/10));
        return MagicJni.jniGetBitmapFromStoredBitmapData(byteBuffer);
    }

    @Override
    public Bitmap setMeibai(Bitmap bitmap, int progress) {
        ByteBuffer byteBuffer = MagicJni.jniStoreBitmapData(bitmap);
        MagicJni.jniInitMagicBeautify(byteBuffer);
        Log.e("tzh", "setmeibai: " + progress/20);
        MagicJni.jniStartWhiteSkin((float)(progress/20));
        return MagicJni.jniGetBitmapFromStoredBitmapData(byteBuffer);
    }
}
