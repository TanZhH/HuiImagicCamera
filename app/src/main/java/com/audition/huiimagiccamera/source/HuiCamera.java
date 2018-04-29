package com.audition.huiimagiccamera.source;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.audition.huiimagiccamera.natives.OpencvNatives;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.source
 * 创建日期： 2018/4/29
 * 描  述：
 * @author TANHUIHUI
 */

public class HuiCamera implements SourceInterface , Camera.PreviewCallback{
    private TextureView mTextureView;
    private Context mContext;
    private int mCameraID;
    private Camera mCamera;
    private int width , height;
    public HuiCamera(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void init(TextureView textureView) {
        this.mTextureView = textureView;
    }

    @Override
    public void start() {
        mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        mCamera = Camera.open(mCameraID);
        SurfaceTexture surfaceTexture = new SurfaceTexture(10);
        openCamera(1536 , 2560);
//        mCamera.setPreviewCallback(this);
        mCamera.setPreviewCallbackWithBuffer(this);
        mCamera.addCallbackBuffer(new byte[((width * height) * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8]);
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();

    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }

    private void openCamera(int width , int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalSize(supportedPreviewSizes, width, height);
        parameters.setPreviewSize(optimalSize.width , optimalSize.height);
        this.width = optimalSize.width;
        this.height = optimalSize.height;
        mCamera.setParameters(parameters);
//        setCameraDisplayOrientation((Activity) mContext , mCameraID , mCamera);
    }

    private Camera.Size getOptimalSize(List<Camera.Size> supportedPreviewSizes, int width, int height) {
        List<Camera.Size> sizeList = new ArrayList<>();
        for (Camera.Size option : supportedPreviewSizes) {
            if (option.width < width && option.height < height) {
                sizeList.add(option);
            } else {
                if (option.width < height && option.height < width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.max(sizeList, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size lhs, Camera.Size rhs) {
                    return Long.signum(lhs.width * lhs.height - rhs.width * rhs.height);

                }
            });
        }
        return sizeList.get(0);
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private static int num = 0;
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mCamera.addCallbackBuffer(data);
        try {
            Log.i("tzh", "onPreviewFrame: 1111111111111");
            if (num % 30 == 0) {
                File file = new File("/sdcard/HuiImagicCamera");
                if (!file.exists() || file.isFile()) {
                    file.mkdirs();
                }
                OpencvNatives.DumpYuvToFile(width, height, data, "/sdcard/HuiImagicCamera" + File.separator + num + ".nv21");
            }

            num++;
        } catch (Exception e) {
            Log.e("tzh", "onPreviewFrame: " +e.toString());
        }
    }
}
