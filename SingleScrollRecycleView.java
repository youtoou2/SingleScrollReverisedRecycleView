package com.cwb.glance.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.cwb.glance.util.AppLog;

/**
 * Created by youtoolaw on 6/1/16.
 */
public class SingleScrollRecycleView extends RecyclerView {
    private boolean mSingleScroll = false;


    final private int mMinDistanceMovedY = 20;
    private VelocityTracker mVelocity = null;
    final private float mEscapeVelocity = 2000.0f;
    private float mStartY = 0;


    final private int mMinDistanceMovedX = 20;
    private VelocityTracker mVelocitx = null;
    final private float mEscapeVelocitx = 2000.0f;
    private float mStartX = 0;

    public SingleScrollRecycleView(Context context)
    {
        super(context);
    }

    public SingleScrollRecycleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SingleScrollRecycleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setSingleScroll(boolean aSingleScroll) { mSingleScroll = aSingleScroll; }

    @Override
    public boolean dispatchTouchEvent(MotionEvent aMotionEvent) {
        if (((LinearLayoutManager) this.getLayoutManager()).getOrientation() == VERTICAL) {
            if (aMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (mSingleScroll && mVelocity == null)
                    mVelocity = VelocityTracker.obtain();
                mStartY = aMotionEvent.getY();
                return super.dispatchTouchEvent(aMotionEvent);
            }

            if (aMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (mVelocity != null) {
                    if (Math.abs(aMotionEvent.getY() - mStartY) > mMinDistanceMovedY) {
                        mVelocity.computeCurrentVelocity(1000);
                        float velocity = mVelocity.getYVelocity();

                        if (aMotionEvent.getY() > mStartY) {
                            // always lock
                            if (velocity > mEscapeVelocity)
                                smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                            else {
                                // lock if over half way there
                                View view = getChildAt(0);
                                if (view != null) {
                                    if (view.getBottom() >= getHeight() / 2)
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                                    else
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                                }
                            }
                        } else {
                            if (velocity < -mEscapeVelocity)
                                smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                            else {
                                // lock if over half way there
                                View view = getChildAt(1);
                                if (view != null) {
                                    if (view.getTop() <= getHeight() / 2)
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                                    else
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                                }
                            }
                        }
                    }
                    mVelocity.recycle();
                }
                mVelocity = null;

                if (mSingleScroll) {
                    if (Math.abs(aMotionEvent.getY() - mStartY) > mMinDistanceMovedY)
                        return super.dispatchTouchEvent(aMotionEvent);
                } else
                    return super.dispatchTouchEvent(aMotionEvent);
            }

            if (mSingleScroll) {
                if (mVelocity == null) {
                    mVelocity = VelocityTracker.obtain();
                    mStartY = aMotionEvent.getY();
                }
                mVelocity.addMovement(aMotionEvent);
            }

            return super.dispatchTouchEvent(aMotionEvent);
        } else { 
        ///////////////HORIZONTAL
            if (aMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (mSingleScroll && mVelocitx == null)
                    mVelocitx = VelocityTracker.obtain();
                mStartX = aMotionEvent.getX();
                return super.dispatchTouchEvent(aMotionEvent);
            }

            if (aMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (mVelocitx != null) {
                    if (Math.abs(aMotionEvent.getX() - mStartX) > mMinDistanceMovedX) {
                        mVelocitx.computeCurrentVelocity(1000);
                        float velocity = mVelocitx.getXVelocity();
                        AppLog.d("velocity X = "+velocity);

                        if (aMotionEvent.getX() > mStartX) {
                            // always lock
                            if (velocity > mEscapeVelocitx) {
                                smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                            }else {
                                // lock if over half way there
                                View view = getChildAt(1);
                                if (view != null) {
                                    if (view.getLeft() >= getWidth() *0.4)
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                                    else
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                                }
                            }
                        } else {
                            if (velocity < -mEscapeVelocitx) {
                                smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                            }else {
                                // lock if over half way there
                                View view = getChildAt(0);
                                if (view != null) {
                                    if (view.getRight() <= getWidth() *0.4)
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition());
                                    else
                                        smoothScrollToPosition(((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition());
                                }
                            }
                        }
                    }
                    mVelocitx.recycle();
                }
                mVelocitx = null;

                if (mSingleScroll) {
                    if (Math.abs(aMotionEvent.getX() - mStartX) > mMinDistanceMovedX)
                        return super.dispatchTouchEvent(aMotionEvent);
                } else
                    return super.dispatchTouchEvent(aMotionEvent);
            }

            if (mSingleScroll) {
                if (mVelocitx == null) {
                    mVelocitx = VelocityTracker.obtain();
                    mStartX = aMotionEvent.getX();
                }
                mVelocitx.addMovement(aMotionEvent);
            }

            return super.dispatchTouchEvent(aMotionEvent);
        }
    }
}
