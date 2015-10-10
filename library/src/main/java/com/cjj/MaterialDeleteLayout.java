package com.cjj;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by cjj on 2015/9/21.
 */
public class MaterialDeleteLayout extends FrameLayout {
    private static final String TAG = "cjj_log";
    private ViewDragHelper mViewDragHelper;
    private View mRightHideView;
    private View mContentView;
    private float mFraction;
    private static final int MIN_FLING_VELOCITY = 400;
    private ParticleSystem particleSystem_1_5;
    private ParticleSystem particleSystem_2_5;
    private ParticleSystem particleSystem_3_5;
    private ParticleSystem particleSystem_4_5;
    private ParticleSystem particleSystem_5_5;
    private int[] location = new int[2];

    public MaterialDeleteLayout(Context context) {
        super(context);
        init();
    }

    public MaterialDeleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this,1.0f,new DragHelperCallbackListener());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mViewDragHelper.setMinVelocity(MIN_FLING_VELOCITY);
        Log.i(TAG, "cjj init");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),  MeasureSpec.getSize(heightMeasureSpec));
        View contentView = getChildAt(0);
        if(contentView == null)
        {
            throw new RuntimeException("you must set contentView");
        }
        Log.i(TAG,"cjj onmeasure");
        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        int contentWSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        int contentHSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWSpec, contentHSpec);
        mContentView = contentView;

        mRightHideView = getChildAt(1);
        if(mRightHideView == null)
        {
            throw new RuntimeException("you must set right hide view");
        }
        MarginLayoutParams lp2 = (MarginLayoutParams)
                mRightHideView.getLayoutParams();
        final int hideViewWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp2.leftMargin + lp2.rightMargin, lp2.width);
        final int hideViewHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp2.topMargin + lp2.bottomMargin, lp2.height);
        mRightHideView.measure(hideViewWidthSpec, hideViewHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        mContentView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + mContentView.getMeasuredWidth(), lp.topMargin + mContentView.getMeasuredHeight());
        lp = (MarginLayoutParams) mRightHideView.getLayoutParams();
        int rightHideWidth = mRightHideView.getMeasuredWidth();
        int childRight = rightHideWidth - (int) (rightHideWidth * mFraction);
        mRightHideView.layout(childRight, lp.topMargin, childRight + rightHideWidth, lp.topMargin + mRightHideView.getMeasuredHeight());
    }

    public class DragHelperCallbackListener extends ViewDragHelper.Callback
    {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mRightHideView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            int l = Math.max(0, Math.min(left, child.getWidth()));
            return l;
        }


        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId)
        {
            mViewDragHelper.captureChildView(mRightHideView, pointerId);
            startParticle();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel)
        {
            int childWidth = releasedChild.getWidth();
            float offset = 1.0f -( releasedChild.getLeft()) * 1.0f / childWidth;
            boolean flag = xvel <= 0  && offset > 0.3f;
            if(flag && deleteListener != null)
            {
                deleteListener.onDelete();
            }
            mViewDragHelper.settleCapturedViewAt(flag ? 0 : childWidth, releasedChild.getTop());
            stopParticle();
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
        {
            int w = changedView.getWidth();
            float fraction = (float) (w - left) / w;
            mFraction = fraction;
            changedView.setVisibility(fraction == 0 ? View.INVISIBLE : View.VISIBLE);
            mRightHideView.getLocationInWindow(location);
            updateParticle(left);
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child)
        {
            return mRightHideView == child ? 1 : 0;
        }
    }

    private void startParticle() {
        particleSystem_1_5 = new ParticleSystem((Activity) getContext(), 40, R.drawable.ic_partical, 1000);
        particleSystem_1_5.setAcceleration(0.00013f, 90)
                .setSpeedByComponentsRange(0f, 0.3f, 0.05f, 0.3f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(mRightHideView, Gravity.LEFT, 40);
        particleSystem_2_5 = new ParticleSystem((Activity) getContext(), 40, R.drawable.ic_partical, 1000);
        particleSystem_2_5.setAcceleration(0.00013f, 90)
                .setSpeedByComponentsRange(0f, 0.3f, 0.05f, 0.3f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(mRightHideView, Gravity.LEFT, 40);
        particleSystem_3_5 = new ParticleSystem((Activity) getContext(), 40, R.drawable.ic_partical, 1000);
        particleSystem_3_5.setAcceleration(0.00013f, 90)
                .setSpeedByComponentsRange(0f, 0.3f, 0.05f, 0.3f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(mRightHideView, Gravity.LEFT, 40);
        particleSystem_4_5 = new ParticleSystem((Activity) getContext(), 40, R.drawable.ic_partical, 1000);
        particleSystem_4_5.setAcceleration(0.00013f, 90)
                .setSpeedByComponentsRange(0f, 0.3f, 0.05f, 0.3f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(mRightHideView, Gravity.LEFT, 40);
        particleSystem_5_5 = new ParticleSystem((Activity) getContext(), 40, R.drawable.ic_partical, 1000);
        particleSystem_5_5.setAcceleration(0.00013f, 90)
                .setSpeedByComponentsRange(0f, 0.3f, 0.05f, 0.3f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(mRightHideView, Gravity.LEFT, 40);
    }

    public void updateParticle(int left)
    {
        particleSystem_1_5.updateEmitPoint(left, location[1] + mRightHideView.getHeight() * 1 / 5);
        particleSystem_5_5.updateEmitPoint(left, location[1] + mRightHideView.getHeight() * 1 / 5);
        particleSystem_2_5.updateEmitPoint(left, location[1] + mRightHideView.getHeight() * 2 / 5);
        particleSystem_3_5.updateEmitPoint(left, location[1] + mRightHideView.getHeight() * 3 / 5);
        particleSystem_4_5.updateEmitPoint(left, location[1] + mRightHideView.getHeight() * 4 / 5);
    }

    public void stopParticle()
    {
        particleSystem_1_5.stopEmitting();
        particleSystem_5_5.stopEmitting();
        particleSystem_2_5.stopEmitting();
        particleSystem_3_5.stopEmitting();
        particleSystem_4_5.stopEmitting();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll()
    {
        if (mViewDragHelper.continueSettling(true))
        {
            invalidate();
        }
    }

    public interface SwipeDeleteListener
    {
        public void onDelete();
    }

    private SwipeDeleteListener deleteListener;

    public void setDeleteListener( SwipeDeleteListener deleteListener)
    {
        this.deleteListener = deleteListener;
    }

    public void closeItem()
    {
        View view = mRightHideView;
        mFraction = 0.f;
        mViewDragHelper.smoothSlideViewTo(view, view.getWidth(), view.getTop());
    }
}
