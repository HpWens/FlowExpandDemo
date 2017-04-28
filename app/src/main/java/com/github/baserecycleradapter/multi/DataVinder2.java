package com.github.baserecycleradapter.multi;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.baserecycleradapter.R;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import jp.satorufujiwara.binder.recycler.RecyclerBinder;

/**
 * desc:
 * author: ws
 * date: 2017/4/25.
 */

public class DataVinder2 extends RecyclerBinder<BinderViewType> {

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;
    private String[] datas = new String[]{"Hello", "IOS", "JAVA", "Python"};
    private List<String> lists = new ArrayList<>();

    protected DataVinder2(Activity activity, BinderViewType viewType) {
        super(activity, viewType);
        for (int i = 0; i < datas.length; i++) {
            lists.add(datas[i]);
        }
    }

    @Override
    public int layoutResId() {
        return R.layout.data_binder_1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(layoutManager);

        holder.mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_img,
                lists) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv, item);
                helper.getView(R.id.tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView mRecyclerView;
        TextView tv;

        public ViewHolder(View view) {
            super(view);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}
