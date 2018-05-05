package com.audition.huiimagiccamera.present;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.camera.CameraEngine;
import com.seu.magicfilter.camera.utils.CameraInfo;
import com.seu.magicfilter.filter.base.MagicCameraInputFilter;
import com.seu.magicfilter.widget.base.MagicBaseView;

/**
 * @author tanzhuohui
 * @date 2018/3/12
 */

public class Present {
    private MagicEngine magicEngine;
    private GLSurfaceView glSurfaceView;

    public Present(MagicEngine magicEngine , GLSurfaceView glSurfaceView) {
        this.magicEngine = magicEngine;
        this.glSurfaceView = glSurfaceView;
    }

    public void cameraSwitch(){
        magicEngine.switchCamera();
    }

    public void openCamera(MagicCameraInputFilter cameraInputFilter , SurfaceTexture surfaceTexture){
        if(CameraEngine.getCamera() == null) {
            boolean b = CameraEngine.openCamera();
        }
        CameraInfo info = CameraEngine.getCameraInfo();
        if(info.orientation == 90 || info.orientation == 270){
            ((MagicBaseView)glSurfaceView).setImageWidth(info.previewHeight);
            ((MagicBaseView)glSurfaceView).setImageHeight(info.previewWidth);
        }else{
            ((MagicBaseView)glSurfaceView).setImageWidth(info.previewWidth);
            ((MagicBaseView)glSurfaceView).setImageHeight(info.previewHeight);
        }
        cameraInputFilter.onInputSizeChanged(info.previewWidth, info.previewHeight);
        ((MagicBaseView)glSurfaceView).adjustSize(info.orientation, info.isFront, false);
        if(surfaceTexture != null) {
            CameraEngine.startPreview(surfaceTexture);
        }
    }
}
