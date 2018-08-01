package com.example.wmmc88.traffictracker;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import static java.lang.Math.round;

public class CameraActivity extends AppCompatActivity implements CustomCameraView.CvCameraViewListener2 {
    public static final int CAMERA_PERMISSION_REQUEST = 1;
    private static final String TAG = CameraActivity.class.getSimpleName();

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.w(TAG, "Could Not Load Local OpenCV Library. Will try opencvmanager.");
            // Handle initialization error
        } else {
//            System.loadLibrary("my_jni_lib1");
//            System.loadLibrary("my_jni_lib2");
        }
    }

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mPreviewFrameWidth;
    private int mPreviewFrameHeight;


    private CountingSolution mCountingSolution;
    //TODO add when implementing opencvManager
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.d(TAG, "onManagerConnected");
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    if (permissionsGranted()) {
                        loadOpenCVView();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //TODO read orientation from settings
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_camera);

    }

    private boolean permissionsGranted() {
        Log.d(TAG, "checkpermissionsGranted");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permissions Granted!");
                    loadOpenCVView();
                } else {
                    Log.w(TAG, "Permissions Denied!");
                    this.finish();
                }
                return;
            }

            //other permission request cases
        }
    }

    private void loadOpenCVView() {
        Log.d(TAG, "loadOpenCVView");

        mOpenCvCameraView = findViewById(R.id.cbvb_camera);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        //TODO lower resolution
        mOpenCvCameraView.setMaxFrameSize(320, 240);
        mOpenCvCameraView.enableView();
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for " +
                    "initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "onCameraViewStarted");

        Toast.makeText(this, width + "x" + height, Toast.LENGTH_LONG).show();

        mCountingSolution = new CountingSolution.Builder().build();

//        android.graphics.Point size = new android.graphics.Point();
//        getWindowManager().getDefaultDisplay().getSize(size);
//        mScreenWidth = size.x;
//        mScreenHeight = size.y;
        //TODO custom frame retrieval
        mScreenWidth = width;
        mScreenHeight = height;


        mPreviewFrameWidth = (int) round(mScreenWidth / 4.0);
        mPreviewFrameHeight = (int) round(mScreenHeight / 2.0);

        mRgba = Mat.zeros(mScreenHeight, mScreenWidth, CvType.CV_8UC4);

    }

    public void onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped");

        mRgba.release();
    }

    //    TODO Rotate Properly
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.v(TAG, "onCameraFrame");

        Mat tempMat = new Mat();
        Mat resizedTempMat = new Mat();

        Imgproc.resize(inputFrame.rgba(), resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        resizedTempMat.copyTo(mRgba.submat(0 * mPreviewFrameHeight, 1 * mPreviewFrameHeight, 0 * mPreviewFrameWidth, 1 * mPreviewFrameWidth));

        tempMat = mCountingSolution.findMask(inputFrame.rgba());
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(mRgba.submat(0 * mPreviewFrameHeight, 1 * mPreviewFrameHeight, 1 * mPreviewFrameWidth, 2 * mPreviewFrameWidth));

        Imgproc.blur(tempMat, tempMat, new Size(5, 5));
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(mRgba.submat(0 * mPreviewFrameHeight, 1 * mPreviewFrameHeight, 2 * mPreviewFrameWidth, 3 * mPreviewFrameWidth));

        Imgproc.threshold(tempMat, tempMat, mCountingSolution.mMaskThreshold, 255, Imgproc.THRESH_BINARY);
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(mRgba.submat(0 * mPreviewFrameHeight, 1 * mPreviewFrameHeight, 3 * mPreviewFrameWidth, 4 * mPreviewFrameWidth));

        Mat erodeKernel = Mat.ones(new Size(3, 3), CvType.CV_8U);
        Imgproc.erode(tempMat, tempMat, erodeKernel, new Point(-1, -1), 3);
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(mRgba.submat(1 * mPreviewFrameHeight, 2 * mPreviewFrameHeight, 0 * mPreviewFrameWidth, 1 * mPreviewFrameWidth));

        Imgproc.medianBlur(tempMat, tempMat, 7);
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        Imgproc.cvtColor(resizedTempMat, resizedTempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        resizedTempMat.copyTo(mRgba.submat(1 * mPreviewFrameHeight, 2 * mPreviewFrameHeight, 1 * mPreviewFrameWidth, 2 * mPreviewFrameWidth));

        List<MatOfPoint> contours = mCountingSolution.findContours(tempMat);
        Imgproc.cvtColor(tempMat, tempMat, Imgproc.COLOR_GRAY2BGRA, 4);
        Imgproc.drawContours(tempMat, contours, -1, new Scalar(0, 0, 255));
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        resizedTempMat.copyTo(mRgba.submat(1 * mPreviewFrameHeight, 2 * mPreviewFrameHeight, 2 * mPreviewFrameWidth, 3 * mPreviewFrameWidth));

        tempMat = inputFrame.rgba();
        List<Rect> boundingBoxes = mCountingSolution.findBoundingBoxes(contours);
        for (Rect boundingBox : boundingBoxes) {
            Imgproc.rectangle(tempMat, boundingBox.tl(), boundingBox.br(), new Scalar(0, 255, 0));
        }
        Imgproc.resize(tempMat, resizedTempMat, new Size(mPreviewFrameWidth, mPreviewFrameHeight));
        resizedTempMat.copyTo(mRgba.submat(1 * mPreviewFrameHeight, 2 * mPreviewFrameHeight, 3 * mPreviewFrameWidth, 4 * mPreviewFrameWidth));


        return mRgba;
    }

}
