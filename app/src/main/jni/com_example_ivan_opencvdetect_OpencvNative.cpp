#include <com_example_ivan_opencvdetect_OpencvNative.h>
#include <iostream>
#include <cstring>
#include <android/log.h>
#define APPNAME "MyApp"
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_detect
  (JNIEnv *, jclass, jlong addrRgba){
       Mat& mRgb = *(Mat*)addrRgba;
        detect(mRgb,name);

}
void removech(char *c) {
    int i;
    for(i = 0; i < strlen(c); i++) {
        c[i] = c[i+1];
    }
    c[i-1] = '\0';
}
int hsize = 16;
float hranges[] = {0,180};
const float* phranges = hranges;
JNIEXPORT jint JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_loadCascades
(JNIEnv *env, jclass, jint TYPE, jstring path) {
    int x = (int) TYPE;
    name = (char*)(*env).GetStringUTFChars(path,0);
    name = (char*)realloc(name, 40);
    switch(x) {
        case 0: strcat(name, "/lbpcascade_frontalface.xml");break;
        case 1: name =  strcat(name,"/haarcascade_fullbody.xml");break;
        case 2: name =  strcat(name,"/haarcascade_eye.xml");break;
        case 3: name =  strcat(name,"/haarcascade_frontalcatface_extended.xml");break;
        case 4: name =  strcat(name,"/haarcascade_frontalface_alt.xml"); break;
        default: printf("BUGGED");

    }
    //removech(name);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, " TEST :%s", name);
    if( !cascade.load(name)){ printf("--(!)Error loading\n");return -1;};
}
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_MakeHist
(JNIEnv *, jclass, jlong addr, jint x1, jint y1,jint x2,jint y2) {
    int x,y;
    x = (int)x1;
    y = (int)y1;
    //jint width = x1 -x2;
    //jint height = (int)y2 - (int)y1;
    ROI.width=(int)x2;
    ROI.height=(int)y2;
    ROI.x = (int)x1;
    ROI.y = (int)y1;
    Mat image;
    Mat& frame = *(Mat*)addr;
    rectangle(frame, Point(100,250), Point(100 + 99, 250-100), Scalar(255,0,0), 2,8,0);
    cvtColor(frame, image, COLOR_BGR2HSV);
    Mat mask;
    int ch[] = {0, 0};
    hue.create(frame.size(), frame.depth());
    mixChannels(&image, 1, &hue, 1, ch, 1);
    inRange(image, Scalar(0., 62., 32.),Scalar(180., 255., 255.), mask);
    Mat roi(hue, ROI);
    Mat maskroi(mask, ROI);
    ya = roi;
    calcHist(&roi, 1, 0, maskroi, hist, 1, &hsize, &phranges);
    normalize(hist, hist, 0, 255, NORM_MINMAX);
}
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_CamShift
(JNIEnv *, jclass, jlong addr) {
    Mat& frame = *(Mat*)addr;
    Mat hsv,backproj;
    cvtColor(frame,hsv,COLOR_BGR2HSV);
    int ch[] = {0, 0};
    hue.create(hsv.size(), hsv.depth());
    mixChannels(&hsv, 1, &hue, 1, ch, 1);
    //Mat roi(hue, ROI);
    //frame = ya;
    calcBackProject(&hue, 1, 0, hist, backproj, &phranges);
    RotatedRect trackBox = CamShift(backproj,ROI , TermCriteria( TermCriteria::EPS | TermCriteria::COUNT, 10, 1 ));
    //float tt = trackBox.size;
    ellipse(frame, trackBox, Scalar(0,255,255), 2,8);

}
/*void Camshift(Mat frame) {
    Mat backproj;
    Mat hsv;
    cvtColor(frame,hsv, COLOR_RGBA2BGR);
    cvtColor(hsv, hsv, COLOR_BGR2HSV);
    calcBackProject(&hsv, 1, 0, hist, backproj, &phranges);
    //RotatedRect trackBox = CamShift(backproj,ROI , TermCriteria( TermCriteria::EPS | TermCriteria::COUNT, 10, 1 ));
    //rectangle(frame, Point(trackBox.x , trackBox.y), Point(trackBox.x + trackBox.width, trackBox.height + trackBox.y), Scalar(255,0,255), 4, 8, 0);
    //circle(frame, trackBox.center, 360, Scalar(0,255,255), 1, 8, 0);
}*/
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_loadUserCascade
(JNIEnv* env, jclass, jstring path) {
    const char *uname = (*env).GetStringUTFChars(path,0);
    if( !cascade.load(uname)){ printf("--(!)Error loading\n");};
}
void detect(Mat& frame, string cascades) {
    if( !cascade.load( cascades ) ){ printf("--(!)Error loading\n");};
     vector<Rect> faces;
    int r,g,b;
     Mat frame_gray;
       cvtColor( frame, frame_gray, CV_BGR2GRAY );
       equalizeHist(frame_gray, frame_gray);
       cascade.detectMultiScale( frame_gray, faces, 1.05,3, 0|CV_HAAR_SCALE_IMAGE, Size(60, 60));
       for( size_t i = 0; i < faces.size(); i++ )
       {
           r = rand()%255;
           g = rand()%255;
           b = rand()%255;
           rectangle(frame, Point(faces[i].x, faces[i].y),Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height), Scalar( r, g, b ), 4, 8,0 );
       }

}

JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_cHSV
(JNIEnv *, jclass, jlong addrRgba) {
    Mat& frame = *(Mat*)addrRgba;
    convert(frame, CV_RGB2HSV);
}

void convert(Mat& frame, int a) {
    Mat converted;
    cvtColor(frame,converted,a);
    frame = converted;
}
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_fColor
(JNIEnv *, jclass, jlong addr ,jint h1, jint s1,jint v1,jint h2,jint v2, jint s2) {
    Mat& frame = *(Mat*)addr;
    Mat need;
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    cvtColor(frame, need, CV_RGB2HSV);
    inRange(need, Scalar((int)h1,(int)v1,(int)s1), Scalar((int)h2,(int)v2,(int)s2), need);
    findContours(need, contours,hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0,0));
    vector<Rect> boundRect( contours.size() );
    vector<vector<Point> > contours_poly( contours.size() );
    for( int i = 0; i< contours.size(); i++ )
    {
        approxPolyDP(Mat(contours[i]), contours_poly[i], 3, false );
        boundRect[i] = boundingRect(Mat(contours_poly[i]));
        rectangle( frame, boundRect[i].tl(), boundRect[i].br(), Scalar(0,255,0), 2, 8, 0 );
    }
}
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_rect
(JNIEnv *, jclass, jlong addr, jint x1,  jint y1,jint x2,jint y2) {
    Mat& frame = *(Mat*)addr;
    //rectangle(frame, Point((int)x1,(int)y1), Point((int)x2, (int)y2), Scalar(255,0,255), 2, 8 , 0);
    rectangle(frame, Point((int)x1,(int)y1), Point((int)x2, (int)y2), Scalar(255,0,255), 2, 8 , 0);
}
JNIEXPORT void JNICALL Java_com_example_ivan_opencvdetect_OpencvNative_Harris
(JNIEnv *, jclass, jlong addr) {
    Mat& frame = *(Mat*)addr;
    Mat gray, corners,co;
    int blockSize = 2;
    int apertureSize = 3 ;
    double k = 0.04;
    cvtColor(frame,gray, COLOR_BGR2GRAY);
    corners = Mat::zeros(frame.size(), CV_32FC1);
    cornerHarris( gray, corners, blockSize, apertureSize, k, BORDER_DEFAULT );
    normalize( corners, co, 0, 255, NORM_MINMAX, CV_32FC1, Mat() );
    //Mat rand;
    //convertScaleAbs( co, rand );
    int r,g,b;
    int c = 0;
    for( int j = 0; j < co.rows ; j++ ){
        for( int i = 0; i < co.cols; i++ ) {
            if( (int) co.at<float>(j,i) > 170 && c < 300 ) {
                r = rand()%255;g = rand()%255; b = rand()%255;
                circle( frame, Point( i, j ), 5,  Scalar(r,g,b), 2, 8, 0 ); c++;
            }
        }
    }
}