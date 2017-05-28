/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <opencv2/opencv.hpp>
#include <stdio.h>
using namespace cv;
using namespace std;
/* Header for class com_example_ivan_opencvdetect_OpencvNative */

#ifndef _Included_com_example_ivan_opencvdetect_OpencvNative
#define _Included_com_example_ivan_opencvdetect_OpencvNative
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_ivan_opencvdetect_OpencvNative
 * Method:    fdetect
 * Signature: (JJ)V
 */
void detect(Mat& frame, string cascades);
void convert(Mat& frame, int a);
void Corner(Mat& frame);
void Camshift(Mat frame);
string name;
Rect ROI;
Mat ya;
CascadeClassifier cascade;
//Rect a;
Mat hist, hue;
//const float* phranges;
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_detect
  (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_fColor
(JNIEnv *, jclass, jlong ,jint, jint,jint,jint,jint, jint);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_loadCascades
(JNIEnv *, jclass, jint);

JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_cHSV
(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_CamShift
(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_Harris
(JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_loadUserCascade
(JNIEnv *, jclass, jstring);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_rect
(JNIEnv *, jclass, jlong, jint, jint,jint,jint);
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_MakeHist
(JNIEnv *, jclass, jlong, jint, jint,jint,jint);


#ifdef __cplusplus
}
#endif
#endif
