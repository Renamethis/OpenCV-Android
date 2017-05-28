package com.example.ivan.opencvdetect;

/**
 * Created by Ivan on 01.03.2017.
 */

public class OpencvNative {
    public native static void detect(long addrRgba);
    public native static void hdetect(long addrRgba);
    public native static void loadCascades(int type);
    public native static void loadUserCascade(String type);
    public native static void cHSV(long addrRgba);
    public native static void fColor(long addrRgba, int h1,int s1,int v1, int h2,int s2, int v2);
    public native static void Harris(long addrRgba);
    public native static void rect(long addrRgba, int x1, int y1, int x2, int y2);
    public native static void MakeHist(long addrRgba, int x1, int y1, int x2, int y2);
    public native static void CamShift(long addrRgba);
}
