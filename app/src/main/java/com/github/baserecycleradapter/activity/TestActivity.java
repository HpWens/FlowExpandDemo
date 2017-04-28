package com.github.baserecycleradapter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.flow.FlowLayoutView;
import com.github.baserecycleradapter.radius.RadiusTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * author: 文师
 * date: 2017/4/20.
 */

public class TestActivity extends AppCompatActivity {

    private FlowLayoutView flv;

    private List<String> datas = new ArrayList<>();

    private SparseBooleanArray mBooleanArray;

    private int mLastCheckedPosition = -1;

    private BaseAdapter mAdapter;

    public static String[] stringCitys = new String[]{"背景", "背景",
            "Android Ios", "张家界", "黄山", "淮北安庆", "阳", "蚌埠", "淮安庆南", "滁州",
            "洛阳", "芜湖", "铜陵", "池州", "宣城", "亳州安庆", "明光", "天", "桐城", "宁安庆安庆国"
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_activity);


        flv = (FlowLayoutView) findViewById(R.id.flow);

        mBooleanArray = new SparseBooleanArray(stringCitys.length);

        for (int i = 0; i < stringCitys.length; i++) {
            datas.add(stringCitys[i]);
        }

        flv.setAdapter(mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                return datas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder = new ViewHolder();

                if (convertView == null) {
                    LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
                    convertView = mInflater.inflate(R.layout.item_flow, parent, false);
                    holder.rv_name = (RadiusTextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

//                if (position == 0) {
//                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_widget_expand);
//                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                    holder.rv_name.setCompoundDrawables(drawable, null, null, null);
//                } else {
                holder.rv_name.setText(stringCitys[position]);

                holder.rv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flv.setSelection(position);
                    }
                });
                // }

                return convertView;
            }
        });

        flv.setOnItemClickListener(new FlowLayoutView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onSelected(View v) {
                RadiusTextView rv = (RadiusTextView) v.findViewById(R.id.tv_name);
                rv.getDelegate().setBackgroundColor(Color.parseColor("#FF00FF"));
            }

            @Override
            public void onUnSelected(View v, boolean reverseEnable) {

            }

//            @Override
//            public void onUnSelected(View v) {
//                RadiusTextView rv = (RadiusTextView) v.findViewById(R.id.tv_name);
//                rv.getDelegate().setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }

            @Override
            public void showMask() {

            }

            @Override
            public void dismissMask() {

            }

            @Override
            public boolean onFirstLineHeight(int height) {
                return false;
            }
        });

        flv.setMultiSelection(true);
    }


    class ViewHolder {
        RadiusTextView rv_name;
    }

    public void setItemChecked(int position) {
        mBooleanArray.put(position, true);

        if (mLastCheckedPosition > -1) {
            mBooleanArray.put(mLastCheckedPosition, false);
        }

        mAdapter.notifyDataSetChanged();

        mLastCheckedPosition = position;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
