package com.github.baserecycleradapter;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * desc:
 * author: ws
 * date: 2017/5/11.
 */

public class MakeComplaintsLayout extends FrameLayout {


    public MakeComplaintsLayout(@NonNull Context context) {
        this(context, null);
    }

    public MakeComplaintsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MakeComplaintsLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
