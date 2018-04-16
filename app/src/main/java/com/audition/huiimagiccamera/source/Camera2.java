package com.audition.huiimagiccamera.source;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
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

public class Camera2 implements SourceInterface {
    private int CameraId = -1;
    private TextureView textureView;
    private Context mContext;

    public Camera2(Context context) {
        this.mContext = context;
    }

    @Override
    public void init(int width , int height) {
        setUpCameraOutputs(width , height);
    }

    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = PreferenceHelper.getCameraId(mContext);
            if (manager != null) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                Size largest = Collections.max(Arrays.asList(map.getOutputSizes()))
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
}
