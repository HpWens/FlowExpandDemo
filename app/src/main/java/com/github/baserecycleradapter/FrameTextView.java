package com.github.baserecycleradapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * desc: 吐槽文本控件
 * author: ws
 * date: 2017/5/3.
 */

public class FrameTextView extends AppCompatTextView {

    Paint mPaint;

    int mBlockRadius;

    String mText = "";

    boolean mDrawSlide;//是否绘制边框

    public FrameTextView(Context context) {
        this(context, null);
    }

    public FrameTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#E2472C"));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dip2px(1));
        mPaint.setStyle(Paint.Style.STROKE);

        mBlockRadius = dip2px(4);

        setGravity(Gravity.LEFT);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //绘制矩形
        if (mDrawSlide) {
            mPaint.setColor(Color.parseColor("#E2472C"));
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(new RectF(mBlockRadius / 2, mBlockRadius / 2, getWidth() - mBlockRadius / 2,
                    getHeight()
                    - mBlockRadius / 2), mBlockRadius / 2, mBlockRadius / 2, mPaint);

            //上 中心点
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(getWidth() / 2 - mBlockRadius / 2, 0, getWidth() / 2 + mBlockRadius / 2, mBlockRadius,
                    mPaint);
            //下中心点
            canvas.drawRect(getWidth() / 2 - mBlockRadius / 2, getHeight() - mBlockRadius, getWidth() / 2 +
                    mBlockRadius / 2, getHeight(), mPaint);
            //左
            canvas.drawRect(0, getHeight() / 2 - mBlockRadius / 2, mBlockRadius, getHeight() / 2 + mBlockRadius / 2,
                    mPaint);
            //右
            canvas.drawRect(getWidth() - mBlockRadius, getHeight() / 2 - mBlockRadius / 2, getWidth(), getHeight() / 2 +
                    mBlockRadius / 2, mPaint);
        }

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#CFEEEBC0"));
        canvas.drawRoundRect(new RectF(mBlockRadius / 2, mBlockRadius / 2, getWidth() - mBlockRadius / 2,
                getHeight() -
                        mBlockRadius / 2), mBlockRadius / 2, mBlockRadius / 2, mPaint);

        super.onDraw(canvas);
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public boolean isDrawSlide() {
        return mDrawSlide;
    }

    public void setDrawSlide(boolean drawSlide) {
        mDrawSlide = drawSlide;
    }
}
