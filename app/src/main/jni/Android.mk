LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := tools
LOCAL_SRC_FILES := functions.c interfaces.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
