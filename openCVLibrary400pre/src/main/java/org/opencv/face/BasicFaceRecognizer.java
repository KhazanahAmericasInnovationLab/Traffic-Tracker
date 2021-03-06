//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.face;

import org.opencv.core.Mat;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

// C++: class BasicFaceRecognizer
//javadoc: BasicFaceRecognizer

public class BasicFaceRecognizer extends FaceRecognizer {

    protected BasicFaceRecognizer(long addr) {
        super(addr);
    }

    // internal usage only
    public static BasicFaceRecognizer __fromPtr__(long addr) {
        return new BasicFaceRecognizer(addr);
    }

    //
    // C++:  Mat cv::face::BasicFaceRecognizer::getEigenValues()
    //

    // C++:  Mat cv::face::BasicFaceRecognizer::getEigenValues()
    private static native long getEigenValues_0(long nativeObj);


    //
    // C++:  Mat cv::face::BasicFaceRecognizer::getEigenVectors()
    //

    // C++:  Mat cv::face::BasicFaceRecognizer::getEigenVectors()
    private static native long getEigenVectors_0(long nativeObj);


    //
    // C++:  Mat cv::face::BasicFaceRecognizer::getLabels()
    //

    // C++:  Mat cv::face::BasicFaceRecognizer::getLabels()
    private static native long getLabels_0(long nativeObj);


    //
    // C++:  Mat cv::face::BasicFaceRecognizer::getMean()
    //

    // C++:  Mat cv::face::BasicFaceRecognizer::getMean()
    private static native long getMean_0(long nativeObj);


    //
    // C++:  double cv::face::BasicFaceRecognizer::getThreshold()
    //

    // C++:  double cv::face::BasicFaceRecognizer::getThreshold()
    private static native double getThreshold_0(long nativeObj);


    //
    // C++:  int cv::face::BasicFaceRecognizer::getNumComponents()
    //

    // C++:  int cv::face::BasicFaceRecognizer::getNumComponents()
    private static native int getNumComponents_0(long nativeObj);


    //
    // C++:  vector_Mat cv::face::BasicFaceRecognizer::getProjections()
    //

    // C++:  vector_Mat cv::face::BasicFaceRecognizer::getProjections()
    private static native long getProjections_0(long nativeObj);


    //
    // C++:  void cv::face::BasicFaceRecognizer::setNumComponents(int val)
    //

    // C++:  void cv::face::BasicFaceRecognizer::setNumComponents(int val)
    private static native void setNumComponents_0(long nativeObj, int val);


    //
    // C++:  void cv::face::BasicFaceRecognizer::setThreshold(double val)
    //

    // C++:  void cv::face::BasicFaceRecognizer::setThreshold(double val)
    private static native void setThreshold_0(long nativeObj, double val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

    //javadoc: BasicFaceRecognizer::getEigenValues()
    public Mat getEigenValues() {

        Mat retVal = new Mat(getEigenValues_0(nativeObj));

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::getEigenVectors()
    public Mat getEigenVectors() {

        Mat retVal = new Mat(getEigenVectors_0(nativeObj));

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::getLabels()
    public Mat getLabels() {

        Mat retVal = new Mat(getLabels_0(nativeObj));

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::getMean()
    public Mat getMean() {

        Mat retVal = new Mat(getMean_0(nativeObj));

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::getThreshold()
    public double getThreshold() {

        double retVal = getThreshold_0(nativeObj);

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::setThreshold(val)
    public void setThreshold(double val) {

        setThreshold_0(nativeObj, val);

        return;
    }

    //javadoc: BasicFaceRecognizer::getNumComponents()
    public int getNumComponents() {

        int retVal = getNumComponents_0(nativeObj);

        return retVal;
    }

    //javadoc: BasicFaceRecognizer::setNumComponents(val)
    public void setNumComponents(int val) {

        setNumComponents_0(nativeObj, val);

        return;
    }

    //javadoc: BasicFaceRecognizer::getProjections()
    public List<Mat> getProjections() {
        List<Mat> retVal = new ArrayList<Mat>();
        Mat retValMat = new Mat(getProjections_0(nativeObj));
        Converters.Mat_to_vector_Mat(retValMat, retVal);
        return retVal;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
