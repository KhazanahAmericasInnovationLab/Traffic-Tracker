//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ximgproc;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

// C++: class EdgeBoxes
//javadoc: EdgeBoxes

public class EdgeBoxes extends Algorithm {

    protected EdgeBoxes(long addr) {
        super(addr);
    }

    // internal usage only
    public static EdgeBoxes __fromPtr__(long addr) {
        return new EdgeBoxes(addr);
    }

    //
    // C++:  float cv::ximgproc::EdgeBoxes::getAlpha()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getAlpha()
    private static native float getAlpha_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getBeta()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getBeta()
    private static native float getBeta_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getClusterMinMag()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getClusterMinMag()
    private static native float getClusterMinMag_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getEdgeMergeThr()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getEdgeMergeThr()
    private static native float getEdgeMergeThr_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getEdgeMinMag()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getEdgeMinMag()
    private static native float getEdgeMinMag_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getEta()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getEta()
    private static native float getEta_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getGamma()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getGamma()
    private static native float getGamma_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getKappa()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getKappa()
    private static native float getKappa_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getMaxAspectRatio()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getMaxAspectRatio()
    private static native float getMaxAspectRatio_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getMinBoxArea()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getMinBoxArea()
    private static native float getMinBoxArea_0(long nativeObj);


    //
    // C++:  float cv::ximgproc::EdgeBoxes::getMinScore()
    //

    // C++:  float cv::ximgproc::EdgeBoxes::getMinScore()
    private static native float getMinScore_0(long nativeObj);


    //
    // C++:  int cv::ximgproc::EdgeBoxes::getMaxBoxes()
    //

    // C++:  int cv::ximgproc::EdgeBoxes::getMaxBoxes()
    private static native int getMaxBoxes_0(long nativeObj);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::getBoundingBoxes(Mat edge_map, Mat orientation_map, vector_Rect& boxes)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::getBoundingBoxes(Mat edge_map, Mat orientation_map, vector_Rect& boxes)
    private static native void getBoundingBoxes_0(long nativeObj, long edge_map_nativeObj, long orientation_map_nativeObj, long boxes_mat_nativeObj);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setAlpha(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setAlpha(float value)
    private static native void setAlpha_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setBeta(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setBeta(float value)
    private static native void setBeta_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setClusterMinMag(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setClusterMinMag(float value)
    private static native void setClusterMinMag_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setEdgeMergeThr(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setEdgeMergeThr(float value)
    private static native void setEdgeMergeThr_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setEdgeMinMag(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setEdgeMinMag(float value)
    private static native void setEdgeMinMag_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setEta(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setEta(float value)
    private static native void setEta_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setGamma(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setGamma(float value)
    private static native void setGamma_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setKappa(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setKappa(float value)
    private static native void setKappa_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setMaxAspectRatio(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setMaxAspectRatio(float value)
    private static native void setMaxAspectRatio_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setMaxBoxes(int value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setMaxBoxes(int value)
    private static native void setMaxBoxes_0(long nativeObj, int value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setMinBoxArea(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setMinBoxArea(float value)
    private static native void setMinBoxArea_0(long nativeObj, float value);


    //
    // C++:  void cv::ximgproc::EdgeBoxes::setMinScore(float value)
    //

    // C++:  void cv::ximgproc::EdgeBoxes::setMinScore(float value)
    private static native void setMinScore_0(long nativeObj, float value);

    // native support for java finalize()
    private static native void delete(long nativeObj);

    //javadoc: EdgeBoxes::getAlpha()
    public float getAlpha() {

        float retVal = getAlpha_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setAlpha(value)
    public void setAlpha(float value) {

        setAlpha_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getBeta()
    public float getBeta() {

        float retVal = getBeta_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setBeta(value)
    public void setBeta(float value) {

        setBeta_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getClusterMinMag()
    public float getClusterMinMag() {

        float retVal = getClusterMinMag_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setClusterMinMag(value)
    public void setClusterMinMag(float value) {

        setClusterMinMag_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getEdgeMergeThr()
    public float getEdgeMergeThr() {

        float retVal = getEdgeMergeThr_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setEdgeMergeThr(value)
    public void setEdgeMergeThr(float value) {

        setEdgeMergeThr_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getEdgeMinMag()
    public float getEdgeMinMag() {

        float retVal = getEdgeMinMag_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setEdgeMinMag(value)
    public void setEdgeMinMag(float value) {

        setEdgeMinMag_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getEta()
    public float getEta() {

        float retVal = getEta_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setEta(value)
    public void setEta(float value) {

        setEta_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getGamma()
    public float getGamma() {

        float retVal = getGamma_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setGamma(value)
    public void setGamma(float value) {

        setGamma_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getKappa()
    public float getKappa() {

        float retVal = getKappa_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setKappa(value)
    public void setKappa(float value) {

        setKappa_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getMaxAspectRatio()
    public float getMaxAspectRatio() {

        float retVal = getMaxAspectRatio_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setMaxAspectRatio(value)
    public void setMaxAspectRatio(float value) {

        setMaxAspectRatio_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getMinBoxArea()
    public float getMinBoxArea() {

        float retVal = getMinBoxArea_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setMinBoxArea(value)
    public void setMinBoxArea(float value) {

        setMinBoxArea_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getMinScore()
    public float getMinScore() {

        float retVal = getMinScore_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setMinScore(value)
    public void setMinScore(float value) {

        setMinScore_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getMaxBoxes()
    public int getMaxBoxes() {

        int retVal = getMaxBoxes_0(nativeObj);

        return retVal;
    }

    //javadoc: EdgeBoxes::setMaxBoxes(value)
    public void setMaxBoxes(int value) {

        setMaxBoxes_0(nativeObj, value);

        return;
    }

    //javadoc: EdgeBoxes::getBoundingBoxes(edge_map, orientation_map, boxes)
    public void getBoundingBoxes(Mat edge_map, Mat orientation_map, MatOfRect boxes) {
        Mat boxes_mat = boxes;
        getBoundingBoxes_0(nativeObj, edge_map.nativeObj, orientation_map.nativeObj, boxes_mat.nativeObj);

        return;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
