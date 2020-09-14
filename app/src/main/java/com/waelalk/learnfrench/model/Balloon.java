package com.waelalk.learnfrench.model;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.waelalk.learnfrench.R;
import com.waelalk.learnfrench.helper.PixelHelper;

/**
 * Created by Rohan on 21-May-17.
 */

public class Balloon extends androidx.appcompat.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {


    public Balloon(Context context) {
        super(context);
    }

    public Balloon(Context context, int color, int rawHeight) {
        super(context);



        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);

        int rawWidth = rawHeight / 2;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        this.setLayoutParams(params);

    }

    public void releaseBalloon(int screenHeight, int duration) {

        ValueAnimator mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }


    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY((Float) valueAnimator.getAnimatedValue());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!mPopped && event.getAction() == MotionEvent.ACTION_DOWN) {
//            mListener.popBalloon(this, true);
//            mPopped = true;
//            mAnimator.cancel();
//        }
//
////        return super.onTouchEvent(event);
//        return true;
//    }




}
