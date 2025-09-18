LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := tools
LOCAL_SRC_FILES := interfaces.c functions.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
