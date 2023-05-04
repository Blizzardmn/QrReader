#include <jni.h>
#include <sys/file.h>
#include <unistd.h>
#include <fcntl.h>
#include <android/log.h>
#include <wait.h>
#include <cstdlib>
#include <cstring>
#include <sys/ptrace.h>

using namespace std;

#define TAG        "JamesMvp"
#define LOGI(...)    __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...)    __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

void startProcess(JNIEnv *env, const char *process_head, const char *entry_class, const char *parcel,
             const char *processName,
             const char *publicSourceDir, const char *nativeLibraryDir);

void cpkg(JNIEnv *env, jobject context);

bool is_ok = true;

void sub_25c8()
{
    int iRet;
    iRet=ptrace(PTRACE_TRACEME,0,0,0);
}
void sub_25c9()
{
//    int pid;
//    FILE *fd;
//    const int MAX=1024;
//    char filename[MAX];
//    char line[MAX];
//    pid = getpid();
//    sprintf(filename, "/proc/%d/status", pid);// 读取proc/pid/status中的TracerPid
//    //while (true)
//    {
//        fd = fopen(filename, "r");
//        if(fd!=NULL){
//            while (fgets(line, MAX, fd)) {
//                if (strncmp(line, "TracerPid", 9) == 0) {
//                    int statue = atoi(&line[10]);
//                    fclose(fd);
//                    if (statue != 0) {
//                        //int ret = kill(pid, SIGKILL);
//                        LOGD("########## has debug");
//                        exit(0);
//                        return;
//                    }
//                    break;
//                }
//            }
//            //sleep(CHECK_TIME);
//        }
//    }
}
extern "C" JNIEXPORT jint JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_l(JNIEnv *env, jclass clazz, jstring path) {
    if (!is_ok)
        return 0;

    const char *a1 = env->GetStringUTFChars(path, nullptr);
    signed int v1; // r5
    int v4; // r0
    int result; // r0

    v1 = 0;
    if (a1) {
        v4 = open(a1, 0);
        if (v4 == -1)
            v4 = open(a1, 64, 256);
        if (v4 < 0 || (v1 = 1, flock(v4, 2)))
            v1 = 0;
    }
    result = v1;
    env->ReleaseStringUTFChars(path, a1);
    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_i(JNIEnv *env, jclass clazz, jobject context) {
    cpkg(env, context);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_s(JNIEnv *env, jclass clazz) {
    if (!is_ok)return 0;
    setsid();
    return 1;
}


int lockFile(const char *a1) {
    if (!is_ok)
        return 0;

    const char *v1; // r6
    signed int v2; // r5
    int v3; // r4
    signed int v4; // r5

    v1 = a1;
    v2 = 0;
    sub_25c9();
    v3 = open(a1, 0);
    if (v3 == -1)
        v3 = open(v1, 64, 256);
    if (v3 >= 0) {
        v4 = -1;
        while (flock(v3, 6) != -1) {
            flock(v3, 8);
            usleep(1000);
            if (++v4 >= 59999)
                exit(-1);
        }
        v2 = 1;
        if (flock(v3, 2))
            v2 = 0;
    }
    return v2;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_w(JNIEnv *env, jclass clazz, jstring path) {
    if (!is_ok)
        return 0;

    const char *a1 = env->GetStringUTFChars(path, nullptr);
    int v3;
    const char *v4;
    int result;

    v3 = 0;
    if (a1)
        v3 = lockFile(a1);
    result = v3;
    env->ReleaseStringUTFChars(path, a1);
    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_ahead(JNIEnv *env, jclass clazz, jstring var1, jstring var2,
                                              jstring var3, jstring var4, jstring var5,
                                              jstring var6) {
    if (!is_ok)
        return;

    const char *v1 = env->GetStringUTFChars(var1, nullptr);
    const char *v2 = env->GetStringUTFChars(var2, nullptr);
    const char *v3 = env->GetStringUTFChars(var3, nullptr);
    const char *v4 = env->GetStringUTFChars(var4, nullptr);
    const char *v5 = env->GetStringUTFChars(var5, nullptr);
    const char *v6 = env->GetStringUTFChars(var6, nullptr);

    startProcess(env, v1, v2, v3, v4, v5, v6);
    env->ReleaseStringUTFChars(var1, v1);
    env->ReleaseStringUTFChars(var2, v2);
    env->ReleaseStringUTFChars(var3, v3);
    env->ReleaseStringUTFChars(var4, v4);
    env->ReleaseStringUTFChars(var5, v5);
    env->ReleaseStringUTFChars(var6, v6);
}

void set_process_name(JNIEnv *env, jstring name) {
    jclass process = env->FindClass("android/os/Process");
    jmethodID setArgV0 = env->GetStaticMethodID(process, "setArgV0",
                                                "(Ljava/lang/String;)V");
    if(setArgV0 != nullptr){
        env->CallStaticVoidMethod(process, setArgV0, name);
    }
}

void create_file_if_not_exist(char *path) {
    FILE *fp = fopen(path, "ab+");
    if (fp) {
        fclose(fp);
    }
}

int lock_file(char *lock_file_path) {
//    LOGI("start try to lock file >> %s <<", lock_file_path);
    int lockFileDescriptor = open(lock_file_path, O_RDONLY);
    if (lockFileDescriptor == -1) {
        lockFileDescriptor = open(lock_file_path, O_CREAT, S_IRUSR);
    }
    int lockRet = flock(lockFileDescriptor, LOCK_EX);
    if (lockRet == -1) {
//        LOGI("lock file failed >> %s <<", lock_file_path);
        return 0;
    } else {
//        LOGI("lock file success  >> %s <<", lock_file_path);
        return 1;
    }
}

void notify_and_waitfor(char *observer_self_path, char *observer_daemon_path) {
    int observer_self_descriptor = open(observer_self_path, O_RDONLY);
    if (observer_self_descriptor == -1) {
        observer_self_descriptor = open(observer_self_path, O_CREAT, S_IRUSR | S_IWUSR);
    }
    int observer_daemon_descriptor = open(observer_daemon_path, O_RDONLY);
    while (observer_daemon_descriptor == -1) {
        usleep(1000);
        observer_daemon_descriptor = open(observer_daemon_path, O_RDONLY);
    }
    remove(observer_daemon_path);
//    LOGI("Watched >>>>OBSERVER<<<< has been ready...");
}

void java_callback(JNIEnv *env) {
    if (!is_ok)
        return;

    jclass cls = env->FindClass("com/reader/multiple/mvp/MvpNavObj");
    jmethodID cb_method = env->GetStaticMethodID(cls, "restartProcess", "()V");
    env->CallStaticVoidMethod(cls, cb_method);
}

void do_daemon(JNIEnv *env, jclass clazz, char *indicator_self_path, char *indicator_daemon_path,
               char *observer_self_path, char *observer_daemon_path) {
    int lock_status = 0;
    int try_time = 0;
    while (try_time < 3 && !(lock_status = lock_file(indicator_self_path))) {
        try_time++;
//        LOGI("Persistent lock myself failed and try again as %d times", try_time);
        usleep(10000);
    }
    if (!lock_status) {
//        LOGI("Persistent lock myself failed and exit");
        return;
    }

    notify_and_waitfor(observer_self_path, observer_daemon_path);

    lock_status = lock_file(indicator_daemon_path);
    if (lock_status) {
//        LOGI("Watch >>>>DAEMON<<<<< Daed !!");
        remove(observer_self_path);// it`s important ! to prevent from deadlock

        java_callback(env);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_forkChild(JNIEnv *env, jclass clazz, jobject context,
                                                  jstring fork_name, jstring selfForkLockFile,
                                                  jstring selfForkWaitFile,
                                                  jstring selfForkIndicatorFile,
                                                  jstring selfForkWaitIndicatorFile) {
    cpkg(env, context);
    if (!is_ok)return;

    char *name = (char *) env->GetStringUTFChars(fork_name, nullptr);
    char *self_fork_lock_file = (char *) env->GetStringUTFChars(selfForkLockFile, nullptr);
    char *self_fork_wait_file = (char *) env->GetStringUTFChars(selfForkWaitFile, nullptr);
    char *self_fork_indicator_file = (char *) env->GetStringUTFChars(selfForkIndicatorFile,
                                                                     nullptr);
    char *self_fork_wait_indicator_File = (char *) env->GetStringUTFChars(selfForkWaitIndicatorFile,
                                                                          nullptr);

    pid_t pid = fork();
    if (pid < 0) {
//        LOGI("创建失败");
        exit(-1);
    } else if (pid == 0) {
        if ((pid = fork()) < 0) {
//            LOGI("二度fork失败\n");
            exit(-1);
        } else if (pid > 0) {
//            LOGD("托孤\n");
            exit(0);
        }

        setsid();
        set_process_name(env, fork_name);

        const int MAX_PATH = 256;
        char indicator_self_path_child[MAX_PATH];
        char indicator_daemon_path_child[MAX_PATH];
        char observer_self_path_child[MAX_PATH];
        char observer_daemon_path_child[MAX_PATH];

        strcpy(indicator_self_path_child, self_fork_lock_file);
        strcpy(indicator_daemon_path_child, self_fork_wait_file);
        strcpy(observer_self_path_child, self_fork_indicator_file);
        strcpy(observer_daemon_path_child, self_fork_wait_indicator_File);

        create_file_if_not_exist(indicator_self_path_child);
        create_file_if_not_exist(indicator_daemon_path_child);

        do_daemon(env, clazz, indicator_self_path_child, indicator_daemon_path_child,
                  observer_self_path_child, observer_daemon_path_child);
        return;
    }

    if (waitpid(pid, NULL, 0) != pid)
        printf("waitpid error\n");

    do_daemon(env, clazz, self_fork_lock_file, self_fork_wait_file, self_fork_indicator_file,
              self_fork_wait_indicator_File);
    env->ReleaseStringUTFChars(fork_name, name);
    env->ReleaseStringUTFChars(selfForkLockFile, self_fork_lock_file);
    env->ReleaseStringUTFChars(selfForkWaitFile, self_fork_wait_file);
    env->ReleaseStringUTFChars(selfForkIndicatorFile, self_fork_indicator_file);
    env->ReleaseStringUTFChars(selfForkWaitIndicatorFile, self_fork_wait_indicator_File);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_reader_multiple_mvp_MvpNavObj_p(JNIEnv *env, jclass clazz, jobject intent) {
    if (!is_ok)
        return;

    /**
    * 找到要调用的类
    */
    jclass cls_util = env->FindClass("com/reader/multiple/mvp/RomHelper");   //注意，这里的使用的斜杠而不是点
    if (cls_util == nullptr) {
        return;
    }
    /**
    * 获取静态方法操作的对象 ，参数分别是 jclass,方法名称，方法签名
    */
    jmethodID isVivoMethod = env->GetStaticMethodID(cls_util, "isVivo", "()Z");
    if (isVivoMethod == nullptr) {
        return;
    }
    /**
    * 调用方法，参数是 jclass，jmethodID，传递的参数
    */
    jboolean isVivo = env->CallStaticBooleanMethod(cls_util, isVivoMethod);
    if (isVivo) {
        jclass classIntent = (env)->FindClass("android/content/Intent");
        jmethodID jmID = (env)->GetMethodID(classIntent, "setIsVivoWidget", "(Z)V");
        (env)->CallVoidMethod(intent, jmID, true);
        return;
    }
    jmethodID isMiuiMethod = env->GetStaticMethodID(cls_util, "isMiui", "()Z");
    if (isMiuiMethod == nullptr) {
        return;
    }
    jboolean isMiui = env->CallStaticBooleanMethod(cls_util, isMiuiMethod);
    if (isMiui) {
        jclass classIntent = (env)->FindClass("android/content/Intent");
        jmethodID jmID = (env)->GetMethodID(classIntent, "setMiuiFlags", "(I)Landroid/content/Intent;");
        (env)->CallObjectMethod(intent, jmID, 0x2);
        return;
    }
}

char* strcats(char *dest,const char *src){
    char *result = (char*)malloc(strlen(dest)+strlen(src));
    strcpy(result,dest);
    strcat(result,src);
    return result;
}

void startProcess(JNIEnv *env, const char *process_head, const char *entry_class, const char *parcel,
             const char *processName,
             const char *publicSourceDir, const char *nativeLibraryDir) {
    const char *params1 = "/";
    char *var2 = "export CLASSPATH=$CLASSPATH:";
    char* param2 = strcats(var2, publicSourceDir);
    char *var3 = "export _LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:";
    char* param3 = strcats(var3, nativeLibraryDir);
    char *var4 = "export LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:";
    char* param4 = strcats(var4, nativeLibraryDir);
    char *var5="";
    char* param5= strcats(var5, process_head);
    param5 = strcats(param5, " / ");
    param5 = strcats(param5, entry_class);
    param5 = strcats(param5, " ");
    param5 = strcats(param5, parcel);
    param5 = strcats(param5, "--application --nice-name=");
    param5 = strcats(param5, processName);
    param5 = strcats(param5, " --daemon &");

    jclass cls = env->FindClass("com/reader/multiple/mvp/MvpNavObj");
    jmethodID sp_method = env->GetStaticMethodID(cls, "sp",
                                                 "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    env->CallStaticVoidMethod(cls, sp_method, env->NewStringUTF(params1), env->NewStringUTF(param2),
                              env->NewStringUTF(param3),
                              env->NewStringUTF(param4), env->NewStringUTF(param5));
}

/*list<char *> getFilter() {
    list<char *> pkg_list;
    pkg_list.push_back("cn.qn.wifi.radar");
    pkg_list.push_back("cn.qn.greenclean.phone");
    pkg_list.push_back("cn.qn.speed.wifi");
    pkg_list.push_back("greenclean.clean.space.memory");
    pkg_list.push_back("cn.qn.wifi.free");
    pkg_list.push_back("com.android.qn.zzswifi");
    return pkg_list;
}*/

void cpkg(JNIEnv *env, jobject context) {
    jclass objClazz = env->GetObjectClass(context);
    jmethodID get_package_name = env->GetMethodID(objClazz, "getPackageName",
                                                  "()Ljava/lang/String;");
    auto pkg_name = (jstring) env->CallObjectMethod(context, get_package_name);

    const char *pkg = env->GetStringUTFChars(pkg_name, nullptr);
    sub_25c8();
    //LOGI("pkgName-> %s",pkg);
    const char *pkg1 = "com.qr.myqr";
    const char *pkg2 = "com.reader.scan.qr";

    is_ok = (strcmp(pkg, pkg1) == 0) ||
            (strcmp(pkg, pkg2) == 0) ;
    env->ReleaseStringUTFChars(pkg_name, pkg);
}

jmethodID cb_method3=NULL;
jobject cn=NULL;

void initTB(JNIEnv *env,jobject context){
    jclass objClazz = env->GetObjectClass(context);
     cb_method3 = env->GetMethodID(objClazz,"startInstrumentation","(Landroid/content/ComponentName;Ljava/lang/String;Landroid/os/Bundle;)Z");
    jclass   v17 = env->FindClass("android/content/ComponentName");
    jmethodID v18 = env->GetMethodID(v17,"<init>","(Landroid/content/Context;Ljava/lang/String;)V");
    const char *params2 = "com.reader.multiple.vb.ViInstrumentation";//"com.opp.io.K";//"com.miqt.demo.K";//"com.vi.kachem.ViInstrumentation";
    cn=env->NewObject( v17, v18, context, env->NewStringUTF(params2));
}
void TB_java_callback(JNIEnv *env,jobject context) {
    //LOGI("TB_java_callback start");
    env->CallBooleanMethod(
            context,
            cb_method3,
            cn,
            0,
            0);
    //LOGI("TB_java_callback end");

}
void TB_daemon(JNIEnv *env, jclass clazz, char *indicator_self_path, char *indicator_daemon_path,
               char *observer_self_path, char *observer_daemon_path,jobject context) {
    int lock_status = 0;
    int try_time = 0;
    while (try_time < 3 && !(lock_status = lock_file(indicator_self_path))) {
        try_time++;
//        LOGI("Persistent lock myself failed and try again as %d times", try_time);
        usleep(10000);
    }
    if (!lock_status) {
        LOGI("Persistent lock myself failed and exit");
        return;
    }

    notify_and_waitfor(observer_self_path, observer_daemon_path);

    lock_status = lock_file(indicator_daemon_path);
    if (lock_status) {
        remove(observer_self_path);// it`s important ! to prevent from deadlock

        TB_java_callback(env,context);
        exit(0);
    }
}

void TB_set_process_name(JNIEnv *env) {
    jclass process = env->FindClass("android/os/Process");
    jmethodID setArgV0 = env->GetStaticMethodID(process, "setArgV0",
                                                "(Ljava/lang/String;)V");
    jstring name = env->NewStringUTF("app");
    env->CallStaticVoidMethod(process, setArgV0, name);
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_mvp_MultiNavObj_TB(JNIEnv *env, jclass clazz, jobject context,
                                              jstring selfForkLockFile,
                                              jstring selfForkWaitFile,
                                              jstring selfForkIndicatorFile,
                                              jstring selfForkWaitIndicatorFile) {
    cpkg(env, context);
    if (!is_ok)return;
    char *name = "app";//(char *) env->GetStringUTFChars(fork_name, nullptr);
    char *self_fork_lock_file = (char *) env->GetStringUTFChars(selfForkLockFile, nullptr);
    char *self_fork_wait_file = (char *) env->GetStringUTFChars(selfForkWaitFile, nullptr);
    char *self_fork_indicator_file = (char *) env->GetStringUTFChars(selfForkIndicatorFile,
                                                                     nullptr);
    char *self_fork_wait_indicator_File = (char *) env->GetStringUTFChars(selfForkWaitIndicatorFile,
                                                                          nullptr);

    pid_t pid = fork();
    if (pid < 0) {
//        LOGI("创建失败");
        exit(-1);
    } else if (pid == 0) {
        if ((pid = fork()) < 0) {
//            LOGI("二度fork失败\n");
            exit(-1);
        } else if (pid > 0) {
//            LOGD("托孤\n");
            exit(0);
        }

        setsid();
        TB_set_process_name(env);

        const int MAX_PATH = 256;
        char indicator_self_path_child[MAX_PATH];
        char indicator_daemon_path_child[MAX_PATH];
        char observer_self_path_child[MAX_PATH];
        char observer_daemon_path_child[MAX_PATH];

        strcpy(indicator_self_path_child, self_fork_lock_file);
        strcat(indicator_self_path_child, "c");
        strcpy(indicator_daemon_path_child, self_fork_wait_file);
        strcat(indicator_daemon_path_child, "c");
        strcpy(observer_self_path_child, self_fork_indicator_file);
        strcat(observer_self_path_child, "c");
        strcpy(observer_daemon_path_child, self_fork_wait_indicator_File);
        strcat(observer_daemon_path_child, "c");

        create_file_if_not_exist(indicator_self_path_child);
        create_file_if_not_exist(indicator_daemon_path_child);
        initTB(env,context);
        TB_daemon(env, clazz, indicator_self_path_child, indicator_daemon_path_child,
                  observer_self_path_child, observer_daemon_path_child,context);
        return;
    }

    if (waitpid(pid, NULL, 0) != pid)
        printf("waitpid error\n");

    TB_daemon(env, clazz, self_fork_lock_file, self_fork_wait_file, self_fork_indicator_file,
              self_fork_wait_indicator_File,context);
    env->ReleaseStringUTFChars(selfForkLockFile, self_fork_lock_file);
    env->ReleaseStringUTFChars(selfForkWaitFile, self_fork_wait_file);
    env->ReleaseStringUTFChars(selfForkIndicatorFile, self_fork_indicator_file);
    env->ReleaseStringUTFChars(selfForkWaitIndicatorFile, self_fork_wait_indicator_File);
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_vb_MvpFbObj_sh(JNIEnv *env, jclass thiz, jobject context, jstring clazzName) {
    cpkg(env, context);
    if (!is_ok)return;
    const char* class_name = env->GetStringUTFChars( clazzName, 0);
    jclass context_class = env->GetObjectClass( context);
    jmethodID get_package_manager = env->GetMethodID( context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject package_manager = env->CallObjectMethod(context, get_package_manager);

    jclass package_manager_class = env->GetObjectClass( package_manager);
    jmethodID set_component_enabled_setting = env->GetMethodID(package_manager_class, "setComponentEnabledSetting", "(Landroid/content/ComponentName;II)V");

    jobject component_name = env->NewObject(env->FindClass("android/content/ComponentName"), env->GetMethodID( env->FindClass("android/content/ComponentName"), "<init>", "(Landroid/content/Context;Ljava/lang/String;)V"), context, env->NewStringUTF(class_name));

    env->CallVoidMethod(package_manager, set_component_enabled_setting, component_name, 1, 1);

    env->ReleaseStringUTFChars( clazzName, class_name);
}

extern "C"  JNIEXPORT void JNICALL
Java_com_reader_multiple_vb_MvpFbObj_hi(JNIEnv* env, jclass thiz, jobject context, jstring clazzName) {
    cpkg(env, context);
    if (!is_ok)return;
    const char* class_name = env->GetStringUTFChars(clazzName, 0);
    jclass context_class = env->GetObjectClass(context);
    jmethodID get_package_manager = env->GetMethodID(context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject package_manager = env->CallObjectMethod(context, get_package_manager);

    jclass package_manager_class = env->GetObjectClass( package_manager);
    jmethodID set_component_enabled_setting = env->GetMethodID(package_manager_class, "setComponentEnabledSetting", "(Landroid/content/ComponentName;II)V");

    jobject component_name = env->NewObject( env->FindClass("android/content/ComponentName"), env->GetMethodID( env->FindClass( "android/content/ComponentName"), "<init>", "(Landroid/content/Context;Ljava/lang/String;)V"), context, env->NewStringUTF( class_name));

    env->CallVoidMethod(package_manager, set_component_enabled_setting, component_name, 2, 1);

    env->ReleaseStringUTFChars(clazzName, class_name);
}


extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_vb_MvpFbObj_cvd(JNIEnv* env, jclass thiz, jobject context) {
    cpkg(env, context);
    if (!is_ok)return;
    jclass contextClass = env->GetObjectClass(context);
    jmethodID getSystemService = env->GetMethodID(contextClass, "getSystemService", "(Ljava/lang/String;)Ljava/lang/Object;");
    //jfieldID displayServiceField = env->GetStaticFieldID(contextClass, "DISPLAY_SERVICE", "Ljava/lang/String;");
    //jstring displayService = (jstring)env->GetStaticObjectField(contextClass, displayServiceField);
    //jobject displayManager = env->CallObjectMethod(context, getSystemService, displayService);
    jobject displayManager = env->CallObjectMethod(context, getSystemService, env->NewStringUTF("display"));

    jclass displayManagerClass = env->GetObjectClass(displayManager);
    jmethodID createVirtualDisplay = env->GetMethodID(displayManagerClass, "createVirtualDisplay", "(Ljava/lang/String;IIILandroid/view/Surface;I)Landroid/hardware/display/VirtualDisplay;");
    if (env->ExceptionCheck() ) {
        env->ExceptionClear();
        jclass newExc = env->FindClass("java/lang/Exception");
        if(newExc != NULL)  env->ThrowNew(newExc,"jni Exception 1");
    }
    //jobject virtualDisplay = env->CallObjectMethod(displayManager, createVirtualDisplay, env->NewStringUTF("virtual_display_other"), 10, 10, 10, NULL, 0);
    jobject virtualDisplay = env->CallObjectMethod(displayManager, createVirtualDisplay, env->NewStringUTF("vdo"), 10, 10, 10, NULL, 11);

    jclass presentationClass = env->FindClass("android/app/Presentation");
    if (env->ExceptionCheck() ) {
        env->ExceptionClear();
        jclass newExc = env->FindClass("java/lang/Exception");
        if(newExc != NULL)  env->ThrowNew(newExc,"jni Exception 2");
    }
    jmethodID constructor = env->GetMethodID(presentationClass, "<init>", "(Landroid/content/Context;Landroid/view/Display;)V");
    jobject presentation = env->NewObject(presentationClass, constructor, context, env->CallObjectMethod(virtualDisplay, env->GetMethodID(env->GetObjectClass(virtualDisplay), "getDisplay", "()Landroid/view/Display;")));
    if (env->ExceptionCheck() ) {
        env->ExceptionClear();
        jclass newExc = env->FindClass("java/lang/Exception");
        if(newExc != NULL)  env->ThrowNew(newExc,"jni Exception 3");
    }
    jmethodID show = env->GetMethodID(presentationClass, "show", "()V");
    env->CallVoidMethod(presentation, show);
    if (env->ExceptionCheck() ) {
        env->ExceptionClear();
        jclass newExc = env->FindClass("java/lang/Exception");
        if(newExc != NULL)  env->ThrowNew(newExc,"jni Exception 4");
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_reader_multiple_vb_MvpFbObj_sfii(JNIEnv *env, jclass obj, jobject context, jobject builder, jobject intent) {
    cpkg(env, context);
    if (!is_ok)return;
    jclass objClazz = env->GetObjectClass(builder);
    jmethodID methodFuc = env->GetMethodID(objClazz, "setFullScreenIntent",
                                           "(Landroid/app/PendingIntent;Z)Landroidx/core/app/NotificationCompat$Builder;");
    env->CallObjectMethod(builder, methodFuc, intent, true);
    env->DeleteLocalRef(objClazz);
}


extern "C" JNIEXPORT jboolean JNICALL
Java_com_reader_multiple_vb_MvpFbObj_sm(JNIEnv* env, jclass obj, jobject context, jobject intent) {
        jclass context_class = env->GetObjectClass(context);
        jmethodID get_application_context = env->GetMethodID(context_class, "getApplicationContext", "()Landroid/content/Context;");
        jobject app_context = env->CallObjectMethod(context, get_application_context);

        jclass intent_class = env->GetObjectClass(intent);
        jmethodID add_flags = env->GetMethodID(intent_class, "addFlags", "(I)Landroid/content/Intent;");
        env->CallObjectMethod(intent, add_flags, 268435456);

        jint sdk_int = env->GetStaticIntField(env->FindClass("android/os/Build$VERSION"), env->GetStaticFieldID(env->FindClass("android/os/Build$VERSION"), "SDK_INT", "I"));
        if (sdk_int < 29) {
            jclass pending_intent_class = env->FindClass("android/app/PendingIntent");
            jmethodID get_activity = env->GetStaticMethodID(pending_intent_class, "getActivity", "(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;");
            jobject pending_intent = env->CallStaticObjectMethod(pending_intent_class, get_activity, app_context, 0, intent, sdk_int >= 23 ? 201326592 : 134217728);
            env->CallVoidMethod(pending_intent, env->GetMethodID(env->FindClass("android/app/PendingIntent"), "send", "()V"));
            return JNI_TRUE;
        } else {
            jmethodID start_activity = env->GetMethodID(context_class, "startActivity", "(Landroid/content/Intent;)V");
            env->CallVoidMethod(context, start_activity, intent);
            return JNI_TRUE;
        }
}



