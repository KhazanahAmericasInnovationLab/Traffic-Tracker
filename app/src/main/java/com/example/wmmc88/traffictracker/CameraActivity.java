package com.example.wmmc88.traffictracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static java.lang.Math.round;

public class CameraActivity extends AppCompatActivity implements CustomCameraView.CvCameraViewListener2 {
    public static final int CAMERA_PERMISSION_REQUEST = 1;
    private static final String TAG = CameraActivity.class.getSimpleName();

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "ERROR. COULD NOT LOAD STATIC OPENCV LIBRARIES!!!");
            //TODO Handler to finish activty
        } else {
//            Other OpenCV JNI libs should be here:
//            System.loadLibrary("my_jni_lib1");
//            System.loadLibrary("my_jni_lib2");
        }
    }

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgb;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mPreviewFrameWidth;
    private int mPreviewFrameHeight;


    private CountingSolution mCountingSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

    }

    private boolean permissionsGranted() {
        Log.d(TAG, "checkPermissionsGranted");

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
                    Log.i(TAG, "Camera Permissions Granted!");
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
        mOpenCvCameraView.setMaxFrameSize(640, 480);
        mOpenCvCameraView.enableView();
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        Log.d(TAG, "onCameraViewStarted");

        Toast.makeText(this, width + "x" + height, Toast.LENGTH_LONG).show();

//        mCountingSolution = new CountingSolution();

//        android.graphics.Point size = new android.graphics.Point();
//        getWindowManager().getDefaultDisplay().getSize(size);
//        mScreenWidth = size.x;
//        mScreenHeight = size.y;
        //TODO custom frame retrieval
        mScreenWidth = width;
        mScreenHeight = height;


        mPreviewFrameWidth = (int) round(mScreenWidth / 4.0);
        mPreviewFrameHeight = (int) round(mScreenHeight / 2.0);

        mRgb = Mat.zeros(mScreenHeight, mScreenWidth, CvType.CV_8UC3);

    }

    public void onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped");

        mRgb.release();
    }


    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.v(TAG, "onCameraFrame");
//TODO update
//        mCountingSolution.findObjects(inputFrame.rgba().clone(), mRgb, new Size(mPreviewFrameWidth, mPreviewFrameHeight));

        return mRgb;
    }

}
