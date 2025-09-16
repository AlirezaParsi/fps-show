#include "perfmon.h"
#include <stdio.h>
#include <string.h>

int read_file_int(const char *path, int *result) {
    char cmd[256];
    sprintf(cmd, "su -c cat %s", path);
    FILE *file = popen(cmd, "r");
    if (file == NULL)
        return UNSUPPORTED;
    fscanf(file, "%d", result);
    pclose(file);
    return 0;
}

int read_file_str(const char *path, char *result) {
    char cmd[256];
    sprintf(cmd, "su -c cat %s", path);
    FILE *file = popen(cmd, "r");
    if (file == NULL)
        return UNSUPPORTED;
    if (fgets(result, 256, file) == NULL) {
        pclose(file);
        return UNSUPPORTED;
    }
    result[strcspn(result, "\n")] = 0;
    pclose(file);
    return 0;
}

int read_process_int(const char *cmd, int *result) {
    FILE *process = popen(cmd, "r");
    if (process == NULL)
        return UNSUPPORTED;
    fscanf(process, "%d", result);
    pclose(process);
    return 0;
}

int read_process_str(const char *cmd, char *result) {
    FILE *process = popen(cmd, "r");
    if (process == NULL)
        return UNSUPPORTED;
    if (fgets(result, 256, process) == NULL) {
        pclose(process);
        return UNSUPPORTED;
    }
    result[strcspn(result, "\n")] = 0;
    pclose(process);
    return 0;
}