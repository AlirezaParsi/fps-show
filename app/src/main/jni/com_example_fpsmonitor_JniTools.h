#include <jni.h>
#ifndef _Included_com_example_fpsmonitor_JniTools
#define _Included_com_example_fpsmonitor_JniTools
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_example_fpsmonitor_JniTools_getCpuNum(JNIEnv *, jclass);
JNIEXPORT jstring JNICALL Java_com_example_fpsmonitor_JniTools_getFps(JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif