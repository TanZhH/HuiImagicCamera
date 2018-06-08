//
// Created by TANHUIHUI on 2018/4/27.
//

// jni头文件
#include <jni.h>

#include <cassert>
#include <cstdlib>
#include <iostream>

#include <Android/log.h>
#include <cstring>

#define TAG "result" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)

using namespace std;

/**
* 将Java层的String字符串转化成JNI能用的Char*数组
*/
char *jstringTostring(JNIEnv *env, jstring jstring1) {
    char *rtn = NULL;
    jclass classtring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(classtring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstring1, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

/**
 * 保存Yuv数据
 */
extern "C"
JNIEXPORT void
JNICALL
DumpYuvToFile(JNIEnv *env, jclass jclass1, jint width, jint height, jbyteArray yuvdata,
              jstring path) {
    char *_path = jstringTostring(env, path);
    FILE *file = fopen(_path, "wb");
    if (file != NULL) {
        jbyte *data = NULL;
        data = env->GetByteArrayElements(yuvdata, false);
        fwrite(data, (size_t) (width * height * 3 / 2), 1, file);
        fclose(file);
    }
}

void face2(JNIEnv *env, jclass jclass1 , jstring path){


}


/**
* 方法对应表
*/
static JNINativeMethod gMethods[] = {
        {"DumpYuvToFile" , "(II[BLjava/lang/String;)V" ,  (void *) &DumpYuvToFile},
};

//注册相应的类以及方法
jint registerNativeMeth(JNIEnv *env){
    jclass cl=env->FindClass("com/audition/huiimagiccamera/natives/OpencvNatives");
    if((env->RegisterNatives(cl,gMethods,sizeof(gMethods)/sizeof(gMethods[0])))<0){
        return -1;
    }
    return 0;
}
//实现jni_onload 动态注册方法
jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    if (registerNativeMeth(env) != JNI_OK) {//注册方法
        return -1;
    }
    return JNI_VERSION_1_4;//必须返回这个值
}
