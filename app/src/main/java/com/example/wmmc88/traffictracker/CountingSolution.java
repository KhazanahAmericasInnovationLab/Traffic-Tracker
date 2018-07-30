package com.example.wmmc88.traffictracker;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.List;

class CountingSolution {
    //TODO Switch to using preferences
    private final int mMaskThreshold;
    private final int mBoxThreshold;

    private int mZone1Count = 0;
    private int mZone2Count = 0;

    private BackgroundSubtractorMOG2 mBackgroundSubtractor;


    protected CountingSolution(Builder<?> builder) {
        this.mMaskThreshold = builder.mMaskThreshold;
        this.mBoxThreshold = builder.mBoxThreshold;
        this.mBackgroundSubtractor = Video.createBackgroundSubtractorMOG2(builder.mHistory, builder.mVarThreshold, builder.mDetectShadows);
    }

    protected List<Rect> findBoundingBoxes(Mat image) {
        List<MatOfPoint> contours = findContours(image);
        List<Rect> boundingBoxes = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect boundingBox = Imgproc.boundingRect(contour);
            if (boundingBox.area() >= mBoxThreshold) {
                boundingBoxes.add(boundingBox);
            }
        }
        return boundingBoxes;
    }

    private List<MatOfPoint> findContours(Mat image) {
        findMask(image);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    private Mat findMask(Mat image) {
        this.mBackgroundSubtractor.apply(image, image);
        Imgproc.blur(image, image, new Size(5, 5));
        //TODO verify Change in Thresh_Binary_INV to Thresh_Binary and removal of binary_not
        //TODO replace with Adaptive Threshold
        Imgproc.threshold(image, image, mMaskThreshold, 255, Imgproc.THRESH_BINARY);
        //TODO erodeKernelSetting
        Mat erodeKernel = Mat.ones(new Size(3, 3), CvType.CV_8U);
        Imgproc.erode(image, image, erodeKernel, new Point(-1, -1), 3);
        Imgproc.medianBlur(image, image, 7);
        return image;
    }

    public int getmZone1Count() {
        return mZone1Count;
    }

    public int getmZone2Count() {
        return mZone2Count;
    }

    //TODO fix unsafe access
    public static class Builder<T extends Builder<T>> {
        //mMaskThreshold was 180
        private int mMaskThreshold = 75;
        private int mBoxThreshold = 6000;

        private int mHistory = 500;
        private int mVarThreshold = 16;
        private boolean mDetectShadows = true;

        public Builder() {
        }

        public CountingSolution build() {
            return new CountingSolution(this);
        }

        public T withMaskThreshold(int maskThreshold) {
            this.mMaskThreshold = maskThreshold;
            return (T) this;
        }

        public T withBoxThreshold(int boxThreshold) {
            this.mBoxThreshold = boxThreshold;
            return (T) this;
        }

        public T withHistory(int history) {
            this.mHistory = history;
            return (T) this;
        }

        public T withVarThreshold(int varThreshold) {
            this.mVarThreshold = varThreshold;
            return (T) this;
        }

        public T withDetectShadows(boolean mDetectShadows) {
            this.mDetectShadows = mDetectShadows;
            return (T) this;
        }

    }


}
