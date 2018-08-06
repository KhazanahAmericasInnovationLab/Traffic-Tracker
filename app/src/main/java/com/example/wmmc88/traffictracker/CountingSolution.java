package com.example.wmmc88.traffictracker;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.List;

class CountingSolution {
    //TODO Switch to using preferences
    protected final int mMaskThreshold = 75;
    private final int mBoxThreshold = 3000;
    //6000

    private int mZone1Count = 0;
    private int mZone2Count = 0;

    private BackgroundSubtractorMOG2 mBackgroundSubtractor;


    protected CountingSolution() {

        this.mBackgroundSubtractor = Video.createBackgroundSubtractorMOG2(500, 16, false);
    }

    protected List<RotatedRect> findRotatedBoundingBoxes(List<MatOfPoint> contours) {
        MatOfPoint2f convertedContour = new MatOfPoint2f();

        List<RotatedRect> rotatedBoundingBoxes = new ArrayList<>();

        for (MatOfPoint contour : contours) {
            contour.convertTo(convertedContour, CvType.CV_32F);
            RotatedRect rotatedBoundingBox = Imgproc.minAreaRect(convertedContour);
            if (rotatedBoundingBox.size.height * rotatedBoundingBox.size.width >= mBoxThreshold) {
                rotatedBoundingBoxes.add(rotatedBoundingBox);
            }
        }
        return rotatedBoundingBoxes;
    }

    protected List<Rect> findBoundingBoxes(List<MatOfPoint> contours) {
        List<Rect> boundingBoxes = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect boundingBox = Imgproc.boundingRect(contour);
            if (boundingBox.area() >= mBoxThreshold) {
                boundingBoxes.add(boundingBox);
            }
        }
        return boundingBoxes;
    }

    protected List<MatOfPoint> findContours(Mat image) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    protected Mat findMask(Mat image) {
        this.mBackgroundSubtractor.apply(image, image);
//        Imgproc.blur(image, image, new Size(5, 5));
//        //TODO verify Change in Thresh_Binary_INV to Thresh_Binary and removal of binary_not
//        //TODO replace with Adaptive Threshold
//        Imgproc.threshold(image, image, mMaskThreshold, 255, Imgproc.THRESH_BINARY);
//        //TODO erodeKernelSetting
//        Mat erodeKernel = Mat.ones(new Size(3, 3), CvType.CV_8U);
//        Imgproc.erode(image, image, erodeKernel, new Point(-1, -1), 3);
//        Imgproc.medianBlur(image, image, 7);
        return image;
    }

    protected void findObjects(Mat inputImg, Mat outputImg, Size previewFrameSize) {

        Mat tempMat = new Mat();
        Mat resizedTempMat = new Mat();


        if (inputImg.channels() == 3) {
            Imgproc.cvtColor(inputImg, inputImg, Imgproc.COLOR_RGB2BGR);
            Imgproc.cvtColor(inputImg, inputImg, Imgproc.COLOR_BGR2BGRA);
        }

        Imgproc.resize(inputImg, resizedTempMat, previewFrameSize);
        resizedTempMat.copyTo(outputImg.submat(0 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.height, 0 * (int) previewFrameSize.width, 1 * (int) previewFrameSize.width));

        tempMat = this.findMask(inputImg);
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(outputImg.submat(0 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.width, 2 * (int) previewFrameSize.width));

        Imgproc.blur(tempMat, tempMat, new Size(5, 5));
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(outputImg.submat(0 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.width, 3 * (int) previewFrameSize.width));

        Imgproc.threshold(tempMat, tempMat, this.mMaskThreshold, 255, Imgproc.THRESH_BINARY);
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(outputImg.submat(0 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.height, 3 * (int) previewFrameSize.width, 4 * (int) previewFrameSize.width));

        Mat erodeKernel = Mat.ones(new Size(3, 3), CvType.CV_8U);
        Imgproc.erode(tempMat, tempMat, erodeKernel, new Point(-1, -1), 3);
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(outputImg.submat(1 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.height, 0 * (int) previewFrameSize.width, 1 * (int) previewFrameSize.width));

        Imgproc.medianBlur(tempMat, tempMat, 7);
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(outputImg.submat(1 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.height, 1 * (int) previewFrameSize.width, 2 * (int) previewFrameSize.width));

        List<MatOfPoint> contours = this.findContours(tempMat);
        Imgproc.cvtColor(tempMat, tempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        Imgproc.drawContours(tempMat, contours, -1, new Scalar(0, 0, 255));
        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        resizedTempMat.copyTo(outputImg.submat(1 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.width, 3 * (int) previewFrameSize.width));

        tempMat = inputImg;
        List<Rect> boundingBoxes = this.findBoundingBoxes(contours);
        for (Rect boundingBox : boundingBoxes) {
            Imgproc.rectangle(tempMat, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0));
        }

        List<RotatedRect> rotatedBoundingBoxes = findRotatedBoundingBoxes(contours);
        List<MatOfPoint> rotatedBoundingBoxesVertices = new ArrayList<>();
        Point[] vertices = new Point[4];
        for (RotatedRect rotatedBoundingBox : rotatedBoundingBoxes) {
            rotatedBoundingBox.points(vertices);
            rotatedBoundingBoxesVertices.add(new MatOfPoint(vertices));
        }
        Imgproc.drawContours(tempMat, rotatedBoundingBoxesVertices, -1, new Scalar(0, 255, 255));

        Imgproc.resize(tempMat, resizedTempMat, previewFrameSize);
        resizedTempMat.copyTo(outputImg.submat(1 * (int) previewFrameSize.height, 2 * (int) previewFrameSize.height, 3 * (int) previewFrameSize.width, 4 * (int) previewFrameSize.width));

    }

    public int getmZone1Count() {
        return mZone1Count;
    }

    public int getmZone2Count() {
        return mZone2Count;
    }

}
