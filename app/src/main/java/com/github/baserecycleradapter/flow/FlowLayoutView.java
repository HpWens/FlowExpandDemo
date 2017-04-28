package com.github.baserecycleradapter.flow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.radius.RadiusTextView;
import com.github.baserecycleradapter.radius.RadiusViewDelegate;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * desc:
 * author: ws
 * date: 2017/4/20.
 */

public class FlowLayoutView extends ViewGroup {

    //第一行个数
    private int firstLineCount = 0;

    //首 view 宽度
    private int firstViewWidth = 0;

    //首 view 高度
    private int firstViewHeight = 0;

    //第一行高度
    private int firstLineHeight = 0;

    //展开 整个内容的高度
    private int contentHeight = 0;

    //底部 view
    private ViewGroup bottomCollapseLayout;

    // 展开 view
    private View firstView;

    //加载第一行 用于记录第一行数
    private boolean mFirstHeight = true;

    //private boolean mMultiBuild = false;
    private OnItemClickListener mListener;

    //动画运动时间
    private long mDuration = DEFAULT_ANIM_DURATION;

    //是否支持多选
    private boolean multiSelection = false;

    private boolean mHideBottom = true;

    //childs
    private SparseArray<View> mChildViews = new SparseArray<>();

    private int mSelectionPosition = -1;

    private boolean mReverseSel = false; //是否支付反选

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    //tag
    private static final String TAG = "tag";

    public FlowLayoutView(@NonNull Context context) {
        this(context, null);
    }

    public FlowLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        buildMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        buildLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null && !adapter.isEmpty()) {
            //初始化数据
            mChildViews = new SparseArray<>();
            mSelectionPosition = -1;
            setVisibility(View.INVISIBLE);
            mFirstHeight = true;
            mHideBottom = true;
            buildTagItems(adapter);
            collapse();
        }
    }

    //构建标签列表项
    private void buildTagItems(ListAdapter adapter) {
        //移除所有控件
        removeAllViews();
        //添加首view
        // addFirstView();
        for (int i = 0; i < adapter.getCount(); i++) {
            final View itemView = adapter.getView(i, null, this);
            final int position = i;
            if (itemView != null) {
                if (i == 0) {
                    firstView = itemView;
                    itemView.setVisibility(View.INVISIBLE);
                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //展开
                            expand();
                        }
                    });
                } else {
                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onClick(v, position);
                            }
                        }
                    });
                }
                itemView.setTag(TAG + i);
                mChildViews.put(i, itemView);
                addView(itemView);
            }
        }
        //添加底部收起试图
        addBottomView();
    }

    //添加下拉view
    private void addFirstView() {
        LinearLayout firstLayout = new LinearLayout(getContext());
        firstLayout.setOrientation(LinearLayout.HORIZONTAL);
        RadiusTextView rtv = new RadiusTextView(getContext());
        firstLayout.addView(rtv);

        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        int margin = dip2px(getContext(), 8);
        int padding = dip2px(getContext(), 14);
        lps.setMargins(margin, margin, margin, margin);
        rtv.setPadding(padding, dip2px(getContext(), 7), padding, dip2px(getContext(), 7));
        rtv.setLayoutParams(lps);
        firstLayout.setGravity(Gravity.CENTER);
        firstLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        RadiusViewDelegate rvd = rtv.getDelegate();
        rvd.setEadiusHalfHeightEnable(true);
        rvd.setBackgroundColor(Color.parseColor("#FFFFFF"));
        rvd.setStrokeWidth(1);
        rvd.setStrokeColor(Color.parseColor("#DADADA"));

        Drawable drawable = getResources().getDrawable(R.mipmap.ic_widget_expand);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rtv.setCompoundDrawables(drawable, null, null, null);

        firstLayout.setVisibility(View.INVISIBLE);
        addView(firstView = firstLayout);

        firstLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //展开
                expand();
            }
        });
    }

    /**
     * 添加底部收起试图
     */
    private void addBottomView() {
        RelativeLayout bottomLayout = new RelativeLayout(getContext());
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, dip2px(getContext(), 40));
        lp.topMargin = dip2px(getContext(), 8);
        bottomLayout.setLayoutParams(lp);

        View view = new View(getContext());
        view.setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 0.5f)));
        view.setBackgroundColor(Color.parseColor("#d9d9d9"));
        bottomLayout.addView(view);

        final ImageView imgCollapse = new ImageView(getContext());
        bottomLayout.addView(imgCollapse);

        ((RelativeLayout.LayoutParams) imgCollapse.getLayoutParams()).addRule(CENTER_IN_PARENT);
        imgCollapse.setImageResource(R.mipmap.ic_widget_collapse);

        addView(bottomCollapseLayout = bottomLayout);

        bottomLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(mDuration);
            }
        });

    }

    /**
     * 展开
     */
    private void expand() {
        ValueAnimator animator = ValueAnimator.ofInt(firstLineHeight, contentHeight);
        animator.setDuration(mDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                getLayoutParams().height = value;
                requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mListener != null) {
                    mListener.showMask();
                }
                firstView.setVisibility(View.INVISIBLE);
                bottomCollapseLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
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
     * 设置展开，收起运动的时间
     *
     * @param duration
     */
    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public int getFirstLineHeight() {
        return firstLineHeight;
    }

    /**
     * 收起，只显示第一行
     */
    private void collapse() {
        post(new Runnable() {
            @Override
            public void run() {
                collapse(0);
            }
        });
    }

    /**
     * 是否支持反选
     *
     * @param reverse
     */
    public void setReverseSelect(boolean reverse) {
        this.mReverseSel = reverse;
    }

    /**
     * 判定是否处于展开
     *
     * @return
     */
    public boolean isOpen() {
        if (firstLineHeight > 0 && contentHeight > 0 && bottomCollapseLayout != null && bottomCollapseLayout
                .getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    /**
     * 关闭控件
     */
    public void close() {
        collapse(mDuration);
    }

    /**
     * 展开控件
     */
    public void open() {
        expand();
    }

    /**
     * 是否支持多选
     *
     * @param multi
     */
    public void setMultiSelection(boolean multi) {
        multiSelection = multi;
    }

    /**
     * @return
     */
    private SparseArray<View> getViews() {
        return mChildViews == null ? null : mChildViews;
    }

    /**
     * @param position
     */
    public void setSelection(int position) {
        if (mChildViews == null) return;

        if (position == mSelectionPosition) { //支持反选
            if (mReverseSel && !multiSelection) {
                if (mListener != null) {
                    mListener.onUnSelected(mChildViews.get(mSelectionPosition), mReverseSel);
                }
                mSelectionPosition = -1;
            }
            return;
        }

        if (mSelectionPosition != -1) {
            if (!multiSelection) {//默认单选
                if (mListener != null) {
                    mListener.onUnSelected(mChildViews.get(mSelectionPosition), mReverseSel);
                }
            }
        }

        if (mListener != null) {
            mListener.onSelected(mChildViews.get(position));
        }

        mSelectionPosition = position;
    }

    /**
     * 清空选中状态
     */
    public void clearSelection() {
        if (mChildViews == null) return;
        for (int i = 0; i < mChildViews.size(); i++) {
            View itemView = mChildViews.get(i);
            if (mListener != null) {
                mListener.onUnSelected(itemView, false);
            }
        }
    }

    /**
     * 收缩
     */
    private void collapse(long time) {
        ValueAnimator animator = ValueAnimator.ofInt(contentHeight - bottomCollapseLayout.getHeight(), firstLineHeight);
        animator.setDuration(time);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                getLayoutParams().height = value;
                requestLayout();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                bottomCollapseLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                firstView.setVisibility(mHideBottom ? View.GONE : View.VISIBLE);
                if (mListener != null) {
                    mListener.dismissMask();
                }
                if (getVisibility() == View.INVISIBLE) {
                    setVisibility(View.VISIBLE);
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
     * 布局
     */
    private void buildLayout() {
        int count = getChildCount();
        int lineWidth = 0;//累加当前行的行宽
        int lineHeight = 0;//当前行的行高
        int top = 0, left = 0;//当前坐标的top坐标和left坐标

        int parentWidth = getMeasuredWidth(); //父控件的宽度

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (i == 0) {
                child.layout(parentWidth - lp.rightMargin - child.getMeasuredWidth(), lp.topMargin, parentWidth - lp
                        .rightMargin, lp.topMargin + child.getMeasuredHeight());
                firstViewWidth = childWidth;
                firstViewHeight = childHeight;
                continue;
            }

            if (firstLineCount == (i + 1)) {
                lineWidth += firstViewWidth;
                lineHeight = Math.max(lineHeight, firstViewHeight);
            }

            if (childWidth + lineWidth > getMeasuredWidth()) {
                //如果换行
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }
            //计算childView的left,top,right,bottom
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;

            int rc = lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();

            child.layout(lc, tc, rc, bc);
            //将left置为下一子控件的起始点
            left += childWidth;
        }
        if (mFirstHeight) {
            contentHeight = getHeight();
            mFirstHeight = false;
            if (mListener != null) {
                mListener.onFirstLineHeight(firstLineHeight);
            }
        }
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void buildMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int lineWidth = 0; //记录每行的宽度
        int lineHeight = 0; //记录每行的高度
        int height = 0; //布局高度
        int width = 0; //布局宽度
        int count = getChildCount(); //所有子控件数量
        boolean firstLine = true; //是否处于第一行
        firstLineCount = 0; //第一行子 View 个数

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childWidth = 0;
            int childHeight = 0;

            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            } else if (child.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }

            if (lineWidth + childWidth > measureWidth) {
                //需要换行
                width = Math.max(lineWidth, width);
                height += lineHeight;
                //因为由于盛不下当前控件，而将此控件调到下一行，所以将此控件的高度和宽度初始化给lineHeight、lineWidth
                lineHeight = childHeight;
                lineWidth = childWidth;

                firstLine = false;

                if (i != firstLineCount) {
                    mHideBottom = false;
                }

            } else {
                // 否则累加值lineWidth,lineHeight取最大高度
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;

                if (firstLine) {
                    firstLineCount++;
                    firstLineHeight = lineHeight;
                }
            }

            //最后一行是不会超出width范围的，所以要单独处理
            if (i == count - 1) {
                height += lineHeight;
                width = Math.max(width, lineWidth);

                if (firstLine) {
                    firstLineCount = 1;
                }
            }
        }

        if (mFirstHeight) {
            measureHeight = height;
        }
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width, (measureHeightMode ==
                MeasureSpec.EXACTLY) ? measureHeight : height);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams lp) {
        return new MarginLayoutParams(lp);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void onClick(View v, int position);

        void onSelected(View v);

        void onUnSelected(View v, boolean reverseEnable);

        void showMask();

        void dismissMask();


        boolean onFirstLineHeight(int height);

    }

    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
