#include "com_example_fpsmonitor_JniTools.h"
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "perfmon.h"

JNIEXPORT jint JNICALL Java_com_example_fpsmonitor_JniTools_getCpuNum(JNIEnv *env, jclass jclass1) {
    int cpunum;
    if (read_process_int("su -c ls /sys/devices/system/cpu | grep -o 'cpu[0-9]*$' | wc -l", &cpunum))
        return 0;
    return cpunum;
}

JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getFps(JNIEnv *env, jclass jclass1) {
    char fps[128] = "";
    if (!read_file_str("/sys/devices/platform/soc/ae00000.qcom,mdss_mdp/drm/card0/sde-crtc-0/measured_fps", fps)) {
        float fps_value;
        if (sscanf(fps, "fps: %f", &fps_value) == 1) {
            snprintf(fps, sizeof(fps), "%.1f", fps_value);
            return (*env)->NewStringUTF(env, fps);
        }
    }

    char cmd[256] = "su -c cat /sys/devices/platform/soc/ae00000.qcom,mdss_mdp/drm/card0/sde-crtc-0/measured_fps | awk '{print $2}'";
    if (!read_process_str(cmd, fps)) {
        float fps_value;
        if (sscanf(fps, "%f", &fps_value) == 1) {
            snprintf(fps, sizeof(fps), "%.1f", fps_value);
            return (*env)->NewStringUTF(env, fps);
        }
    }

    return (*env)->NewStringUTF(env, "Error: No FPS data");
}