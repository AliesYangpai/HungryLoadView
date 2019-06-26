package org.alie.hungryloadview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.alie.hungryloadview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alie on 2019/6/26.
 * 类描述
 * 版本
 */
public class HungryLoadView extends View {
    private static final String TAG = "HungryLoadView";
    private int mWidth;
    private int mHeight;


    /**
     * 影子画笔相关
     */
    private float mHalfShadowHeight;
    private float mHalfShadowWidth;
    private RectF mShadowRectF;
    private Paint mShadowPaint;

    private long mDuration = 700;
    private List<Bitmap> bitmaps;
    private Bitmap mCurrentBitmap;
    private int currentIndex;
    private float mPercent;



    private ValueAnimator mValueAnimator;
    private Matrix matrix;// 占位的matrix，无实际用处


    /**
     * 外部调用，添加变化的bitmap
     * @param id
     */
    public void addBitmap(int id) {
        if (null != bitmaps) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
            if (null != bitmap) {
                bitmaps.add(bitmap);
            }
        }
    }

    public void setDurationTime(long duration) {
        this.mDuration = duration;
    }
    private void initPaint(){
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(Color.LTGRAY);
        mShadowRectF = new RectF();
    }

    public HungryLoadView(Context context) {
        super(context);
        bitmaps = new ArrayList<>();
        initPaint();
        matrix = new Matrix();
    }

    public HungryLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmaps = new ArrayList<>();
        initPaint();
        matrix = new Matrix();
    }

    public HungryLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bitmaps = new ArrayList<>();
        initPaint();
        matrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mHalfShadowWidth = Math.max(16, getWidth() / 2f * mPercent);
        mHalfShadowHeight = Math.max(8, getHeight() / 8f * mPercent);
        mShadowRectF.set(getWidth() / 2f - mHalfShadowWidth,
                getHeight() * 7 / 8f - mHalfShadowHeight,
                getWidth() / 2f + mHalfShadowWidth,
                getHeight() * 7 / 8f + mHalfShadowHeight);
        canvas.drawOval(mShadowRectF, mShadowPaint);

        if(null != mCurrentBitmap) {
            canvas.save();
            float dx = (mWidth - mCurrentBitmap.getWidth()) / 2; // 让图片居中
            float dy = mHeight - mCurrentBitmap.getHeight();
            canvas.translate(dx, dy * mPercent);
            canvas.drawBitmap(mCurrentBitmap, matrix, null);
            canvas.restore();
        }
    }

    public void startAnimation() {
        if (null == mValueAnimator) {
            mValueAnimator = ValueAnimator.ofFloat(0, 1F, 0);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    if (animatedValue != mPercent) {
                        mPercent = animatedValue;
                        postInvalidate();
                    }
                }
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.i(TAG, "======onAnimationStart()=====");
                    if (null != bitmaps && bitmaps.size() > 0) {
                        mCurrentBitmap = bitmaps.get(currentIndex);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.i(TAG, "======onAnimationEnd()=====");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.i(TAG, "======onAnimationCancel()=====");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.i(TAG, "======onAnimationRepeat()=====");
                    currentIndex++;
                    if (currentIndex < bitmaps.size()) {
                        mCurrentBitmap = bitmaps.get(currentIndex);
                    }else {
                        currentIndex = 0;
                        mCurrentBitmap = bitmaps.get(currentIndex);
                    }
                }
            });
            mValueAnimator.setDuration(mDuration);
            mValueAnimator.start();
        }
    }

    private void stopAnimation() {
        if(null !=mValueAnimator) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }


    /**
     * 当前view与window脱离的时候，会被回调
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }
}
