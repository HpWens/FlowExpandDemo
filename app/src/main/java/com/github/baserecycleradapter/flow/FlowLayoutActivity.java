package com.github.baserecycleradapter.flow;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.layoutManager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * author: ws
 * date: 2017/4/27.
 */

public class FlowLayoutActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    Button mBtnExpand;

    Button mBtnCollapse;

    BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mBtnExpand = (Button) findViewById(R.id.btn_expand);
        mBtnCollapse = (Button) findViewById(R.id.btn_collapse);

        mRecyclerView.setLayoutManager(new FlowLayoutManager());

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_flow, getDatas
                ()) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_name, item);

                helper.getView(R.id.tv_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "ScaleX", 0f,
                                1.0f);
                        animator.setDuration(300);
                        animator.start();
                    }
                });
            }
        });

        mBtnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setLayoutManager(new FlowLayoutManager());
            }
        });

        mBtnCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FlowLayoutActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(layoutManager);
            }
        });

    }


    public List<String> getDatas() {
        List<String> datas = new ArrayList<>();

        datas.add("女装");
        datas.add("数码电器");
        datas.add("车票旅行");
        datas.add("运动户外");
        datas.add("浴巾 成人 冬季");
        datas.add("牛肉干");
        datas.add("橘子");
        datas.add("海泰首饰专用");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("小孩 大海 太空");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("梦想");
        datas.add("女装");
        datas.add("数码电器");
        datas.add("车票旅行");
        datas.add("运动户外");
        datas.add("浴巾 成人 冬季");
        datas.add("牛肉干");
        datas.add("橘子");
        datas.add("海泰首饰专用");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("小孩 大海 太空");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("梦想");
        datas.add("女装");
        datas.add("数码电器");
        datas.add("车票旅行");
        datas.add("运动户外");
        datas.add("浴巾 成人 冬季");
        datas.add("牛肉干");
        datas.add("橘子");
        datas.add("海泰首饰专用");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("小孩 大海 太空");
        datas.add("女装");
        datas.add("女装");
        datas.add("女装");
        datas.add("梦想");

        return datas;
    }
}
