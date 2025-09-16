#ifndef PERFMON_PERFMON_H
#define PERFMON_PERFMON_H

#define DEFAULT_PATH_SIZE 60
#define UNSUPPORTED (-1)

int read_file_int(const char *from, int *to);
int read_file_str(const char *from, char *to);
int read_process_int(const char *cmd, int *result);
int read_process_str(const char *cmd, char *result);

#endif //PERFMON_PERFMON_H