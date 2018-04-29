package com.audition.huiimagiccamera.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.audition.huiimagiccamera.BaseActivity;
import com.audition.huiimagiccamera.MainActivity;
import com.audition.huiimagiccamera.R;

/**
 * @author tanzhuohui
 */
public class FlashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }
}
