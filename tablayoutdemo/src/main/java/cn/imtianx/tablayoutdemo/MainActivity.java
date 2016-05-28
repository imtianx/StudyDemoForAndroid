package cn.imtianx.tablayoutdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Fragment> mFragmentList;
    List<String> mTitles;
    TabFragment1 mFragment1;
    TabFragment2 mFragment2;
    TabFragment3 mFragment3;
    FragmentAdapter mAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.container);

        //添加标题
        mTitles = new ArrayList<>();
        mTitles.add("报价中");
        mTitles.add("运输中");
        mTitles.add("已完成");

        //添加页面
        mFragmentList = new ArrayList<>();
        mFragment1 = new TabFragment1();
        mFragment2 = new TabFragment2();
        mFragment3 = new TabFragment3();
        mFragmentList.add(mFragment1);
        mFragmentList.add(mFragment2);
        mFragmentList.add(mFragment3);

        //初始化适配器
        mAdapter = new FragmentAdapter(getSupportFragmentManager(),
                mTitles, mFragmentList);
        //设置适配器
        mViewPager.setAdapter(mAdapter);
        //加载viewpager
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
