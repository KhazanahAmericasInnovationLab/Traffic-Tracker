//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ximgproc;

// C++: class SelectiveSearchSegmentationStrategyFill
//javadoc: SelectiveSearchSegmentationStrategyFill

public class SelectiveSearchSegmentationStrategyFill extends SelectiveSearchSegmentationStrategy {

    protected SelectiveSearchSegmentationStrategyFill(long addr) {
        super(addr);
    }

    // internal usage only
    public static SelectiveSearchSegmentationStrategyFill __fromPtr__(long addr) {
        return new SelectiveSearchSegmentationStrategyFill(addr);
    }

    // native support for java finalize()
    private static native void delete(long nativeObj);

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }

}
