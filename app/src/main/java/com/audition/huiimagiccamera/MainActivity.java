package com.audition.huiimagiccamera;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audition.huiimagiccamera.natives.OpencvNatives;
import com.audition.huiimagiccamera.source.Camera2;
import com.audition.huiimagiccamera.source.HuiCamera;
import com.audition.huiimagiccamera.utils.CameraTools;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.widget.MagicCameraView;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.MainActivity
 * 创建日期： 2018/3/4
 * 描  述：  MainActivity
 * @author  tanzhuohui
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextureView mTextureView;
    private Camera2 mCamera2;
    private HuiCamera huiCamera;
    private MagicCameraView mCameraView;
    private MagicEngine magicEngine;
    private ImageView mSwichImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉titlebar-全屏模式
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //细节supportRequestWindowFeature一定要在setContentView之前设置
        setContentView(R.layout.activity_main);
        //屏幕常亮
        CameraTools.keepScreenLongLight(this , true);
        //沉浸式状态栏
        CameraTools.fullScreen(this);
        initView();
        init();
    }

    private void init() {
        MagicEngine.Builder builder = new MagicEngine.Builder();
        magicEngine = builder.build(mCameraView);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.y;
        mCameraView.setLayoutParams(params);
        magicEngine.setFilter(MagicFilterType.NONE);

    }

    private void initView() {
//        mTextureView = (TextureView) findViewById(R.id.tv_surface);
        mCameraView = (MagicCameraView) findViewById(R.id.glsurfaceview_camera);
        mSwichImage = (ImageView) findViewById(R.id.iv_swich);
        mSwichImage.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mCamera2 = new Camera2(this);
//        mCamera2.init(mTextureView);
//        mCamera2.start();
//        huiCamera = new HuiCamera(this);
//        huiCamera.init(mTextureView);
//        huiCamera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCamera2.stop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_swich:

                break;
            default:
                break;
        }

    }
}
