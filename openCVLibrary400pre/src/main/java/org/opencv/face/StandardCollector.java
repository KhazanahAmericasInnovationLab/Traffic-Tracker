//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.face;

// C++: class StandardCollector
//javadoc: StandardCollector

public class StandardCollector extends PredictCollector {

    protected StandardCollector(long addr) {
        super(addr);
    }

    // internal usage only
    public static StandardCollector __fromPtr__(long addr) {
        return new StandardCollector(addr);
    }

    //
    // C++: static Ptr_StandardCollector cv::face::StandardCollector::create(double threshold = DBL_MAX)
    //

    //javadoc: StandardCollector::create(threshold)
    public static StandardCollector create(double threshold) {

        StandardCollector retVal = StandardCollector.__fromPtr__(create_0(threshold));

        return retVal;
    }

    //javadoc: StandardCollector::create()
    public static StandardCollector create() {

        StandardCollector retVal = StandardCollector.__fromPtr__(create_1());

        return retVal;
    }


    //
    // C++:  double cv::face::StandardCollector::getMinDist()
    //

    // C++: static Ptr_StandardCollector cv::face::StandardCollector::create(double threshold = DBL_MAX)
    private static native long create_0(double threshold);


    //
    // C++:  int cv::face::StandardCollector::getMinLabel()
    //

    private static native long create_1();


    //
    // C++:  vector_pair_int_and_double cv::face::StandardCollector::getResults(bool sorted = false)
    //

    // Return type 'vector_pair_int_and_double' is not supported, skipping the function

    // C++:  double cv::face::StandardCollector::getMinDist()
    private static native double getMinDist_0(long nativeObj);

    // C++:  int cv::face::StandardCollector::getMinLabel()
    private static native int getMinLabel_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

    //javadoc: StandardCollector::getMinDist()
    public double getMinDist() {

        double retVal = getMinDist_0(nativeObj);

        return retVal;
    }

    //javadoc: StandardCollector::getMinLabel()
    public int getMinLabel() {

        int retVal = getMinLabel_0(nativeObj);

        return retVal;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
