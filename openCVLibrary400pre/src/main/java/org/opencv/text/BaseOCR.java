//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.text;


// C++: class BaseOCR
//javadoc: BaseOCR

public class BaseOCR {

    protected final long nativeObj;

    protected BaseOCR(long addr) {
        nativeObj = addr;
    }

    // internal usage only
    public static BaseOCR __fromPtr__(long addr) {
        return new BaseOCR(addr);
    }

    // native support for java finalize()
    private static native void delete(long nativeObj);

    public long getNativeObjAddr() {
        return nativeObj;
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
