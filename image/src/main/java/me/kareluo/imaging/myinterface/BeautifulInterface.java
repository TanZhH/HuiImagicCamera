package me.kareluo.imaging.myinterface;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   me.kareluo.imaging.myinterface
 * 创建日期： 2018/5/15
 * 描  述：
 */

public interface BeautifulInterface {
    ByteBuffer setBitmap(Bitmap bitmap);
    Bitmap setMopi(ByteBuffer byteBuffer , int progress);
    Bitmap setMeibai(ByteBuffer byteBuffer , int progress);
}
