package com.github.baserecycleradapter.radius;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class RadiusEditText extends AppCompatEditText {
    private RadiusViewDelegate delegate;

    public RadiusEditText(Context context) {
        this(context, null);
    }

    public RadiusEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new RadiusViewDelegate(this, context, attrs);
        setFocusableInTouchMode(true);
    }

    /**
     * use delegate to set attr
     */
    public RadiusViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.getWidthHeightEqualEnable() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.getRadiusHalfHeightEnable()) {
            delegate.setRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }
}
