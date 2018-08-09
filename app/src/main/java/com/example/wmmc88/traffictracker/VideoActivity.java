package com.example.wmmc88.traffictracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;

import static java.lang.Math.round;

public class VideoActivity extends AppCompatActivity implements Runnable {
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 2;
    private static final String TAG = VideoActivity.class.getSimpleName();

    private Thread mProcessingThread = null;
    private volatile boolean mProcessingThreadRunning = false;

    private VideoSurfaceView mVideoSurfaceView;

    private CountingSolution mCountingSolution;
    private VideoCapture mVC;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            Log.d(TAG, "onManagerConnected");
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    if (permissionsGranted()) {
                        if (mProcessingThreadRunning == false && mProcessingThread == null) {
                            mProcessingThreadRunning = true;
                            mProcessingThread = new Thread(VideoActivity.this, "Processing Thread");
                            mProcessingThread.start();
                        }
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
        setContentView(R.layout.activity_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //TODO read orientation from settings
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mVideoSurfaceView = findViewById(R.id.vsv);
    }


    @SuppressLint("InlinedApi")
    private boolean permissionsGranted() {
        Log.d(TAG, "checkPermissionsGranted");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "External Storage Permissions Granted!");
                    mProcessingThreadRunning = true;
                    mProcessingThread = new Thread(VideoActivity.this, "Processing Thread");
                    mProcessingThread.start();
                } else {
                    Log.w(TAG, "Permissions Denied!");
                    this.finish();
                }
                return;
            }

            //other permission request cases
        }
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
        destroyProcessingThread();
        super.onPause();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    //TODO multiple threads rnning
    public void run() {
        Log.i(TAG, "Processing Thread Started");
        loadVideo();
        mCountingSolution = new CountingSolution();

        android.graphics.Point size = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

//        int screenWidth = 2960;
//        int screenHeight = 1440;
        int previewFrameWidth = (int) round(screenWidth / 4.0);
        int previewFrameHeight = (int) round(screenHeight / 2.0);
        Size previewFrameSize = new Size(previewFrameWidth, previewFrameHeight);

        Mat inputFrame = new Mat();
        Mat rgb = Mat.zeros(screenHeight, screenWidth, CvType.CV_8UC3);
        Canvas canvas = new Canvas();

        while (mProcessingThreadRunning && mVC.grab() && mVC.retrieve(inputFrame)) {//TODO change retrieve to if statement
            Log.v(TAG, "new frame grabbed");

            mCountingSolution.findObjects(inputFrame.clone(), rgb = Mat.zeros(rgb.size(), rgb.type()), previewFrameSize);

            Bitmap bm = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bm);

            Log.v(TAG, "frameSent to SurfaceView Thread");
            mVideoSurfaceView.setNextBitmap(bm);
        }
        //TODO use handler finish activity from other thread
        finish();
    }

    private void loadVideo() {
        Log.d(TAG, "loadVideo");
        //TODO use decoder that converts to mjpg in avi container (only acceptable videotype in opencv 3.4.2)
        //TODO select video file using built in android app

        String filePath = "/storage/emulated/0/TrafficTracker/output.avi";
//        String filePath = "/storage/0000-0000/TrafficTracker/output.avi";
        mVC = new VideoCapture(filePath);
        if (mVC.isOpened()) {
            Log.i(TAG, "Video Opened!!");
        } else {
            Log.e(TAG, "Video at " + filePath + " could not be opened!");

            VideoActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(VideoActivity.this, "Video could not be opened!", Toast.LENGTH_LONG).show();
                }
            });
            finish();
        }
    }

    //call in on pause not on destroy
    private void destroyProcessingThread() {
        mProcessingThreadRunning = false;

        //Try to join thread with UI thread
        boolean retry = true;
        while (retry) {
            try {
                mProcessingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Interrupted Exception when waiting for surface thread to die");
                Log.e(TAG, e.toString());
            }
        }
    }

}


