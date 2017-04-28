package com.github.baserecycleradapter.multi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.baserecycleradapter.R;

import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter;

/**
 * desc:
 * author: ws
 * date: 2017/4/25.
 */

public class MultiTypeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private RecyclerBinderAdapter<MultiType, BinderViewType> mAdapter = new RecyclerBinderAdapter<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.add(MultiType.SECTION_1, new DataBinder1(this, BinderViewType.VIEW_TYPE_1));
        mAdapter.add(MultiType.SECTION_2, new DataVinder2(this, BinderViewType.VIEW_TYPE_2));

    }

}
