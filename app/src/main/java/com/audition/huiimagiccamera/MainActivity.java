package com.audition.huiimagiccamera;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.audition.huiimagiccamera.natives.OpencvNatives;
import com.audition.huiimagiccamera.source.Camera2;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.MainActivity
 * 创建日期： 2018/3/4
 * 描  述：  MainActivity
 * @author  tanzhuohui
 */
public class MainActivity extends BaseActivity {
    private TextureView mTextureView;
    private Camera2 mCamera2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉titlebar-全屏模式
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //细节supportRequestWindowFeature一定要在setContentView之前设置
        setContentView(R.layout.activity_main);
        fullScreen();
        mTextureView = (TextureView) findViewById(R.id.tv_surface);
        mCamera2 = new Camera2(this);
        mCamera2.init(mTextureView);
        mCamera2.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera2.stop();
    }

    private void fullScreen(){
        Activity activity = this;
        int statusColor = Color.parseColor("#008000");
        //针对版本5.x以上的即LOLLIPOP以上的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //设置透明状态栏,这样才能让 ContentView 向上
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);
            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View .
                // 使其不为系统 View 预留空间.不预留空间的话 状态栏就会覆盖布局顶部
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }
        }
    }
}
