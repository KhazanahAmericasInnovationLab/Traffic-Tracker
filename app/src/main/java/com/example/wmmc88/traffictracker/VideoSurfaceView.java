package com.example.wmmc88.traffictracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = VideoSurfaceView.class.getSimpleName();
    private Bitmap mLastBitmap = null;
    private volatile Bitmap mNextBitmap = null;
    private volatile int mReceivedFrameCount = 0;
    private int mDisplayedFrameCount = 0;

    private Thread mSurfaceThread;
    private boolean mSurfaceThreadRunning = false;

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        mSurfaceThreadRunning = true;
        mSurfaceThread = new Thread(this, "Surface Thread");
        mSurfaceThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed");

        destroySurfaceThread();
    }

    @Override
    public void run() {
        Log.d(TAG, "Surface Thread Started");

        Canvas canvas = null;

        while (mSurfaceThreadRunning) {
            if (this.mNextBitmap != this.mLastBitmap) {
                this.mLastBitmap = this.mNextBitmap;

                long startTime = System.currentTimeMillis();

                try {
                    //todo hardware lock canvas
                    canvas = getHolder().lockCanvas();

                    if (canvas != null) {

                        synchronized (getHolder()) {
                            canvas.drawBitmap(mLastBitmap, 0, 0, null);
                            Log.v(TAG, "new frame displayed");
                            mDisplayedFrameCount++;
                            Log.e(TAG, "Received: " + mReceivedFrameCount + "\tDisplayed: " + mDisplayedFrameCount + "\tFrames Dropped: " + (mReceivedFrameCount - mDisplayedFrameCount));
                        }
                    }
                } finally {
                    if (canvas != null) {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }

//TODO framerate
//                long frameTime = System.currentTimeMillis() - startTime;
//                if (frameTime < 1000 / 30)
//                    try {
//                        Thread.sleep(1000 / 30 - frameTime);
//                    } catch (InterruptedException e) {
//                    }
            }
        }
    }

    private void destroySurfaceThread() {
        Log.d(TAG, "destroySurfaceThread Called");

        mSurfaceThreadRunning = false;

        //Try to join thread with UI thread
        boolean retry = true;
        while (retry) {
            try {
                mSurfaceThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Interrupted Exception when waiting for surface thread to die");
                Log.e(TAG, e.toString());
            }
        }
    }

    //Todo Implement pause and resume for thread


    public void setNextBitmap(Bitmap nextBitmap) {
        Log.v(TAG, "New Bitmap Received for Rendering");

        this.mNextBitmap = nextBitmap;
    }

    public void incrementFrameCount() {
        this.mReceivedFrameCount++;
    }
}
