package com.github.baserecycleradapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * desc: 吐槽控件文本视图
 * author: ws
 * date: 2017/5/3.
 */

public class PullExpandView extends LinearLayout {

    private ImageView imgArrow;
    private FrameTextView tvComp;
    private ImageView imgLeftActive;
    private ImageView imgRightActive;

    private boolean parentTopMoveEnable;

    public PullExpandView(Context context) {
        this(context, null);
    }

    public PullExpandView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullExpandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        addCompTextView();
    }

    private void addCompTextView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_view_comp, null, false);
        imgArrow = (ImageView) view.findViewById(R.id.arrow_img);
        tvComp = (FrameTextView) view.findViewById(R.id.comp_tv);
        imgLeftActive = (ImageView) view.findViewById(R.id.left_img);
        imgRightActive = (ImageView) view.findViewById(R.id.right_img);
        addView(view);
    }


    /**
     * @param width
     */
    public void setDefaultTextWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tvComp.getLayoutParams();
        lp.width = width;
        tvComp.setLayoutParams(lp);
    }


    /**
     * @return
     */
    public String getCompText() {
        return tvComp.getText().toString().replaceAll("\n", "");
    }

    public void setCompText(String text) {
        tvComp.setText(text);
    }


    /**
     * @param dx 水平位置的偏移量
     */
    public void setCompTextWidth(int dx, int parentWidth) {

        String content = tvComp.getText().toString();

        int width = tvComp.getWidth();

        final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tvComp.getLayoutParams();

        lp.width = width + dx;

        if (lp.width + imgArrow.getWidth() / 2 >= parentWidth) {
            lp.width = parentWidth - imgArrow.getWidth() / 2;
        }

        //注意宽度的区间  取最大值和最小值
        Rect rect = new Rect();
        tvComp.getPaint().getTextBounds(content, 0, content.length(), rect);
        //需要加上 padding 值
        // 1 左边边缘
        int minWidth = rect.height() + tvComp.getPaddingTop() + tvComp.getPaddingBottom();
        int maxWidth = rect.width() + tvComp.getPaddingLeft() + tvComp.getPaddingRight();

        if (lp.width <= minWidth) {
            lp.width = minWidth;
        } else if (lp.width >= maxWidth) {
            lp.width = maxWidth + dip2px(1);
        }

        tvComp.setLayoutParams(new FrameLayout.LayoutParams(lp.width, FrameLayout.LayoutParams.WRAP_CONTENT));

        post(new Runnable() {
            @Override
            public void run() {
                //处理快速滑动  超过边缘
                if (getX() <= 0) {
                    setX(0);
                    tvComp.setX(0);
                }

            }
        });
    }

    public void setDefaultTextWidth(String text) {
        if (text.length() > 15) {
            Rect rect = new Rect();
            tvComp.getPaint().getTextBounds(text, 0, 15, rect);
            int width = rect.width() + tvComp.getPaddingLeft() + tvComp.getPaddingRight();
            tvComp.setLayoutParams(new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT));
        }
        tvComp.setText(text);
    }

    /**
     * @param controlWidth
     * @param dx
     */
    public void setCompTextLocation(float controlWidth, int dx, int parentWidth) {
        if (controlWidth <= getWidth()) {
            setCompTextWidth(dx, parentWidth);
        } else {
            float locationX = getX() + dx;
            if (locationX <= 0) {
                locationX = 0;
                setCompTextWidth(dx, parentWidth);
            } else if (locationX + getWidth() >= controlWidth) {
                locationX = controlWidth - getWidth();
            }
            setX(locationX);
        }
    }

    /**
     * 是否隐藏上下活动区域
     *
     * @param visible
     */
    public void setVisibleActive(boolean visible) {
//        imgLeftActive.setVisibility(visible ? View.GONE : VISIBLE);
//        imgRightActive.setVisibility(visible ? View.GONE : VISIBLE);
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 是否隐藏箭头
     *
     * @param visible
     */
    public void setVisibleArrow(boolean visible) {
        //imgArrow.setVisibility(visible ? View.GONE : VISIBLE);
    }


    public boolean isParentTopMoveEnable() {
        return parentTopMoveEnable;
    }

    public void setParentTopMoveEnable(boolean parentTopMoveEnable) {
        this.parentTopMoveEnable = parentTopMoveEnable;
    }
}
