package com.audition.huiimagiccamera.natives;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.natives
 * 创建日期： 2018/4/27
 * 描  述：
 * @author TANHUIHUI
 */

public class OpencvNatives {
    static{
        System.loadLibrary("native-opencv");
    }

    public static native void DumpYuvToFile(int width, int height, byte[] data, String path);


}
