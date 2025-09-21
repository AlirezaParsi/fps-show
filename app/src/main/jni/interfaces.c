#include <jni.h>
#include "com_example_fpsmonitor_JniTools.h"

JNIEXPORT jint JNICALL Java_com_example_fpsmonitor_JniTools_getCpuNum(JNIEnv *env, jclass clazz) {
    return sysconf(_SC_NPROCESSORS_CONF);
}

JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getFps(JNIEnv *env, jclass clazz) {
    // پیاده‌سازی در functions.c
    return (*env)->NewStringUTF(env, "N/A");
}

// توابع اضافی اگر نیاز بود
JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getGpuUsage(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "N/A");
}

JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getCpuTemperature(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "N/A");
}

JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getDeviceModel(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "N/A");
}
