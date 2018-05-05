package com.seu.magicfilter.present;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.R;
import com.seu.magicfilter.camera.CameraEngine;
import com.seu.magicfilter.camera.utils.CameraInfo;
import com.seu.magicfilter.filter.base.MagicCameraInputFilter;
import com.seu.magicfilter.minterface.InterActivityParms;
import com.seu.magicfilter.widget.base.MagicBaseView;

/**
 * @author tanzhuohui
 * @date 2018/3/12
 */

public class Present {
    private MagicEngine magicEngine;
    private GLSurfaceView glSurfaceView;
    private MagicCameraInputFilter cameraInputFilter;
    private SurfaceTexture surfaceTexture;
    private InterActivityParms interActivityParms;
    private PopupWindow mBeatifulPopup;

    public Present(InterActivityParms interActivityParms , MagicEngine magicEngine , GLSurfaceView glSurfaceView) {
        this.magicEngine = magicEngine;
        this.glSurfaceView = glSurfaceView;
        this.interActivityParms = interActivityParms;
    }

    public void cameraSwitch(){
        magicEngine.switchCamera();
    }

    public void openCamera(MagicCameraInputFilter cameraInputFilter , SurfaceTexture surfaceTexture){
        this.cameraInputFilter = cameraInputFilter;
        this.surfaceTexture = surfaceTexture;
        CameraEngine.openCamera(this , cameraInputFilter , surfaceTexture , CameraEngine.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK);
    }


    public void setImageWH(int previewWidth , int previewHeight){
        ((MagicBaseView)glSurfaceView).setImageWidth(previewWidth);
        ((MagicBaseView)glSurfaceView).setImageHeight(previewHeight);
    }

    public void setImageHW(int previewWidth , int previewHeight){
        ((MagicBaseView)glSurfaceView).setImageWidth(previewHeight);
        ((MagicBaseView)glSurfaceView).setImageHeight(previewWidth);
    }

    public void adjustSize(int orientation , boolean isFront , boolean flipVertical){
        ((MagicBaseView)glSurfaceView).adjustSize(orientation, isFront, flipVertical);
    }

    public void releaseCamera(){
        CameraEngine.stopPreview();
        CameraEngine.releaseCamera();
    }

    public void setSwitchSrc(boolean filp){
        interActivityParms.setSwicthSrc(filp);
    }

    public void setBeatiful(int num) {
        int whith = 0;
        if(num < 10){
            interActivityParms.setSeekBarNum(0);
            whith = 0;
        }else if(num >= 10 && num < 30){
            interActivityParms.setSeekBarNum(20);
            whith = 1;
        }else if(num >= 30 && num < 50){
            interActivityParms.setSeekBarNum(40);
            whith = 2;
        }else if(num >= 50 && num < 70){
            interActivityParms.setSeekBarNum(60);
            whith = 3;
        }else if(num >= 70 && num < 90){
            interActivityParms.setSeekBarNum(80);
            whith = 4;
        }else if(num >= 90 && num <= 100){
            interActivityParms.setSeekBarNum(100);
            whith = 5;
        }
        magicEngine.setBeautyLevel(whith);
    }
}
