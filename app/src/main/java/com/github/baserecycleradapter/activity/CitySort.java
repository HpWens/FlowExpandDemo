package com.github.baserecycleradapter.activity;

import com.github.baserecycleradapter.entity.CityBean;

import java.util.Comparator;

/**
 * desc:
 * author: 文师
 * date: 2017/4/14.
 */

public class CitySort implements Comparator<CityBean> {
    @Override
    public int compare(CityBean o1, CityBean o2) {
        return o1.pinyin.compareTo(o2.pinyin);
    }

}
