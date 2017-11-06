package com.github.baserecycleradapter.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.baserecycleradapter.EditDialog;
import com.github.baserecycleradapter.FloatCompLayout;
import com.github.baserecycleradapter.MakeComplaintsView;
import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.radius.RadiusEditText;
import com.github.baserecycleradapter.radius.RadiusTextView;
import com.github.baserecycleradapter.utils.KeyBoardUtil;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * desc:
 * author: ws
 * date: 2017/4/30.
 */

public class UnderActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    TextView mTvUnder;

    FloatCompLayout fcl;

    MakeComplaintsView mcv;

    LinearLayout mEditLayout;

    RadiusEditText mEditView;

    FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("UnderActivity", "onCreate--------");

        setContentView(R.layout.activity_under);
        mTvUnder = (TextView) findViewById(R.id.tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        fcl = (FloatCompLayout) findViewById(R.id.fcl);
        mcv = (MakeComplaintsView) findViewById(R.id.mcv);
        mEditLayout = (LinearLayout) findViewById(R.id.edit_layout);
        mEditView = (RadiusEditText) findViewById(R.id.ret_view);
        mFrameLayout = (FrameLayout) findViewById(R.id.fl);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditDialog dialog= new EditDialog(UnderActivity.this);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

                final RadiusTextView rtv = new RadiusTextView(v.getContext());
                rtv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                rtv.setTextSize(COMPLEX_UNIT_PX,28);
                rtv.getDelegate().setBackgroundColor(Color.parseColor("#CFEEEBC0"));
                rtv.getDelegate().setRadius(4);
                rtv.setText("hello word" + mFrameLayout.getChildCount());

                mFrameLayout.addView(rtv);

                rtv.setX(new Random().nextInt(500) + 200);
                rtv.setY(new Random().nextInt(1200) + 200);

                ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                animator.setDuration(1000);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rtv.setAlpha(0.8f);

                        if (mFrameLayout.getChildCount() > 10) {
                            mFrameLayout.removeViewAt(0);
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
        });

        fcl.closeFloatMenu();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_item, getDatas()) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View view = recyclerView.findChildViewUnder(mTvUnder.getWidth() / 2, mTvUnder.getMeasuredHeight()
                        + 1);

                if (view != null) {
                    TextView tv = (TextView) view.findViewById(R.id.tv_item_text);
                    if (tv != null) {
                        Log.e("UnderActivity", "onScrolled--------" + tv.getText());
                    }
                }
            }
        });


        fcl.setOnCompListener(new FloatCompLayout.OnCompListener() {
            @Override
            public void close() {

            }

            @Override
            public void publish(View v) {
                mEditLayout.setVisibility(View.VISIBLE);
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardUtil.getInstance(UnderActivity.this).show(mEditView);
                    }
                });
            }
        });


        mEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = mEditView.getText().toString();
                if (content.length() > 1) {
                    KeyBoardUtil.getInstance(UnderActivity.this).hide();

                    mEditLayout.setVisibility(View.GONE);

                    mcv.setVisibility(View.VISIBLE);

                    mcv.setCompText(content.replaceAll("\n", ""));
                }
                return false;
            }
        });

        mcv.setOnCompClickListener(new MakeComplaintsView.OnCompClickListener() {
            @Override
            public void onCancel(View v) {
                mEditLayout.setVisibility(View.VISIBLE);
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardUtil.getInstance(UnderActivity.this).show(mEditView);
                    }
                });
                mcv.setVisibility(View.GONE);
                mcv.setCompVisible(false);
            }

            @Override
            public void onConfirm(float locationX, float locationY, String text) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("UnderActivity", "onResume--------");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("UnderActivity", "onConfigurationChanged--------");
    }

    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("i love you " + i);
        }

        return datas;
    }
}
