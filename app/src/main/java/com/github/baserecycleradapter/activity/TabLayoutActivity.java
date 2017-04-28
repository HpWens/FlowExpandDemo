package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.baserecycleradapter.R;

/**
 * desc:
 * author: ws
 * date: 2017/4/22.
 */

public class TabLayoutActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;

    private String[] mTab = new String[]{"推荐", "热门", "收藏"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new FirstPagerFragment();
            }

            @Override
            public int getCount() {
                return mTab.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTab[position];
            }

        });
    }
}
