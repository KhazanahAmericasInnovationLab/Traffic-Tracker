//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.face;

// C++: class FacemarkLBF
//javadoc: FacemarkLBF

public class FacemarkLBF extends FacemarkTrain {

    protected FacemarkLBF(long addr) {
        super(addr);
    }

    // internal usage only
    public static FacemarkLBF __fromPtr__(long addr) {
        return new FacemarkLBF(addr);
    }

    // native support for java finalize()
    private static native void delete(long nativeObj);

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
