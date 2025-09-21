#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include "perfmon.h"
#include <android/log.h>

#define LOG_TAG "FPSMonitor"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// تابع برای خواندن از فایل
char* read_from_file(const char* path) {
    FILE* file = fopen(path, "r");
    if (!file) {
        LOGE("Cannot open file: %s", path);
        return NULL;
    }

    char* buffer = malloc(256);
    if (!buffer) {
        fclose(file);
        return NULL;
    }

    if (fgets(buffer, 255, file) == NULL) {
        free(buffer);
        fclose(file);
        return NULL;
    }

    fclose(file);
    buffer[strcspn(buffer, "\n")] = 0;
    return buffer;
}

// تابع اصلی برای گرفتن FPS
JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getFps(JNIEnv *env, jclass clazz) {
    // اول مدیاتک رو چک کن
    char* fps_value = read_from_file("/sys/kernel/fpsgo/fstb/fstb_fps_list");
    if (fps_value != NULL) {
        char* token = strtok(fps_value, " ");
        token = strtok(NULL, " ");
        if (token != NULL) {
            jstring result = (*env)->NewStringUTF(env, token);
            free(fps_value);
            return result;
        }
        free(fps_value);
    }

    // سپس کوالکام رو چک کن
    fps_value = read_from_file("/sys/class/kgsl/kgsl-3d0/gpuclk");
    if (fps_value != NULL) {
        long gpu_freq = atol(fps_value);
        char buffer[20];
        snprintf(buffer, sizeof(buffer), "%ld", gpu_freq / 1000000);
        jstring result = (*env)->NewStringUTF(env, buffer);
        free(fps_value);
        return result;
    }

    return (*env)->NewStringUTF(env, "N/A");
}

// تابع برای گرفتن تعداد CPU
JNIEXPORT jint JNICALL Java_com_example_fpsmonitor_JniTools_getCpuNum(JNIEnv *env, jclass clazz) {
    return sysconf(_SC_NPROCESSORS_CONF);
}

// تابع برای گرفتن مدل دستگاه
JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getDeviceModel(JNIEnv *env, jclass clazz) {
    char model[100];
    sprintf(model, "%s %s", "Device", "Model");
    return (*env)->NewStringUTF(env, model);
}
