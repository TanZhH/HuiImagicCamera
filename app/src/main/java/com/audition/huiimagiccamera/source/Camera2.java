package com.audition.huiimagiccamera.source;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Size;
import android.view.TextureView;

import com.audition.huiimagiccamera.utils.PreferenceHelper;

import java.util.Arrays;
import java.util.Collections;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.source
 * 创建日期： 2018/3/4
 * 描  述：   Camera2数据源
 * @author tanzhuohui
 */

public class Camera2 implements SourceInterface, TextureView.SurfaceTextureListener {
    private TextureView mTextureView;
    private Context mContext;
    private String mCameraID;
    private Size mPreviewSize;
    private Size mCaptureSize;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    public Camera2(Context context) {
        this.mContext = context;
    }

    @Override
    public void init(int width , int height) {
        startCameraThread();
        if(!mTextureView.isAvailable()){
            mTextureView.setSurfaceTextureListener(this);
        }else {
            // TODO: 2018/4/16 startPreview
        }
    }

    private void startCameraThread(){
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //当SurefaceTexture可用时，设置相机参数并打开相机
        setupCamera(width , height);
    }

    private void setupCamera(int width, int height) {
        //获取摄像头的管理者CameraManager
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (manager != null) {
                for (String cameraID : manager.getCameraIdList()) {
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
                    Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                    //默认打开后置摄像头
                    if(facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){
                        continue;
                    }
                    //获取StreamConfiguretionMap，管理摄像头支持的所有输出格式和尺寸
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if(map != null){
                        //根据TextureView的尺寸设置预览尺寸
                        // TODO: 2018/4/16 contiue;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
