package com.audition.huiimagiccamera.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.audition.huiimagiccamera.natives.OpencvNatives;
import com.audition.huiimagiccamera.utils.PreferenceHelper;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.source
 * 创建日期： 2018/3/4
 * 描  述：   Camera2数据源
 *
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
    private Handler mainHandler;
    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mCameraCaptureSession;
    private int width , height;

    public Camera2(Context context) {
        this.mContext = context;
        mainHandler = new Handler(Looper.getMainLooper());

    }

    @Override
    public void init(TextureView textureView) {
        this.mTextureView = textureView;
        startCameraThread();
    }

    private void startCameraThread() {
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }


    @Override
    public void start() {
        if (!mTextureView.isAvailable()) {
            mTextureView.setSurfaceTextureListener(this);
        } else {
            startPreview();
        }
    }

    @Override
    public void stop() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    @Override
    public void destory() {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //当SurefaceTexture可用时，设置相机参数并打开相机
        setupCamera(width, height);
        openCamera();
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (manager != null) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                manager.openCamera(mCameraID, mStateCallback, mCameraHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }
    };

    private void startPreview() {
        SurfaceTexture mSurfaceTexture = mTextureView.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(mSurfaceTexture);
        mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG,1);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler);
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(previewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        mCaptureRequest = mCaptureRequestBuilder.build();
                        mCameraCaptureSession = session;
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();

        }
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
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        continue;
                    }
                    //获取StreamConfiguretionMap，管理摄像头支持的所有输出格式和尺寸
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (map != null) {
                        //根据TextureView的尺寸设置预览尺寸
                        mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                        this.width = mPreviewSize.getWidth();
                        this.height = mPreviewSize.getHeight();
                        //获取相机支持的最大拍照尺寸
                        mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                            @Override
                            public int compare(Size lhs, Size rhs) {
                                return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                            }
                        });
                    }
                    setupImageReader();
                    mCameraID = cameraID;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupImageReader() {
        //2代表ImageReader中最多可以获取两帧图像流
        mImageReader = ImageReader.newInstance(mCaptureSize.getWidth(), mCaptureSize.getHeight(),
                ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
//                mCameraHandler.post(new imageSaver(reader.acquireNextImage()));
            }
        }, mCameraHandler);
    }

    private Size getOptimalSize(Size[] outputSizes, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : outputSizes) {
            if (option.getWidth() > width && option.getHeight() > height) {
                sizeList.add(option);
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return outputSizes[0];
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

    private static int num = 0;
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //进行相片存储
//            mCameraDevice.close();
            //mSurfaceView.setVisibility(View.GONE);//存疑
            Log.e("tzh", "onImageAvailable: is begin");
            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            if(num % 30 == 0) {
                File file = new File("/sdcard/HuiImagicCamera");
                if(!file.exists() || file.isDirectory()){
                    try {
                        boolean newFile = file.createNewFile();
                        Log.e("tzh", "onImageAvailable: newFile :  " + newFile );
                    } catch (IOException e) {
                        Log.e("tzh", "onImageAvailable: " + e.toString() );
                    }
                }
                OpencvNatives.DumpYuvToFile(width, height, bytes, "/sdcard/HuiImagicCamera" + File.separator + num + ".nv21");
            }
//            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            if (bitmap != null) {
//                img_show.setImageBitmap(bitmap);
//            }
        }
    };
}
