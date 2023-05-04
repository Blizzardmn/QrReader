# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("own")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        james

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        james.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
add_definitions(-std=c++11)

## 源码编译混淆配置
#SET(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -mllvm -fla -mllvm -bcf -mllvm -sub")
#SET(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -mllvm -fla -mllvm -bcf -mllvm -sub")

#[[set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -mllvm -sub -mllvm -sobf -mllvm -fla")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -mllvm -sub -mllvm -sobf -mllvm -fla")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -mllvm -sub -mllvm -sobf -mllvm -fla -mllvm -bcf" )
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -mllvm -sub -mllvm -sobf -mllvm -fla -mllvm -bcf" )]]
#set(CMAKE_CXX_FLAGS "-mllvm -sub -mllvm -sobf -mllvm -fla -mllvm -bcf")

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        james

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

#include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include/)

#add_compile_options(-fno-rtti -O3)

#message(@@@${CMAKE_CURRENT_SOURCE_DIR})

#link_directories(native_binder_libs/${CMAKE_ANDROID_ARCH_ABI})

#add_library( # Sets the name of the library.
#        bhtest
#
#        # Sets the library as a shared library.
#        SHARED
#
#        # Provides a relative path to your source file(s).
#        bhtest.cpp)
#
#find_library(log-lib log)
#
#target_link_libraries(
#        bhtest
#        ${log-lib} binder cutils utils c)