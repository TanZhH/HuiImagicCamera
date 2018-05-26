package com.audition.huiimagiccamera;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.audition.huiimagiccamera.adapter.FilterAdapter;
import com.audition.huiimagiccamera.source.Camera2;
import com.audition.huiimagiccamera.source.HuiCamera;
import com.audition.huiimagiccamera.utils.BeautifulImp;
import com.audition.huiimagiccamera.utils.CameraTools;
import com.audition.huiimagiccamera.utils.FilterTypeHelper;
import com.audition.huiimagiccamera.utils.PhotoTools;
import com.audition.huiimagiccamera.view.BaseActivity;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.minterface.InterActivityParms;
import com.seu.magicfilter.present.Present;
import com.seu.magicfilter.widget.MagicCameraView;

import java.io.File;
import java.io.FileNotFoundException;

import me.kareluo.imaging.IMGEditActivity;
import me.kareluo.imaging.myinterface.BeautifulInterface;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.MainActivity
 * 创建日期： 2018/3/4
 * 描  述：  MainActivity
 *
 * @author tanzhuohui
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,
        InterActivityParms,
        SeekBar.OnSeekBarChangeListener,
        FilterAdapter.onFilterChangListener {
    private TextureView mTextureView;
    private Camera2 mCamera2;
    private HuiCamera huiCamera;
    private MagicCameraView mCameraView;
    private MagicEngine magicEngine;
    private ImageView mSwichImage;
    private Present mPresent;
    private ImageView mBeatiful;
    private ImageView mFiler;
    private PopupWindow popupWindow;
    private SeekBar mSeekBar;
    private LinearLayout mFilterLayout;
    private RecyclerView mFilterListView;
    private LinearLayout mOption;
    private FilterAdapter mFilterAdapter;
    private ImageView shutter;
    private ImageView takePhoto;
    private static final int REQUEST_CODE_PICK_IMAGE = 0x03;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉titlebar-全屏模式
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //细节supportRequestWindowFeature一定要在setContentView之前设置
        setContentView(R.layout.activity_main);
        //屏幕常亮
        CameraTools.keepScreenLongLight(this, true);
        //沉浸式状态栏
        CameraTools.fullScreen(this);
        initView();
        takeFirstPhoto();
        init();
        Log.e("tzh", "onCreate:  create");
    }

    private void takeFirstPhoto() {
        String columns[] = new String[]{
                MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME
        };
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        int photoIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToLast()) {
            String photoPath = cursor.getString(photoIndex);
            Bitmap photo = decodeBitmap(photoPath);
            takePhoto.setImageBitmap(photo);
        }
        cursor.close();
    }

    /**
     * 从path中获取图片信息
     *
     * @param path
     * @return
     */
    private Bitmap decodeBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        //获取尺寸信息
        Bitmap bmp = BitmapFactory.decodeFile(path, op);
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / 150);
        int hRatio = (int) Math.ceil(op.outHeight / 150);
        //如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }

    private Bitmap decodeBitmap(File file) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        //获取尺寸信息
        Bitmap bmp = BitmapFactory.decodeFile(file.getPath(), op);
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / 150);
        int hRatio = (int) Math.ceil(op.outHeight / 150);
        //如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(file.getPath(), op);
        return bmp;
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
        mPresent = new Present(this, magicEngine, mCameraView);
        mCameraView.setmPresent(mPresent);
        mFilterAdapter = new FilterAdapter(FilterTypeHelper.types, this);
        mFilterAdapter.setOnFilterChangListener(this);
        mFilterListView.setAdapter(mFilterAdapter);
    }

    private void initView() {
//        mTextureView = (TextureView) findViewById(R.id.tv_surface);
        mFilterLayout = (LinearLayout) findViewById(R.id.layout_filter);
        mCameraView = (MagicCameraView) findViewById(R.id.glsurfaceview_camera);
        mBeatiful = (ImageView) findViewById(R.id.btn_camera_beauty);
        mSwichImage = (ImageView) findViewById(R.id.iv_swich);
        mFiler = (ImageView) findViewById(R.id.btn_camera_filter);
        mOption = (LinearLayout) findViewById(R.id.ll_option);
        mFiler.setOnClickListener(this);
        mSwichImage.setOnClickListener(this);
        mBeatiful.setOnClickListener(this);
        findViewById(R.id.btn_camera_closefilter).setOnClickListener(this);
        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);
        shutter = (ImageView) findViewById(R.id.btn_camera_shutter);
        shutter.setOnClickListener(this);

        takePhoto = (ImageView) findViewById(R.id.btn_camera_photo);
        takePhoto.setOnClickListener(this);
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
        Log.e("tzh", "onResume:  ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresent.releaseCamera();
//        mCamera2.stop();
        Log.e("tzh", "onPause:  ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("tzh", "onDestroy:  ");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_swich:
                mPresent.cameraSwitch();
                break;
            case R.id.btn_camera_beauty:
                showBeatiful();
                break;
            case R.id.btn_camera_filter:
                showFiler();
                break;
            case R.id.btn_camera_closefilter:
                hideFilters();
                break;
            case R.id.btn_camera_shutter:
                mPresent.takePhoto();
                break;
            case R.id.btn_camera_photo:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent , REQUEST_CODE_PICK_IMAGE);
                break;
            default:
                break;
        }
    }

    private void hideFilters() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0, mFilterLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFilterLayout.setVisibility(View.GONE);
                mOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mFilterLayout.setVisibility(View.GONE);
                mOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mFilterLayout.setVisibility(View.GONE);
                mOption.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    private void showFiler() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFilterLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mOption.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mOption.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    @Override
    public void setSwicthSrc(final boolean isfilp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isfilp) {
                    mSwichImage.setBackground(getDrawable(R.mipmap.btn_change_camera_pressed));
                } else {
                    mSwichImage.setBackground(getDrawable(R.mipmap.btn_change_camera_normal));
                }
            }
        });
    }

    private void showBeatiful() {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.popuplayout, null);
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //设置setBackgroundDrawablec才能使用setOutsideTouchable
            ColorDrawable cd = new ColorDrawable();
            popupWindow.setBackgroundDrawable(cd);
            mSeekBar = (SeekBar) contentView.findViewById(R.id.sb_beatiful);
            mSeekBar.setOnSeekBarChangeListener(this);
            popupWindow.setOutsideTouchable(true);
        }
        mSeekBar.setSecondaryProgress(100);
        popupWindow.showAtLocation(findViewById(R.id.rl_parents), 0, -100, Gravity.CENTER);
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
    public void setSeekBarNum(int num) {
        mSeekBar.setProgress(num);
    }

    @Override
    public void onFilterChanged(MagicFilterType filterType) {
        mPresent.setFilter(filterType);
    }

    @Override
    public void setPhoto(File file) {
        Bitmap bitmap = decodeBitmap(file);
        takePhoto.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_PICK_IMAGE:
                if(data != null){
                    Uri uri = data.getData();
                    if(uri != null){
                        Intent intent = new Intent(MainActivity.this , IMGEditActivity.class);
                        String path = PhotoTools.getPath(this , uri);
                        Uri uri1 = Uri.fromFile(new File(path));
                        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, uri1);
                        BeautifulInterface beautifulInterface = new BeautifulImp();
                        IMGEditActivity.setBeautifulInterface(beautifulInterface);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }
    }
}
