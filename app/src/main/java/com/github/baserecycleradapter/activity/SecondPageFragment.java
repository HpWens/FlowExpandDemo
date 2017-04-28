package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.flow.FlowLayoutView;
import com.github.baserecycleradapter.radius.RadiusTextView;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * author: ws
 * date: 2017/4/24.
 */

public class SecondPageFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<String> datas = new ArrayList<>();

    public static String[] stringCitys = new String[]{"Hello",
            "Android Ios", "张家界", "黄山", "淮北安庆", "阳", "蚌埠", "淮安庆南", "滁州",
            "洛阳", "芜湖", "铜陵", "池州", "宣城", "亳州安庆", "明光", "天", "桐城", "宁安庆安庆国"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        for (int i = 0; i < 10; i++) {
            datas.add(i + "");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recy, datas) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                FlowLayoutView flv = helper.getView(R.id.flow);
                flv.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return stringCitys.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return stringCitys[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder holder = new ViewHolder();

                        if (convertView == null) {
                            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
                            convertView = mInflater.inflate(R.layout.item_flow, parent, false);
                            holder.rv_name = (RadiusTextView) convertView.findViewById(R.id.tv_name);
                            convertView.setTag(holder);
                        } else {
                            holder = (ViewHolder) convertView.getTag();
                        }

                        holder.rv_name.setText(stringCitys[position]);

                        holder.rv_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        return convertView;
                    }
                });
            }
        });


        return view;
    }

    class ViewHolder {
        RadiusTextView rv_name;
    }
}
