package com.seu.magicfilter.camera;

import java.io.IOException;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceView;

import com.seu.magicfilter.camera.utils.CameraUtils;
import com.seu.magicfilter.filter.base.MagicCameraInputFilter;
import com.seu.magicfilter.present.Present;
import com.seu.magicfilter.widget.base.MagicBaseView;

public class CameraEngine {
    private static Camera camera = null;
    private static int cameraID = CameraInfo.CAMERA_FACING_FRONT;
    private static SurfaceTexture surfaceTexture;
    private static SurfaceView surfaceView;
    private static Present present;
    private static MagicCameraInputFilter cameraInputFilter;

    public static Camera getCamera(){
        return camera;
    }

    public static boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraID);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraID = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static void releaseCamera(){
        if(camera != null){

            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            camera.getParameters();
        return null;
    }

    public static void switchCamera(){
        releaseCamera();
        cameraID = cameraID == CameraInfo.CAMERA_FACING_BACK ? CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK;
        boolean isFilp = cameraID == CameraInfo.CAMERA_FACING_BACK;
//        openCamera(cameraID);
//        startPreview(surfaceTexture);
        openCamera(present , cameraInputFilter , surfaceTexture , isFilp);
    }

    public static void openCamera(Present present , MagicCameraInputFilter cameraInputFilter
            , SurfaceTexture surfaceTexture , boolean isFilp){
        CameraEngine.present = present;
        CameraEngine.cameraInputFilter = cameraInputFilter;
        if(CameraEngine.getCamera() == null) {
            boolean b = CameraEngine.openCamera();
        }
        com.seu.magicfilter.camera.utils.CameraInfo info = CameraEngine.getCameraInfo();
        if(info.orientation == 90 || info.orientation == 270){
            present.setImageHW(info.previewWidth , info.previewHeight);
        }else{
            present.setImageWH(info.previewWidth , info.previewHeight);
        }
        cameraInputFilter.onInputSizeChanged(info.previewWidth, info.previewHeight);
        present.adjustSize(info.orientation, info.isFront, isFilp);
        present.setSwitchSrc(isFilp);
        if(surfaceTexture != null) {
            CameraEngine.startPreview(surfaceTexture);
        }
    }

    private static void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        Size previewSize = CameraUtils.getLargePreviewSize(camera);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Size pictureSize = CameraUtils.getLargePictureSize(camera);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setRotation(90);
        camera.setParameters(parameters);
    }

    private static Size getPreviewSize(){
        return camera.getParameters().getPreviewSize();
    }

    private static Size getPictureSize(){
        return camera.getParameters().getPictureSize();
    }

    public static void startPreview(SurfaceTexture surfaceTexture){
        if(camera != null) {
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startPreview(){
        if(camera != null)
            camera.startPreview();
    }

    public static void stopPreview(){
        camera.stopPreview();
    }

    public static void setRotation(int rotation){
        Camera.Parameters params = camera.getParameters();
        params.setRotation(rotation);
        camera.setParameters(params);
    }

    public static void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static com.seu.magicfilter.camera.utils.CameraInfo getCameraInfo(){
        com.seu.magicfilter.camera.utils.CameraInfo info = new com.seu.magicfilter.camera.utils.CameraInfo();
        Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraID == CameraInfo.CAMERA_FACING_FRONT;
        size = getPictureSize();
        info.pictureWidth = size.width;
        info.pictureHeight = size.height;
        return info;
    }

    public static void setCameraInputFilter(MagicCameraInputFilter cameraInputFilter) {
        CameraEngine.cameraInputFilter = cameraInputFilter;
    }

    public static void setPresent(Present present) {
        CameraEngine.present = present;
    }

    public static int getCameraID() {
        return cameraID;
    }
}