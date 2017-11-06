package com.github.baserecycleradapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.baserecycleradapter.radius.RadiusLinearLayout;

/**
 * desc:
 * author: ws
 * date: 2017/5/10.
 */

public class FloatCompLayout extends LinearLayout {

    private TextView mRtvComp;

    //private RadiusTextView mRtvSign;

    private RadiusLinearLayout mRtvPublish;

    private RadiusLinearLayout mRtvClose;

    private ImageView mImgClose;

    private TextView mTvClose;

    private boolean mExpand = true;

    private boolean mClose = false;

    private int mScreenHeight = 0;

    private OnCompListener mListener;

    public FloatCompLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatCompLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatCompLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

        setListener();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.float_comp_layout, null, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT));
        mRtvComp = (TextView) view.findViewById(R.id.rv_comp);
        //mRtvSign = (RadiusTextView) view.findViewById(R.id.rv_sign);
        mImgClose = (ImageView) view.findViewById(R.id.img_close);
        mTvClose = (TextView) view.findViewById(R.id.tv_close);
        mRtvPublish = (RadiusLinearLayout) view.findViewById(R.id.rv_publish);
        mRtvClose = (RadiusLinearLayout) view.findViewById(R.id.rv_close);

        addView(view);
    }


    private void setListener() {
        mRtvComp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpand = !mExpand;
                if (mExpand) {
                    openAnimator();
                } else {
                    closeAnimator();
                }
            }
        });

        mRtvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mClose = !mClose;
                if (mListener != null) {
                    mListener.close();
                }
                mImgClose.setImageResource(mClose ? R.mipmap.ic_comp_close : R.mipmap.ic_comp_open);
                mTvClose.setText(mClose ? "开启" : "关闭");

                mRtvComp.setBackgroundResource(mClose ? R.mipmap.ic_comp_float_expand_lock_bg : R.mipmap
                        .ic_comp_float_expand_bg);
            }
        });

        mRtvPublish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpand = false;
                closeAnimator();

                if (mListener != null) {
                    mListener.publish(v);
                }
            }
        });
    }

    /**
     * 打开动画
     */
    public void openAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                mRtvClose.setY(mScreenHeight - dip2px(56) - value * dip2px(100));
                mRtvPublish.setY(mScreenHeight - dip2px(56) - value * dip2px(50));

                mRtvClose.setAlpha(value);
                mRtvPublish.setAlpha(value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mExpand) {
                    mRtvComp.setBackgroundResource(mClose ? R.mipmap.ic_comp_float_expand_lock_bg : R.mipmap
                            .ic_comp_float_expand_bg);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mExpand) {
                    closeAnimator();
                } else {
                    mRtvComp.setBackgroundResource(mClose ? R.mipmap.ic_comp_float_expand_lock_bg : R.mipmap
                            .ic_comp_float_expand_bg);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 关闭动画
     */
    public void closeAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        animator.setDuration(200);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                mRtvClose.setY(dip2px(100) * value + mScreenHeight - dip2px(156));
                mRtvPublish.setY(dip2px(50) * value + mScreenHeight - dip2px(106));

                mRtvClose.setAlpha(1.0f - value);
                mRtvPublish.setAlpha(1.0f - value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                mRtvComp.setBackgroundResource(mClose ? R.mipmap.ic_comp_float_close_lock_bg : R.mipmap
                        .ic_comp_float_close_bg);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    /**
     * 关闭悬浮菜单
     */
    public void closeFloatMenu() {
        mExpand = false;
        closeAnimator();
    }

    /**
     * @param expand 默认展开悬浮按钮
     */
    public void setExpand(boolean expand) {
        this.mExpand = expand;
    }

    public boolean getExpand() {
        return mExpand;
    }


    public interface OnCompListener {

        //关闭
        void close();

        //发表
        void publish(View v);

    }

    /**
     * @param listener
     */
    public void setOnCompListener(OnCompListener listener) {
        this.mListener = listener;
    }


    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
