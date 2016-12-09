#include <jni.h>
#include <string>

extern "C"
jstring
Java_fred_freechess_Play_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";


    return env->NewStringUTF(hello.c_str());
}
