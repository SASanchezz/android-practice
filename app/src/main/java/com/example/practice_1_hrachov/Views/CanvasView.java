package com.example.practice_1_hrachov.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.practice_1_hrachov.MainActivity;

public class CanvasView extends SurfaceView implements Runnable{

    private boolean mRunning;
    private Thread mGameThread = null;
    private Path mPath;

    private Context mContext;


    private Paint mPaint;
    private Bitmap mBitmap;
    private int mBitmapX;
    private int mBitmapY;
    private SurfaceHolder mSurfaceHolder;
    private float someCoord = -999999;
    private float lastPointX = someCoord;
    private float lastPointY = someCoord;
    private float radius = 25;
    private float lineRadius = 20;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(Color.RED);
    }

    /**
     * Runs in a separate thread.
     * All drawing happens here.
     */
    public void run() {

        Canvas canvas;

        while (mRunning) {
            // If we can obtain a valid drawing surface...
            if (!mSurfaceHolder.getSurface().isValid()) continue;

            canvas = mSurfaceHolder.lockCanvas();

            // Fill the canvas with white and draw the bitmap.
            canvas.save();
            canvas.drawColor(Color.RED);

            // Add clipping region and fill rest of the canvas with black.

            // The method clipPath(path, Region.Op.DIFFERENCE) was
            // deprecated in API level 26. The recommended alternative
            // method is clipOutPath(Path), which is currently available
            // in API level 26 and higher.
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                canvas.clipPath(mPath, Region.Op.DIFFERENCE);
            } else {
                canvas.clipOutPath(mPath);
            }
            canvas.drawColor(Color.WHITE);

            mPaint.setColor(Color.RED);
            canvas.drawRect(10,10, 200, 120, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawRect(220,10, 400, 120, mPaint);

            canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);

            canvas.restore();
            // Release the lock on the canvas and show the surface's
            // contents on the screen.
            mSurfaceHolder.unlockCanvasAndPost(canvas);

        }
    }


//    @Override
//    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        this.postInvalidate();
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        mBitmap = Bitmap.createBitmap(w, h, conf);
    }

    /**
     * Called by MainActivity.onPause() to stop the thread.
     */
    public void pause() {
        mRunning = false;
        try {
            // Stop the thread == rejoin the main thread.
            mGameThread.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (x > 10 && y> 10 && x < 200 && y < 120) {
                    pause();
                    ((MainActivity)getContext()).closeCanvasFragment();
                }
                if (x > 220 && y > 10 && x < 400 && y < 120) {
                    Toast.makeText(getContext(), "clean", Toast.LENGTH_SHORT).show();
                    mPath.rewind();
                    lastPointX = someCoord;
                    lastPointY = someCoord;
                    return true;
                }

                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.save();

                mPath.addCircle(x, y, radius, Path.Direction.CCW);

                if (lastPointX != someCoord) {
                    mPath.quadTo(lastPointX, lastPointY, x, y);
                    mPath.quadTo(lastPointX, lastPointY, x, y);
                }


                canvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(canvas);

                lastPointX = x;
                lastPointY = y;

                invalidate();
                break;
            default:
                // Do nothing.
        }
        return true;
    }
}
