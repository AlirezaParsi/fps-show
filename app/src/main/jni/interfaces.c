#include "com_example_fpsmonitor_JniTools.h"
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "perfmon.h"

JNIEXPORT jint JNICALL Java_com_example_fpsmonitor_JniTools_getCpuNum(JNIEnv *env, jclass jclass1) {
    int cpunum;
    if (read_process_int("ls /sys/devices/system/cpu | grep -o \"cpu[0-9]*$\" | wc -l", &cpunum))
        return 0;
    return cpunum;
}

JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getFps(JNIEnv *env, jclass jclass1) {
    char fps[32] = "";  // بافر بزرگ
    char fps_path[256] = "";
    float fps_value = 0.0;

    // جستجوی پویا تو /sys/class/drm (مثل GameSpace)
    if (find_fps_path_drm(fps_path, sizeof(fps_path)) == 0 && strlen(fps_path) > 0) {
        if (read_file_str(fps_path, fps) == 0) goto parse_fps;
    }

    // فال‌بک به مسیرهای Qualcomm
    snprintf(fps_path, sizeof(fps_path), "/sys/devices/platform/soc/ae00000.qcom,mdss_mdp/drm/card0/sde-crtc-0/measured_fps");
    if (read_file_str(fps_path, fps) == 0) goto parse_fps;
    snprintf(fps_path, sizeof(fps_path), "/sys/devices/platform/soc/ae00000.qcom,mdss_mdp/drm/card0/sde-crtc-1/measured_fps");
    if (read_file_str(fps_path, fps) == 0) goto parse_fps;

    return (*env)->NewStringUTF(env, "Error: No FPS data");

parse_fps:
    if (sscanf(fps, "fps: %f", &fps_value) == 1 || sscanf(fps, "%f", &fps_value) == 1) {
        char result[32];
        snprintf(result, sizeof(result), "%.1f", fps_value);
        return (*env)->NewStringUTF(env, result);
    }
    return (*env)->NewStringUTF(env, "Error: No FPS data");
}

int find_fps_path_drm(char *fps_path, size_t size) {
    char cmd[256];
    snprintf(cmd, sizeof(cmd), "su -c find /sys/class/drm -type f -name '*measured_fps*' 2>/dev/null | head -n 1");
    FILE *process = popen(cmd, "r");
    if (process == NULL) return UNSUPPORTED;
    if (fgets(fps_path, size, process) == NULL) {
        pclose(process);
        return UNSUPPORTED;
    }
    fps_path[strcspn(fps_path, "\n")] = 0; // حذف \n
    pclose(process);
    return 0;
}
