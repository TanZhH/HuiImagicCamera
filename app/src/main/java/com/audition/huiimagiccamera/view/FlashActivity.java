package com.audition.huiimagiccamera.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.Toast;

import com.audition.huiimagiccamera.MainActivity;
import com.audition.huiimagiccamera.R;
import com.audition.huiimagiccamera.utils.CameraTools;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.view
 * 创建日期： 2018/5/10
 * 描  述：
 * @author TANHUIHUI
 */

public class FlashActivity extends BaseActivity {
    private static final int GRANTED = 0x03;
    private static final int OK = 0x02;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GRANTED:
                    System.exit(0);
                    break;
                case OK:
                    Intent intent = new Intent(FlashActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉titlebar-全屏模式
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //细节supportRequestWindowFeature一定要在setContentView之前设置
        setContentView(R.layout.activity_flash);
        //沉浸式状态栏
        CameraTools.fullScreen(this);
        myCheckPermission();
    }

    private void myCheckPermission() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean b = CameraTools.requestPower(this);
            if(b){
                Message message = new Message();
                message.what = OK;
                handler.sendMessageDelayed(message , 2000);
            }
        }else {
            Message message = new Message();
            message.what = OK;
            handler.sendMessageDelayed(message , 2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraTools.PERMISSION){
            boolean b = true;
            for (int grantResult : grantResults) {
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    b = false;
                    Toast.makeText(this , "无使用权限，程序即将退出！", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.what = GRANTED;
                    handler.sendMessageDelayed(message , 2000);
                    break;
                }
            }
            if(b){
                Message message = new Message();
                message.what = OK;
                handler.sendMessageDelayed(message , 2000);
            }
        }
    }
}
