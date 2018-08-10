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
import java.util.concurrent.ArrayBlockingQueue;

public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = VideoSurfaceView.class.getSimpleName();
    private static final DecimalFormat FPS_FORMAT = new DecimalFormat("0.00");
    private static final int FPS_SAMPLE_SIZE = 50;
    final ArrayBlockingQueue<Bitmap> FRAME_BUFFER = new ArrayBlockingQueue<Bitmap>(100);//TODO reduce queue size
    LinkedList<Long> mFrameTimes = new LinkedList<Long>() {
        {
            this.add(System.nanoTime());
        }
    };
    int mDisplayedFrameCount = 0;
    private Thread mSurfaceThread;
    private boolean mSurfaceThreadRunning = false;
    private Paint mFPSPaint = new Paint() {
        {
            this.setColor(Color.BLUE);
            this.setTextSize(40);
        }
    };
    private volatile int mReceivedFrameCount = 0;

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    //Todo Implement pause and resume for thread

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
            Long startTime = System.currentTimeMillis();
            try {
                Bitmap nextFrame = this.FRAME_BUFFER.take();

                try {
                    //todo hardware lock canvas performance test
                    canvas = getHolder().lockCanvas();

                    if (canvas != null) {
                        String fps = FPS_FORMAT.format(getFps()) + " FPS";

                        synchronized (getHolder()) {
                            canvas.drawBitmap(nextFrame, 0, 0, null);
                            canvas.drawText(fps, 50, 50, mFPSPaint);
                            Log.v(TAG, "new frame displayed");
                            mDisplayedFrameCount++;
                            Log.d(TAG, "Received: " + mReceivedFrameCount + "\tDisplayed: " + mDisplayedFrameCount + "\tFrames Waiting to be Displayed: " + FRAME_BUFFER.size());
                        }
                    }
                } finally {
                    if (canvas != null) {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }

                if (System.currentTimeMillis() - startTime < 1000 / 10) { //limits to ~10 fps
                    try {
                        Thread.sleep(1000 / 10 - (System.currentTimeMillis() - startTime));
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Interrupted Exception when trying to sleep to limit fps");
                        Log.e(TAG, e.getStackTrace().toString());
                    }
                }

            } catch (InterruptedException e) {
                Log.e(TAG, "Interrupted Exception when trying to retrieve frame from buffer");
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
    }

    public void incrementFrameCount() {
        this.mReceivedFrameCount++;
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
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
    }

    private double getFps() {
        long currentTime = System.nanoTime();
        double timeElapsed = currentTime - mFrameTimes.getFirst();

        if (timeElapsed == 0) {
            Log.e(TAG, "Two frames displayed in same nanosecond??");
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
