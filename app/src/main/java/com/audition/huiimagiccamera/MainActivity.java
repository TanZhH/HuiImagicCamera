package com.audition.huiimagiccamera;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.audition.huiimagiccamera.natives.OpencvNatives;
import com.audition.huiimagiccamera.source.Camera2;
import com.audition.huiimagiccamera.source.HuiCamera;
import com.audition.huiimagiccamera.utils.CameraTools;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.minterface.InterActivityParms;
import com.seu.magicfilter.present.Present;
import com.seu.magicfilter.widget.MagicCameraView;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.MainActivity
 * 创建日期： 2018/3/4
 * 描  述：  MainActivity
 * @author  tanzhuohui
 */
public class MainActivity extends BaseActivity implements View.OnClickListener , InterActivityParms, SeekBar.OnSeekBarChangeListener {
    private TextureView mTextureView;
    private Camera2 mCamera2;
    private HuiCamera huiCamera;
    private MagicCameraView mCameraView;
    private MagicEngine magicEngine;
    private ImageView mSwichImage;
    private Present mPresent;
    private ImageView mBeatiful;
    private PopupWindow popupWindow;
    private SeekBar mSeekBar;

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
        mPresent = new Present(this , magicEngine , mCameraView);
        mCameraView.setmPresent(mPresent);
    }

    private void initView() {
//        mTextureView = (TextureView) findViewById(R.id.tv_surface);
        mCameraView = (MagicCameraView) findViewById(R.id.glsurfaceview_camera);
        mBeatiful = (ImageView) findViewById(R.id.btn_camera_beauty);
        mSwichImage = (ImageView) findViewById(R.id.iv_swich);
        mSwichImage.setOnClickListener(this);
        mBeatiful.setOnClickListener(this);
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
        mPresent.releaseCamera();
//        mCamera2.stop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_swich:
                mPresent.cameraSwitch();
                break;
            case R.id.btn_camera_beauty:
                showBeatiful();
                break;
            default:
                break;
        }
    }

    @Override
    public void setSwicthSrc(final boolean isfilp){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isfilp){
                    mSwichImage.setBackground(getDrawable(R.mipmap.btn_change_camera_pressed));
                }else {
                    mSwichImage.setBackground(getDrawable(R.mipmap.btn_change_camera_normal));
                }
            }
        });
    }

    @Override
    public void setBeatiful() {

    }

    private void showBeatiful(){
        if(popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.popuplayout, null);
            popupWindow = new PopupWindow(contentView , ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT , true);
            //设置setBackgroundDrawablec才能使用setOutsideTouchable
            ColorDrawable cd = new ColorDrawable();
            popupWindow.setBackgroundDrawable(cd);
            mSeekBar = (SeekBar) contentView.findViewById(R.id.sb_beatiful);
            mSeekBar.setOnSeekBarChangeListener(this);
            popupWindow.setOutsideTouchable(true);
        }
        mSeekBar.setSecondaryProgress(100);
        popupWindow.showAsDropDown(findViewById(R.id.ll_option), 0, 20, Gravity.CENTER);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mPresent.setBeatiful(progress);
        popupWindow.dismiss();
    }

    @Override
    public void setSeekBarNum(int num){
        mSeekBar.setProgress(num);
    }
}
