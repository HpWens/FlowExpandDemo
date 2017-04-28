package com.github.baserecycleradapter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * Created by YoKeyword on 16/6/3.
 */
public class FirstPagerFragment extends Fragment {

    private FlowLayoutView flv;

    private List<String> datas = new ArrayList<>();

    private SparseBooleanArray mBooleanArray;

    private int mLastCheckedPosition = -1;

    private BaseAdapter mAdapter;

    private View mask;

    public static String[] stringCitys = new String[]{"下拉按钮", "Hello",
            "Android Ios", "Java", "python", "php", "阳", "蚌埠", "淮", "滁州",
            "洛阳", "芜湖", "铜陵", "池州", "宣城", "亳州安庆", "明光"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.flow_activity, container, false);

        flv = (FlowLayoutView) view.findViewById(R.id.flow);

        mask = view.findViewById(R.id.mask);

        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

//                        if (position == 0) {
//                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_widget_expand);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            holder.rv_name.setCompoundDrawables(drawable, null, null, null);
//                        } else {
                        holder.rv_name.setText(datas.get(position));

                        if (position != 0) {
                            holder.rv_name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    flv.setSelection(position);
                                }
                            });
                        }
                        //  }

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
                        RadiusTextView rv = (RadiusTextView) v.findViewById(R.id.tv_name);
                        rv.getDelegate().setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }

                    @Override
                    public void showMask() {
                        mask.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void dismissMask() {
                        mask.setVisibility(View.GONE);
                    }

                    @Override
                    public boolean onFirstLineHeight(int height) {
                        return false;
                    }
                });

                flv.setMultiSelection(false);

                flv.setReverseSelect(true);

            }
        });

        datas = new ArrayList<>();
        mBooleanArray = new SparseBooleanArray(stringCitys.length);

        for (int i = 0; i < stringCitys.length; i++) {
            datas.add(stringCitys[i]);
        }


        return view;
    }

    class ViewHolder {
        RadiusTextView rv_name;
    }
}
