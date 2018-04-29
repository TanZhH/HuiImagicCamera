package com.audition.huiimagiccamera.source;

import android.view.TextureView;

/**
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.source
 * 创建日期： 2018/3/4
 * 描  述：  数据源接口
 * @author tanzhuohui
 */

public interface SourceInterface {
    /**
     * 初始化数据源
     */
    void init(TextureView textureView);
    void start();
    void stop();
    void destory();
}
