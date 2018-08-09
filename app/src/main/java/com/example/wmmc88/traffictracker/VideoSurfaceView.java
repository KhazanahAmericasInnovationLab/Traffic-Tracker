package com.example.wmmc88.traffictracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = VideoSurfaceView.class.getSimpleName();
    private static final DecimalFormat FPS_FORMAT = new DecimalFormat("0.00");
    private final int FPS_SAMPLE_SIZE = 50;
    LinkedList<Long> mFrameTimes = new LinkedList<Long>() {
        {
            this.add(System.nanoTime());
        }
    };

    private Bitmap mLastBitmap = null;
    private volatile Bitmap mNextBitmap = null;
    private Thread mSurfaceThread;
    private boolean mSurfaceThreadRunning = false;
    private Paint mFPSPaint = new Paint() {
        {
            this.setColor(Color.BLUE);
            this.setTextSize(40);
        }
    };
    z
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

    //Todo Implement pause and resume for thread

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

                try {
                    //todo hardware lock canvas
                    canvas = getHolder().lockCanvas();

                    if (canvas != null) {
                        //TODO Currently it is the frames processed and displayed per second. doesnt actually correspond to the fps of the video
                        String fps = FPS_FORMAT.format(getFps()) + " FPS";

                        synchronized (getHolder()) {
                            canvas.drawBitmap(mLastBitmap, 0, 0, null);
                            canvas.drawText(fps, 50, 50, mFPSPaint);
                            Log.v(TAG, "new frame displayed");
                        }
                    }
                } finally {
                    if (canvas != null) {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }
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

    public void setNextBitmap(Bitmap nextBitmap) {
        Log.v(TAG, "New Bitmap Received for Rendering");

        this.mNextBitmap = nextBitmap;
    }

    private double getFps() {
        long currentTime = System.nanoTime();
        double timeElapsed = currentTime - mFrameTimes.getFirst();

        if (timeElapsed == 0) {
            Log.e(TAG, "Two frames displayed in same nanoscond??");
            return -1;
        }

        mFrameTimes.addLast(currentTime);
        double fps = mFrameTimes.size() / timeElapsed * 1000000000;

        if (mFrameTimes.size() > FPS_SAMPLE_SIZE) {
            mFrameTimes.removeFirst();
        }

        return fps;
    }

}
