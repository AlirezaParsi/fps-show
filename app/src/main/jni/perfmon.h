#ifndef PERFMON_PERFMON_H
#define PERFMON_PERFMON_H

#include <jni.h>
#include <stddef.h>

#define DEFAULT_PATH_SIZE 60
#define UNSUPPORTED (-1)
#define NULLTEMP (-233)

int read_file_int(const char *from, int *to);
int read_file_str(const char *from, char *to);
int read_process_int(const char *cmd, int *result);
int read_process_str(const char *cmd, char *result);
int find_fps_path_drm(char *fps_path, size_t size);

#endif //PERFMON_PERFMON_H
