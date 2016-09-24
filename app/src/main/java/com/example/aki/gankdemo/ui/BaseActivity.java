package com.example.aki.gankdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aki.gankdemo.R;
import com.example.aki.gankdemo.adpter.ContentPagerAdapter;
import com.example.aki.gankdemo.ui.Fragment.MeiziFragment;
import com.example.aki.gankdemo.ui.Fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aki on 2016/9/24.
 */

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTabTitles;
    private List<Fragment> mFragments;
    private ContentPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initViews();
        initData();
        initTab();

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.base_tb);
        setSupportActionBar(mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.base_tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

    }

    private void initData() {
        mTabTitles = new ArrayList<>();
        mTabTitles.add("Meizi");
        mTabTitles.add("Android");
        mTabTitles.add("IOS");

        mFragments = new ArrayList<>();
        mFragments.add(new MeiziFragment());
        mFragments.add(NewsFragment.newInstance("Android"));
        mFragments.add(NewsFragment.newInstance("iOS"));

        mPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitles);
        mViewPager.setAdapter(mPagerAdapter);

    }

    private void initTab() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.gray), ContextCompat.getColor(this, R.color.white));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
//        ViewCompat.setElevation(mTabLayout, 10);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Snackbar.make(mViewPager, "TODO", Snackbar.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
