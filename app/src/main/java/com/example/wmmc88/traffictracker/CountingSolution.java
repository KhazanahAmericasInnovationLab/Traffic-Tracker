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
    private final int mBoxThreshold = 3000;
    //6000
    BackgroundSubtractorMOG2 mBackgroundSubtractor;
    private int mZone1Count = 0;
    private int mZone2Count = 0;


    protected CountingSolution() {
        this.mBackgroundSubtractor = Video.createBackgroundSubtractorMOG2(500, 16, true);
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
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    protected Mat findMask(Mat inputImage) {
        Mat fgMask = new Mat();
        this.mBackgroundSubtractor.apply(inputImage, fgMask);
//        Imgproc.blur(image, image, new Size(5, 5));
//        //TODO replace with Adaptive Threshold
//        Imgproc.threshold(image, image, mMaskThreshold, 255, Imgproc.THRESH_BINARY);
//        //TODO erodeKernelSetting
//        Mat erodeKernel = Mat.ones(new Size(3, 3), CvType.CV_8U);
//        Imgproc.erode(image, image, erodeKernel, new Point(-1, -1), 3);
//        Imgproc.medianBlur(image, image, 7);
        return fgMask;
    }

    protected void findObjects(Mat inputImg, Mat outputImg, Size previewFrameSize) {

        if (inputImg.channels() == 4) {
            Imgproc.cvtColor(inputImg, inputImg, Imgproc.COLOR_BGRA2BGR);
        } else if (inputImg.channels() == 3) {
            Imgproc.cvtColor(inputImg, inputImg, Imgproc.COLOR_RGB2BGR);
        } else {

        }

        final int displayRow = 2;
        final int displayCol = 4;

        Mat[][] intermediateMats = new Mat[displayRow][displayCol];

        //Input Image
        intermediateMats[0][0] = inputImg;

        //Foreground Mask
        Mat fgMask = intermediateMats[0][1] = this.findMask(inputImg);

        //Threshold (Remove Shadows)
        Mat fgMaskNoShadows;
        Imgproc.threshold(fgMask, fgMaskNoShadows = intermediateMats[0][2] = new Mat(inputImg.size(), inputImg.type()), 128, 255, Imgproc.THRESH_BINARY);


        //Close (remove holes in mask)
//        Mat openedMask;
//        Imgproc.morphologyEx(fgMaskNoShadows, openedMask = intermediateMats[0][3] = new Mat(inputImg.size(), inputImg.type()), Imgproc.MORPH_CLOSE,Mat.ones(new Size(15, 15),CvType.CV_8UC1), new Point(-1,-1), 1);

        ///Median Blur
        Mat medianFgMask;
        Imgproc.medianBlur(fgMaskNoShadows, medianFgMask = intermediateMats[0][3] = new Mat(inputImg.size(), inputImg.type()), 75);

        //Dilate
        Mat dilatedMat;
        Imgproc.dilate(medianFgMask, dilatedMat = intermediateMats[1][0] = new Mat(inputImg.size(), inputImg.type()), Mat.ones(10, 10, CvType.CV_8UC1), new Point(-1, -1), 3);

        //Draw Contours
        List<MatOfPoint> contours = this.findContours(dilatedMat);
        Mat contourMat;
        Imgproc.drawContours(contourMat = intermediateMats[1][1] = Mat.zeros(inputImg.size(), CvType.CV_8UC3), contours, -1, new Scalar(255, 255, 255));

        //Draw Bounding Boxes on Contours Image
        List<Rect> boundingBoxes = this.findBoundingBoxes(contours);
        for (Rect boundingBox : boundingBoxes) {
            Imgproc.rectangle(contourMat, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0));
        }

        //Draw Rotated(min Area) Bounding Boxes on Contours Image
        List<RotatedRect> rotatedBoundingBoxes = findRotatedBoundingBoxes(contours);
        List<MatOfPoint> rotatedBoundingBoxesVertices = new ArrayList<>();
        Point[] vertices = new Point[4];
        for (RotatedRect rotatedBoundingBox : rotatedBoundingBoxes) {
            rotatedBoundingBox.points(vertices);
            rotatedBoundingBoxesVertices.add(new MatOfPoint(vertices));
        }
        Imgproc.drawContours(contourMat, rotatedBoundingBoxesVertices, -1, new Scalar(0, 255, 255));

//        //Draw Rotated(min Area) Bounding Boxes on Input Image
//        Mat finalMat = intermediateMats[1][2] = inputImg.clone();
//        for (RotatedRect rotatedBoundingBox : rotatedBoundingBoxes) {
//            rotatedBoundingBox.points(vertices);
//            rotatedBoundingBoxesVertices.add(new MatOfPoint(vertices));
//        }
//        Imgproc.drawContours(finalMat, rotatedBoundingBoxesVertices, -1, new Scalar(0, 255, 255));


        //Draw Bounding Boxes on Input Image
        Mat finalMat2 = intermediateMats[1][3] = inputImg.clone();
        for (Rect boundingBox : boundingBoxes) {
            Imgproc.rectangle(finalMat2, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0));
        }


        //Move Mats to final Mat
        for (int row = 0; row < displayRow; row++) {
            for (int col = 0; col < displayCol; col++) {
                if (intermediateMats[row][col] != null && !intermediateMats[row][col].size().empty()) {
                    Imgproc.resize(intermediateMats[row][col], intermediateMats[row][col], previewFrameSize);

                    if (intermediateMats[row][col].channels() != 3) {
                        Imgproc.cvtColor(intermediateMats[row][col], intermediateMats[row][col], Imgproc.COLOR_GRAY2BGR, 3);
                    }

                    intermediateMats[row][col].copyTo(outputImg.submat(row * (int) previewFrameSize.height, (row + 1) * (int) previewFrameSize.height, col * (int) previewFrameSize.width, (col + 1) * (int) previewFrameSize.width));
                }
            }
        }
    }

    public int getmZone1Count() {
        return mZone1Count;
    }

    public int getmZone2Count() {
        return mZone2Count;
    }

}
