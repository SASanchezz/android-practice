package com.example.practice_1_hrachov.Views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.practice_1_hrachov.MainActivity;

public class AnimationsView extends View {

    private static final int ANIMATION_DURATION = 1000;
    private static final long ANIMATION_DELAY = 200;
    private static final int COLOR_ADJUSTER = 5;

    private float mX;
    private float mY;

    private float mRadius;
    private float mYLocation = -100;
    private float mYLocation2 = -100;
    private final Paint mPaint = new Paint();


    private AnimatorSet mPulseAnimatorSet = new AnimatorSet();

    public AnimationsView(Context context) {
        super(context);
        mPaint.setTextSize(55);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }
    public AnimationsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        // This method is called when the size of the view changes.
        // For this app, it is only called when the activity is started or restarted.
        // getWidth() cannot return anything valid in onCreate(), but it does here.
        // We create the animators and animator set here once, and handle the starting and
        // canceling in the event handler.

        // Animate the "radius" property with an ObjectAnimator,
        // giving it an interpolator and duration.
        // This animator creates an increasingly larger circle from a
        // radius of 0 to the width of the view.
        ObjectAnimator growAnimator = ObjectAnimator.ofFloat(this,
                "radius", 0, getWidth() / 3);
        growAnimator.setDuration(ANIMATION_DURATION);
        growAnimator.setInterpolator(new LinearInterpolator());

        // Create a second animator to
        // animate the "radius" property with an ObjectAnimator,
        // giving it an interpolator and duration.
        // This animator creates a shrinking circle
        // from a radius of the view's width to 0.
        // Add a delay to starting the animation.
        ObjectAnimator shrinkAnimator = ObjectAnimator.ofFloat(this,
                "radius", getWidth() / 3, 0);
        shrinkAnimator.setDuration(ANIMATION_DURATION);
        shrinkAnimator.setInterpolator(new LinearInterpolator());
        shrinkAnimator.setStartDelay(ANIMATION_DELAY);


        ObjectAnimator textAnimator = ObjectAnimator.ofFloat(this,
                "YLocation", 0, getHeight()-10);
        textAnimator.setDuration(ANIMATION_DURATION*2);
        textAnimator.setInterpolator(new LinearInterpolator());
        textAnimator.setStartDelay(ANIMATION_DELAY);

        ObjectAnimator textAnimator2 = ObjectAnimator.ofFloat(this,
                "YLocation2", 0, getHeight()-70);
        textAnimator2.setDuration(ANIMATION_DURATION*2);
        textAnimator2.setInterpolator(new LinearInterpolator());
        textAnimator2.setStartDelay(ANIMATION_DELAY);
        // If you don't need a delay between the two animations,
        // you can use one animator that repeats in reverse.
        // Uses the default AccelerateDecelerateInterpolator.
//        ObjectAnimator repeatAnimator = ObjectAnimator.ofFloat(this,
//                "radius", 0, getWidth());
//        repeatAnimator.setStartDelay(ANIMATION_DELAY);
//        repeatAnimator.setDuration(ANIMATION_DURATION);
//        repeatAnimator.setRepeatCount(1);
//        repeatAnimator.setRepeatMode(ValueAnimator.REVERSE);

        // Create an AnimatorSet to combine the two animations into a sequence.
        // Play the expanding circle, wait, then play the shrinking circle.
        mPulseAnimatorSet.play(growAnimator).before(shrinkAnimator);
        mPulseAnimatorSet.play(textAnimator).after(shrinkAnimator);
        mPulseAnimatorSet.play(textAnimator2).after(textAnimator);
        //mPulseAnimatorSet.play(repeatAnimator).after(shrinkAnimator);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            // Where the center of the circle will be.
            mX = event.getX();
            mY = event.getY();
            if (mX > 10 && mY> 10 && mX < 200 && mY < 120) {
                ((MainActivity)getContext()).closeAnimationFragment();
            }

            // If there is an animation running, cancel it.
            // This resets the AnimatorSet and its animations to the starting values.
            if(mPulseAnimatorSet != null && mPulseAnimatorSet.isRunning()) {
                mPulseAnimatorSet.cancel();
            }
            // Start the animation sequence.
            mPulseAnimatorSet.start();
        }
        return super.onTouchEvent(event);
    }


    public void setRadius(float radius) {
        mRadius = radius;
        // Calculate a new color from the radius.
        mPaint.setColor(Color.MAGENTA + (int) radius * 3 / COLOR_ADJUSTER);
        // Updating the property does not automatically redraw.
        invalidate();
    }

    public void setYLocation(float YLocation) {
        mYLocation = YLocation;
        // Calculate a new color from the radius.
        invalidate();
    }

    public void setYLocation2(float YLocation2) {
        mYLocation2 = YLocation2;
        // Calculate a new color from the radius.
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawRect(10,10, 200, 120, mPaint);

        canvas.drawCircle(mX, mY, mRadius, mPaint);
        canvas.drawText("Слава Україні!", getWidth()/2, mYLocation, mPaint);
        canvas.drawText("Героям слава!", getWidth()/2, mYLocation2, mPaint);
    }


}
