package com.audition.huiimagiccamera.source;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.source
 * 创建日期： 2018/3/4
 * 描  述：  数据源接口
 */

public interface SourceInterface {
    void init();
    void start();
    void stop();
    void destory();
}
