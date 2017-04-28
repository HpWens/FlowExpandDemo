package com.github.baserecycleradapter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.entity.City;
import com.github.baserecycleradapter.entity.CityBean;
import com.github.baserecycleradapter.utils.StringUtils;
import com.github.library.BaseQuickAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.widget.LetterNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Administrator on 7/27 0027.
 */
public class NavigationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private BaseQuickAdapter<CityBean, BaseViewHolder> mAdapter;

    private TextView tvLetterHeader;
    private TextView tvCenter;

    private List<CityBean> mDatas = new ArrayList<>();

    private List<String> mLetterDatas;

    private boolean move;
    private int selectPosition = 0;

    private LetterNavigationView mNavigationView;

    public static final int HEADER_FIRST_VIEW = 0x00000111;
    public static final int HEADER_VISIBLE_VIEW = 0x00000222;
    public static final int HEADER_NONE_VIEW = 0x00000333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        tvCenter = (TextView) findViewById(R.id.tv_letter_hide);
        mNavigationView = (LetterNavigationView) findViewById(R.id.navigation);
        tvLetterHeader = (TextView) findViewById(R.id.tv_letter_header);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        getDatas();
        mNavigationView.setData(mLetterDatas = getNavigationDatas());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<CityBean, BaseViewHolder>(R.layout.rv_item_city,
                mDatas) {
            @Override
            protected void convert(BaseViewHolder helper, final CityBean item) {

                if (helper.getAdapterPosition() - mAdapter.getHeaderLayoutCount() == 0) {
                    helper.setVisible(R.id.tv_letter_header, true);
                    helper.itemView.setTag(HEADER_FIRST_VIEW);
                } else {
                    if (item.firstPinYin.equals(mDatas.get(helper.getAdapterPosition() - 1 - mAdapter
                            .getHeaderLayoutCount()).firstPinYin)) {
                        helper.setVisible(R.id.tv_letter_header, false);
                        helper.itemView.setTag(HEADER_NONE_VIEW);
                    } else {
                        helper.setVisible(R.id.tv_letter_header, true);
                        helper.itemView.setTag(HEADER_VISIBLE_VIEW);
                    }
                }

//                if (item.hideEnable) {
//                    helper.setVisible(R.id.tv_city, false);
//                } else {
//                    helper.setVisible(R.id.tv_city, true);
//                }

                helper.setText(R.id.tv_letter_header, item.firstPinYin);
                helper.setText(R.id.tv_city, item.name);

                helper.setOnClickListener(R.id.tv_city, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(view, "" + item.cityName, Snackbar.LENGTH_SHORT).show();
                    }
                });

//                helper.setOnClickListener(R.id.tv_letter_header, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        for (CityBean city : mDatas) {
//                            if (city.firstPinYin.equals(item.firstPinYin)) {
//                                city.hideEnable = !city.hideEnable;
//                            }
//                        }
//                        //notifyDataSetChanged();
//                    }
//                });


            }
        });

        addHeaderView();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View transView = recyclerView.findChildViewUnder(
                        tvLetterHeader.getMeasuredWidth(), tvLetterHeader.getMeasuredHeight() - 1);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManager instanceof LinearLayoutManager) {
                    if(((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition()==(mAdapter.getHeaderLayoutCount()-1)){
                        tvLetterHeader.setVisibility(View.GONE);
                    }else {
                        tvLetterHeader.setVisibility(View.VISIBLE);
                    }
                }

                if (transView != null) {
                    TextView tvLetter = (TextView) transView.findViewById(R.id.tv_letter_header);
                    if (tvLetter != null) {
                        String tvLetterStr = tvLetter.getText().toString().trim();
                        String tvHeaderStr = tvLetterHeader.getText().toString().trim();
                        if (!tvHeaderStr.equals(tvLetterStr)) {
                            for (int i = 0; i < mLetterDatas.size(); i++) {
                                if (tvLetterStr.equals(mLetterDatas.get(i))) {
                                    mNavigationView.setSelectorPosition(i);
                                    break;
                                }
                            }
                        }
                        tvLetterHeader.setText(tvLetterStr);
                        if (transView.getTag() != null) {
                            int headerMoveY = transView.getTop() - tvLetterHeader.getMeasuredHeight();
                            int tag = (int) transView.getTag();
                            if (tag == HEADER_VISIBLE_VIEW) {
                                if (transView.getTop() > 0) {
                                    tvLetterHeader.setTranslationY(headerMoveY);
                                } else {
                                    tvLetterHeader.setTranslationY(0);
                                }
                            } else {
                                tvLetterHeader.setTranslationY(0);
                            }
                        }
                    }
                }

                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = selectPosition - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mRecyclerView.getChildAt(n).getTop();
                        //最后的移动
                        mRecyclerView.scrollBy(0, top);
                    }
                }


            }
        });

        mNavigationView.setOnTouchListener(new LetterNavigationView.OnTouchListener() {
            @Override
            public void onTouchListener(String str, boolean hideEnable) {
                tvCenter.setText(str);
                if (hideEnable) {
                    tvCenter.setVisibility(View.GONE);
                } else {
                    tvCenter.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).firstPinYin.equals(str)) {
                        selectPosition = i;
                        break;
                    }
                }
                moveToPosition(selectPosition + mAdapter.getHeaderLayoutCount());
            }

        });

    }

    /**
     * @param n
     */
    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }


    /**
     * @param cityName
     * @return
     */
    public City getCity(String cityName) {
        City city = new City();
        city.cityName = cityName;
        city.hideEnable = false;
        city.cityPinYin = StringUtils.transformPinYin(city.cityName);
        if (city.cityPinYin != null && city.cityPinYin.length() >= 1) {
            city.firstPinYin = city.cityPinYin.substring(0, 1);
        }
        return city;
    }

    /**
     * get data
     */
    public void getDatas() {

        Gson gson = new Gson();

        String s = new String(readAssets(this, "cities.json"));

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                CityBean bean = gson.fromJson(jsonArray.optJSONObject(i).toString(), CityBean.class);

                bean.firstPinYin = bean.pinyin.substring(0, 1);

                mDatas.add(bean);
            }

            Collections.sort(mDatas, new CitySort());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 读取Assets文件
     *
     * @param fileName
     * @return
     */
    public static byte[] readAssets(Context context, String fileName) {
        if (fileName == null || fileName.length() <= 0) {
            return null;
        }
        byte[] buffer = null;
        try {
            InputStream fin = context.getAssets().open(fileName);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer;
        }
    }

    public List<String> getNavigationDatas() {
        List<String> datas = new ArrayList<>();
        for (CityBean city : mDatas) {
            if (!datas.contains(city.firstPinYin)) {
                datas.add(city.firstPinYin);
            }
        }
        return datas;
    }

    public static String[] stringCitys = new String[]{
            "安定", "张家界", "黄山", "淮北", "阜阳", "蚌埠", "淮南", "滁州",
            "洛阳", "芜湖", "铜陵", "安庆", "安阳", "黄山", "六安", "巢湖",
            "池州", "宣城", "亳州", "明光", "天长", "桐城", "宁国",
            "徐州", "连云港", "宿迁", "淮安", "盐城", "扬州", "长安",
            "南通", "镇江", "常州", "无锡", "苏州", "江阴", "广安",
            "邳州", "新沂", "金坛", "溧阳", "常熟", "张家港", "太仓",
            "昆山", "吴江", "如皋", "通州", "海门", "晋江", "大丰",
            "东台", "高邮", "仪征", "江都", "扬中", "句容", "丹阳",
            "兴化", "姜堰", "泰兴", "靖江", "福州", "南平", "三明",
            "邻水", "上海", "深圳", "香港", "乐山", "文淑", "重庆"
    };

    private void addHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.layout_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));
        mAdapter.addHeaderView(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
